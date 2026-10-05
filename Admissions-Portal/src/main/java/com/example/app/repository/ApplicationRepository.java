package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationRepository extends JpaRepository<Application, String> {

    // repo_method_id: a_exists_by_user_and_drive_fz8
    @MethodMetadata(irId = "a_exists_by_user_and_drive_fz8", hash = "6be3b4cd", zone = 1)
    public Optional<Application> findByUserIdAndDriveId(String userId, String driveId);

    // repo_method_id: findApplicationByUserAndId_ci9 | Derived query to verify applicant owns the application
    @MethodMetadata(irId = "findApplicationByUserAndId_ci9", hash = "fc7ef766", zone = 1)
    public Optional<Application> findByUserIdAndId(String userId, String id);

    // repo_method_id: findByDriveIdAndStatus_ba5 | Derived query to fetch applications by drive and status.
    @MethodMetadata(irId = "findByDriveIdAndStatus_ba5", hash = "90421f0f", zone = 1)
    public List<Application> findByDriveIdAndStatus(String driveId, String status);

    // repo_method_id: findEligibleCandidatesForOfflineRound_zse | Finds applications eligible for offline round based on prior round completion and payment success.
    @MethodMetadata(irId = "findEligibleCandidatesForOfflineRound_zse", hash = "8af91edd", zone = 1)
    @Query(value = "SELECT a FROM Application a WHERE a.id IN (SELECT ar.applicationId FROM ApplicationRound ar WHERE ar.roundId = :roundId AND ar.paymentStatus = 'SUCCESS' AND ar.roundStatus IN ('ONLINE_ALLOCATED', 'SLIDING') AND ar.offerStatus IN ('OFFER_SENT', 'ACCEPTED')) ORDER BY a.id", nativeQuery = false)
    public List<Application> findEligibleCandidatesForOfflineRound(String roundId);
}
