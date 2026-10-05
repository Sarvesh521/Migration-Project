package com.example.app.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.app.annotation.MethodMetadata;

@Configuration
public class KeycloakAdminConfig {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String adminClientId;

    @Value("${keycloak.admin.client-secret}")
    private String adminClientSecret;

    @Bean
    @MethodMetadata(irId = "custom:config:KeycloakAdminConfig:keycloakAdminClient()", hash = "b3a64449", zone = 1)
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl).realm(realm).grantType(OAuth2Constants.CLIENT_CREDENTIALS).clientId(adminClientId).clientSecret(adminClientSecret).build();
    }
}
