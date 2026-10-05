package com.example.app.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.ProtocolMapperRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.example.app.annotation.MethodMetadata;

@Slf4j
@Configuration
public class KeycloakInitializer implements CommandLineRunner {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.default-role}")
    private String keycloakDefaultRole;

    @Value("${keycloak.client-id}")
    private String adminClientId;

    /**
     * Roles are read from application.yml at runtime (keycloak.roles).
     * The SpEL expression splits the comma-separated string into a List.
     * Example YAML:  keycloak.roles: ADMIN,DOCTOR,RECEPTIONIST
     */
    @Value("#{'${keycloak.roles}'.split(',')}")
    private List<String> roles;

    public KeycloakInitializer(Keycloak keycloakAdmin) {
        this.keycloakAdmin = keycloakAdmin;
    }

    @Override
    @MethodMetadata(irId = "custom:config:KeycloakInitializer:run(String)", hash = "12276cc0", zone = 1)
    public void run(String... args) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            // Find client
            List<ClientRepresentation> clients = realmResource.clients().findByClientId(adminClientId);
            if (clients.isEmpty()) {
                throw new RuntimeException("Client not found: " + adminClientId);
            }
            // Internal UUID
            String clientUuid = clients.get(0).getId();
            // Client resource
            ClientResource clientResource = realmResource.clients().get(clientUuid);
            // Ensure the client supports the Resource Owner Password Credentials
            // grant (grant_type=password) — required for /auth/login to work.
            // Without this, Keycloak rejects every password-grant token request
            // with 401, regardless of whether the user/credentials are correct.
            ensureDirectAccessGrantsEnabled(clientResource);
            // Client roles resource
            RolesResource rolesResource = clientResource.roles();
            List<String> existingRoles = rolesResource.list().stream().map(RoleRepresentation::getName).toList();
            // Create roles read from application.yml (keycloak.roles)
            for (String role : roles) {
                createRoleIfNotExists(rolesResource, existingRoles, role);
            }
            // Create custom_id protocol mapper on the client
            createCustomIdProtocolMapper(clientResource);
            // NOTE: a "default role for new registrations" must NEVER be
            // implemented by adding a client role to the realm's
            // "default-roles-{realm}" composite — that composite is granted to
            // EVERY user in the realm (existing and future, including every
            // test/service account), not just newly self-registered ones. Doing
            // so silently grants that role's authority to every other account
            // too, defeating any @PreAuthorize/hasAnyRole(...) check that's
            // supposed to exclude it (confirmed: this let every wrong-role
            // Karate scenario succeed instead of getting 403). If a default
            // role for self-registration is needed, assign it directly to that
            // one user at registration time (see KeycloakAuthService.register())
            // rather than via this realm-wide composite.
            log.info("Keycloak role initialization complete.");
        } catch (Exception e) {
            log.error("Failed to initialize Keycloak roles. Ensure Keycloak is running and admin credentials are correct.", e);
        }
    }

    // -------------------------------------------------------------------------
    // ROLE HELPERS
    // -------------------------------------------------------------------------
    @MethodMetadata(irId = "custom:config:KeycloakInitializer:createRoleIfNotExists(RolesResource,List<String>,String)", hash = "1b6c2bb4", zone = 1)
    private void createRoleIfNotExists(RolesResource rolesResource, List<String> existingRoles, String roleName) {
        if (!existingRoles.contains(roleName)) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            role.setDescription("Client Role: " + roleName);
            rolesResource.create(role);
            log.info("Created client role: {}", roleName);
        } else {
            log.info("Client role already exists: {}", roleName);
        }
    }

    // -------------------------------------------------------------------------
    // CLIENT CONFIG – enable password grant for /auth/login
    // -------------------------------------------------------------------------
    @MethodMetadata(irId = "custom:config:KeycloakInitializer:ensureDirectAccessGrantsEnabled(ClientResource)", hash = "620eb5b5", zone = 1)
    private void ensureDirectAccessGrantsEnabled(ClientResource clientResource) {
        try {
            ClientRepresentation rep = clientResource.toRepresentation();
            if (!Boolean.TRUE.equals(rep.isDirectAccessGrantsEnabled())) {
                rep.setDirectAccessGrantsEnabled(true);
                clientResource.update(rep);
                log.info("Enabled Direct Access Grants (password grant) on client '{}'", rep.getClientId());
            }
        } catch (Exception e) {
            log.warn("Could not verify/enable Direct Access Grants on client: {}", e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // PROTOCOL MAPPER – custom_id claim in tokens
    // -------------------------------------------------------------------------
    /**
     * Creates an OIDC protocol mapper on the client so the {@code custom_id}
     * user attribute is included in access tokens, ID tokens and userinfo
     * responses.
     *
     * <p>Step 2 of 2 for {@code custom_id} support.
     */
    @MethodMetadata(irId = "custom:config:KeycloakInitializer:createCustomIdProtocolMapper(ClientResource)", hash = "749601d5", zone = 1)
    private void createCustomIdProtocolMapper(ClientResource clientResource) {
        try {
            List<ProtocolMapperRepresentation> existing = clientResource.getProtocolMappers().getMappers();
            boolean exists = existing.stream().anyMatch(m -> "custom_id".equals(m.getName()));
            if (!exists) {
                ProtocolMapperRepresentation mapper = new ProtocolMapperRepresentation();
                mapper.setName("custom_id");
                mapper.setProtocol("openid-connect");
                mapper.setProtocolMapper("oidc-usermodel-attribute-mapper");
                Map<String, String> config = new HashMap<>();
                config.put("user.attribute", "custom_id");
                config.put("claim.name", "custom_id");
                config.put("jsonType.label", "String");
                config.put("id.token.claim", "true");
                config.put("access.token.claim", "true");
                config.put("userinfo.token.claim", "true");
                mapper.setConfig(config);
                clientResource.getProtocolMappers().createMapper(mapper);
                log.info("Created protocol mapper for 'custom_id'.");
            } else {
                log.info("Protocol mapper 'custom_id' already exists.");
            }
        } catch (Exception e) {
            log.warn("Could not create protocol mapper for 'custom_id': {}", e.getMessage());
        }
    }
}
