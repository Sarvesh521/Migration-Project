# Spring Boot Compilation Error Resolution Plan

## 1. Current situation

`mvn spring-boot:run` does not reach application startup because Maven fails during the `test-compile` phase. The MySQL container is running on port `3306`, but that only provides a database service; Java source compilation must succeed before Spring Boot can connect to it.

The checksum warning for the Shibboleth repository is not the cause of the build failure. The blocking issues are Java compilation errors in the application source.

## 2. Error categories and confirmed causes

### A. Duplicate `ApplicationController` class

`src/main/java/com/example/app/controller/ApplicationController.java` contains two public classes with the same fully qualified name:

```java
public class ApplicationController {
    // Save draft application endpoint
}

public class ApplicationController {
    // Submit application after payment endpoint
}
```

Java permits only one public class with a given name in a source package. This directly causes:

```text
duplicate class: com.example.app.controller.ApplicationController
```

### B. Missing imports in `SaveDraftApplicationRequest`

`src/main/java/com/example/app/dto/SaveDraftApplicationRequest.java` declares fields using:

- `Application`
- `ApplicationEducation`
- `ApplicationExamScore`
- `ApplicationPreference`
- `ApplicationDocument`

The DTO is in `com.example.app.dto`, while these classes are in `com.example.app.entity`. Because they are not in the same package and are not imported, javac reports each type as missing.

### C. Lombok-generated methods are unavailable

The project uses Lombok annotations such as `@Data`, `@Getter`, `@Setter`, and `@Slf4j`. The affected source files include:

- `entity/Drive.java`
- `entity/Round.java`
- `dto/LoginUserDto.java`
- `service/KeycloakAuthService.java`

The compiler errors show that methods and fields expected from Lombok are absent:

- `Drive`: `get...` and `set...` methods
- `Round`: `set...` methods
- `LoginUserDto`: `getUserName()` and `getPassword()`
- `KeycloakAuthService`: the `log` field from `@Slf4j`

The `pom.xml` declares Lombok as a dependency, so the next step is to verify whether annotation processing is actually running and whether the Lombok artifact is compatible with the active Java 17 toolchain. If processing is not being discovered reliably, configure Lombok explicitly as a compiler annotation processor rather than relying only on implicit discovery.

## 3. Resolution sequence

Apply the fixes in this order so that each compile run exposes the next independent problem clearly:

1. **Merge the two controller declarations**
   - Keep one `ApplicationController` class.
   - Preserve both endpoint methods and their existing annotations.
   - Keep only one `@RestController` and one `@RequestMapping` for the combined controller.
   - Confirm the mapping string is intentional before changing it; the current value contains a leading space (`"/ applications"`), which is separate from the reported compilation failure.

2. **Import the entity types in the draft request DTO**
   - Add imports from `com.example.app.entity` for all five referenced classes.
   - Keep the existing request shape and accessors unchanged unless later compilation reveals a type mismatch.

3. **Repair and verify Lombok annotation processing**
   - Confirm the Maven dependency tree contains Lombok and identify the resolved version.
   - Run Maven with compiler diagnostics if needed to confirm annotation processors are discovered.
   - Configure Lombok explicitly in `maven-compiler-plugin` using the project’s Java 17 compiler if implicit processing is not active.
   - Avoid replacing Lombok-generated methods manually across entities unless annotation processing cannot be supported, because that would create unnecessary duplicated boilerplate.
   - Ensure the IDE is also configured to enable annotation processing so editor diagnostics match Maven.

4. **Recompile and address only newly exposed errors**
   - Run `mvn clean test-compile`.
   - Recheck all errors in the affected controller, DTO, entities, and services.
   - Do not treat the database container as a compilation fix; database connectivity is validated only after the application starts.

5. **Start the application**
   - Run `mvn spring-boot:run`.
   - If startup then fails, investigate runtime configuration separately: datasource URL, credentials, schema initialization, Keycloak settings, and port conflicts.

## 4. Validation checklist

The fix is complete when:

- `mvn clean test-compile` completes successfully.
- No duplicate-class or missing-symbol errors remain.
- Lombok-generated getters, setters, and the `log` field resolve during compilation.
- `mvn spring-boot:run` reaches Spring Boot startup rather than failing in compilation.
- The application can connect to the MySQL container on `localhost:3306` using the configured datasource settings.
- The draft-save and submit-after-payment controller mappings are both present exactly once.

## 5. Important distinction

Docker status and Maven compilation are independent stages:

```text
Docker MySQL running -> database is available
Maven compile passes  -> Java sources are valid
Spring Boot starts   -> application can initialize and then connect to MySQL
```

The current failure is in the second stage, before any database connection is attempted.

## 6. Fix log: errors and warnings addressed

The following changes were made after the original compilation errors and subsequent runtime startup errors were reported. Line numbers refer to the current source files after the fixes.

| # | Error or warning | File and location | Change made |
|---|---|---|---|
| 1 | Duplicate class `com.example.app.controller.ApplicationController` | `src/main/java/com/example/app/controller/ApplicationController.java`, classes near lines 18 and 37 | Merged the save-draft and submit-after-payment endpoints into one `ApplicationController` class. |
| 2 | Missing entity symbols in `SaveDraftApplicationRequest` | `src/main/java/com/example/app/dto/SaveDraftApplicationRequest.java`, imports near line 3 | Imported `Application`, `ApplicationEducation`, `ApplicationExamScore`, `ApplicationPreference`, and `ApplicationDocument` from `com.example.app.entity`. |
| 3 | Missing Lombok-generated getters, setters, and `log` field | `pom.xml`, compiler plugin under `<build><plugins>`; affected classes include `Drive`, `Round`, `LoginUserDto`, and `KeycloakAuthService` | Added an explicit Lombok annotation processor path using Lombok `1.18.30`, so javac generates the methods and logger field during Maven compilation. |
| 4 | Missing repository fields and entity imports in generated services | `ApplicationEducationService`, `ApplicationExamScoreService`, `ApplicationDocumentService`, `ApplicationPreferenceService`, `ApplicationOfferHistoryService`, `ApplicationPaymentService`, and `QueryMessageService`, imports and injected fields near the top of each class | Added the missing entity imports, repository imports, and `@Autowired` repository fields used by each service. |
| 5 | Missing return statements in `Void` methods | `DriveService.updateDriveConfiguration` line 137; `ApplicationService.submitApplicationAfterPayment` line 221; `QueryService.respondToQueryAndUpdateStatus` line 149; `ApplicationRoundService` methods near lines 255, 274, 297, and 325 | Added explicit `return null` statements to satisfy the existing `Void` method signatures after persistence completes. |
| 6 | `Optional<Application>` assigned directly to `Application` | `ApplicationService.startNewApplicationForDrive`, line 108 | Changed the repository call to `.orElse(null)` before checking whether an application already exists. |
| 7 | Query/controller signature mismatch for query responses | `QueryController.respondToQueryAndUpdateStatus`, line 121; `QueryService.respondToQueryAndUpdateStatus`, lines 134-149 | Passed `request.getReplyText()` and `request.getNewStatus()` to the service instead of passing the whole request object. |
| 8 | `InviteCandidatesRequest` missing `roundId` | `src/main/java/com/example/app/dto/InviteCandidatesRequest.java`, field/accessor section after line 27 | Added the `roundId` field, getter, and setter required by `ApplicationRoundController`. |
| 9 | Void response incorrectly used as a response body | `ApplicationRoundController.processSlidingAllocationRound`, line 143 | Invoked the void service method separately and returned `ResponseEntity.status(...).build()`. |
| 10 | Entity response type did not match controller DTO type | `ApplicationRoundController.inviteCandidatesToOnlineRound`, line 154 | Added conversion from `ApplicationRound` entities to `ApplicationRoundResponse` DTOs before returning the response. |
| 11 | Invalid application-round repository method names/queries | `ApplicationRoundRepository`, lines 18-29 | Added explicit JPQL for `findByRoundIdAndOfferStatusSliding` and corrected `findByApplicationIdInAndRoundId` to query `applicationId IN :applicationIds`. |
| 12 | Application-round service saved offer history through the wrong repository | `ApplicationRoundService`, lines 175 and 201 | Injected and used `ApplicationOfferHistoryRepository` instead of saving `ApplicationOfferHistory` through `ApplicationRoundRepository`. |
| 13 | Undefined sliding-allocation helper | `ApplicationRoundService`, line 184 | Removed the call to the nonexistent `determineUpgradedProgramme` method and retained the current allocated programme until a real allocation algorithm is provided. |
| 14 | Duplicate JPA physical column mapping for `drive_id` and related foreign keys | `Application.java` lines 43, 47, 168, 172; `Round.java` lines 44 and 140; relationship entities listed below | Added explicit scalar column names and marked relationship joins `insertable = false, updatable = false` so each foreign-key column has one writable mapping. |
| 15 | Duplicate foreign-key column annotations introduced while normalizing mappings | `ApplicationEducation`, `ApplicationExamScore`, `ApplicationPreference`, `ApplicationDocument`, `ApplicationPayment`, `ApplicationOfferHistory`, `ApplicationRound`, `ApplicationWithdrawal`, `Query`, and `QueryMessage` | Removed duplicate `@Column` annotations and retained one explicit `@Column(name = "...")` per scalar foreign-key field. |
| 16 | MySQL reserved column name `rank` prevented schema creation | `ApplicationExamScore.java`, field near line 58 | Mapped the Java `rank` field to the database column `exam_rank`. |
| 17 | Invalid `DriveRepository.findByDriveId` derived query | `DriveRepository.java`, removed method originally near line 14 | Removed the method because `Drive` has `id`, not `driveId`; callers use the standard `findById`. |
| 18 | Invalid `RoundRepository.findByRoundId` derived query | `RoundRepository.java`, removed method originally near line 18 | Removed the method because the entity identifier is `id`; callers use `findById`. |
| 19 | Invalid `findMaxRoundNumberByDriveId` derived query | `RoundRepository.java`, method near lines 13-16; `RoundService.java` call near line 180 | Added explicit JPQL using `Round.driveId`, allowing the existing service method name to remain unchanged. |
| 20 | Invalid `ApplicationRoundRepository.findByRoundIdAndOfferStatusSliding` property parsing | `ApplicationRoundRepository.java`, method near lines 17-20 | Added explicit JPQL so `offerStatus` is compared directly instead of being parsed as a nonexistent nested property. |
| 21 | Keycloak issuer metadata lookup blocked startup when Keycloak was not running | `src/main/resources/application.yml`, `spring.security.oauth2.client` section | Removed unused OAuth2 client registration/provider configuration. Resource-server opaque-token settings remain for API authentication. |

## 7. Warnings that remain intentionally

These messages are not unresolved compilation failures:

- `MySQLDialect does not need to be specified explicitly`: Hibernate 6 can detect MySQL automatically. The explicit `spring.jpa.database-platform` setting in `application.yml` can be removed later as cleanup.
- `spring.jpa.open-in-view is enabled by default`: this is a Spring warning, not a startup failure. It can be addressed separately by setting `spring.jpa.open-in-view: false` after confirming no controller/view requires lazy loading.
- `KeycloakInitializer: Failed to initialize Keycloak roles`: the application still starts, but role initialization cannot contact Keycloak at `localhost:4000`. Running a correctly configured Keycloak instance at that address, realm, and credentials will remove this warning. MySQL connectivity and Spring Boot startup are already successful.

## 8. Exact changed-file and line-number index

The following index gives the current line numbers for the implemented fixes. These line numbers should be refreshed if later edits are made above the listed code.

### Compilation fixes

| Error fixed | Exact file | Current line(s) | Modified class/method |
|---|---|---:|---|
| Duplicate controller class | `src/main/java/com/example/app/controller/ApplicationController.java` | 18 | `ApplicationController`; both endpoints now share this single class |
| Missing DTO entity types | `src/main/java/com/example/app/dto/SaveDraftApplicationRequest.java` | 3-7 | `SaveDraftApplicationRequest` imports |
| Lombok methods/logger unavailable | `pom.xml` | 109-115 | `maven-compiler-plugin` annotation processor configuration |
| Missing service repositories/entities | `src/main/java/com/example/app/service/ApplicationEducationService.java` | 6-8, 24; `ApplicationExamScoreService.java` 6-8, 24; `ApplicationDocumentService.java` 6-8, 24; `ApplicationPreferenceService.java` 6-8, 24 | Service imports and injected repository fields |
| Missing offer-history/payment/query-message repositories | `src/main/java/com/example/app/service/ApplicationOfferHistoryService.java` | 6-9, 29; `ApplicationPaymentService.java` 6-9, 29; `QueryMessageService.java` 6-7, 24 | Service imports and injected repository fields |
| Missing `Void` returns | `src/main/java/com/example/app/service/DriveService.java` | 137; `ApplicationService.java` 221; `QueryService.java` 149; `ApplicationRoundService.java` 255, 274, 297, 325 | `updateDriveConfiguration`, `submitApplicationAfterPayment`, `respondToQueryAndUpdateStatus`, and online-round methods |
| Optional assigned to entity | `src/main/java/com/example/app/service/ApplicationService.java` | 108 | `startNewApplicationForDrive` |
| Query request signature mismatch | `src/main/java/com/example/app/controller/QueryController.java` | 121 | `respondToQueryAndUpdateStatus` controller method |
| Missing invitation round ID | `src/main/java/com/example/app/dto/InviteCandidatesRequest.java` | 25-32 | `roundId` field, getter, and setter |
| Void response used as body | `src/main/java/com/example/app/controller/ApplicationRoundController.java` | 142-144 | `processSlidingAllocationRound` |
| Entity response returned where DTO was declared | `src/main/java/com/example/app/controller/ApplicationRoundController.java` | 153-176 | `inviteCandidatesToOnlineRound` and `toApplicationRoundResponse` |
| Invalid application-round repository query | `src/main/java/com/example/app/repository/ApplicationRoundRepository.java` | 19-25 | `findByRoundIdAndOfferStatusSliding`, `findByApplicationIdInAndRoundId` |
| Wrong repository used for offer history | `src/main/java/com/example/app/service/ApplicationRoundService.java` | 175, 201 | `applicationOfferHistoryRepository` injection and save |
| Missing sliding-allocation helper | `src/main/java/com/example/app/service/ApplicationRoundService.java` | 184 | `processSlidingAllocationRound` |

### Runtime and JPA fixes

| Runtime error/warning fixed | Exact file | Current line(s) | Modified class/method |
|---|---|---:|---|
| Duplicate `drive_id` logical/physical mapping | `src/main/java/com/example/app/entity/Application.java` | 43, 47, 168, 172 | Scalar `userId`/`driveId` columns and `user`/`drive` joins |
| Duplicate `drive_id` mapping on rounds | `src/main/java/com/example/app/entity/Round.java` | 44, 140 | `driveId` column and `drive` join |
| Duplicate application/round foreign-key mappings | `src/main/java/com/example/app/entity/ApplicationEducation.java` 42, 63; `ApplicationExamScore.java` 44, 70; `ApplicationPreference.java` 42, 55; `ApplicationDocument.java` 42, 62 | Listed lines | Scalar IDs and read-only relationships |
| Duplicate application/round foreign-key mappings | `src/main/java/com/example/app/entity/ApplicationPayment.java` 45, 48, 84, 88; `ApplicationOfferHistory.java` 43, 47, 65, 69; `ApplicationRound.java` 42, 46, 66, 70 | Listed lines | Scalar IDs and read-only relationships |
| Duplicate application/query foreign-key mappings | `src/main/java/com/example/app/entity/ApplicationWithdrawal.java` 46, 87; `Query.java` 45, 64; `QueryMessage.java` 44, 64 | Listed lines | Scalar IDs and read-only relationships |
| MySQL reserved column `rank` | `src/main/java/com/example/app/entity/ApplicationExamScore.java` | 58 | `rank` mapped to `exam_rank` |
| Invalid `Drive.driveId` repository property | `src/main/java/com/example/app/repository/DriveRepository.java` | 10-12 | Removed invalid `findByDriveId` method; standard `findById` remains |
| Invalid `Round.roundId` repository property | `src/main/java/com/example/app/repository/RoundRepository.java` | 15-16 | Removed invalid `findByRoundId` method |
| Maximum round query could not be derived | `src/main/java/com/example/app/repository/RoundRepository.java` | 15 | Explicit JPQL for `findMaxRoundNumberByDriveId` |
| Sliding query parsed invalid property path | `src/main/java/com/example/app/repository/ApplicationRoundRepository.java` | 19 | Explicit JPQL for round, offer-status filtering |
| Keycloak OAuth2 metadata lookup blocked startup | `src/main/resources/application.yml` | 19-20 | Removed unused `oauth2.client` block; retained `resourceserver` configuration |

### Remaining warnings

| Warning | Exact file/location | Status |
|---|---|---|
| Explicit MySQL dialect is unnecessary | `src/main/resources/application.yml`, lines 10-11 | Non-fatal; Hibernate still starts. |
| Open EntityManager in View is enabled | Spring Boot runtime configuration; no source error | Non-fatal; not changed because it is a separate application design decision. |
| Keycloak role initialization cannot connect | `src/main/java/com/example/app/config/KeycloakInitializer.java`, `run` method; runtime endpoint is configured through `application.yml` | Non-fatal; will disappear when Keycloak is available at the configured `localhost:4000` realm and credentials. |
