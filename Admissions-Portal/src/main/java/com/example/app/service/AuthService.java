package com.example.app.service;

import com.example.app.dto.KeycloakAuthResponse;
import com.example.app.dto.LoginUserDto;
import com.example.app.dto.UserResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.app.annotation.MethodMetadata;

/**
 * Provider-agnostic authentication facade used by AuthController.
 *
 * This class's public API is stable regardless of which auth provider is configured
 * in the Integration IR (Keycloak, Okta, Auth0, ...) — it simply delegates every call
 * to the {@link AuthProvider} bean generated for that provider (currently: KeycloakService).
 * Swapping providers means regenerating a different AuthProvider implementation; this
 * class and AuthController never need to change.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthProvider authProvider;

    @MethodMetadata(irId = "custom:service:AuthService:addUser(UserResource)", hash = "45fe3f30", zone = 1)
    public String addUser(UserResource userResource) {
        return authProvider.addUser(userResource);
    }

    @MethodMetadata(irId = "custom:service:AuthService:register(UserResource)", hash = "fafaa762", zone = 1)
    public String register(UserResource userResource) {
        return authProvider.register(userResource);
    }

    @MethodMetadata(irId = "custom:service:AuthService:login(LoginUserDto)", hash = "53a5297e", zone = 1)
    public KeycloakAuthResponse login(LoginUserDto loginUserDto) {
        return authProvider.login(loginUserDto);
    }

    @MethodMetadata(irId = "custom:service:AuthService:logout(String)", hash = "670bf8ec", zone = 1)
    public void logout(String authHeader) {
        authProvider.logout(authHeader);
    }

    @MethodMetadata(irId = "custom:service:AuthService:addUsers(List<UserResource>)", hash = "e5eb5280", zone = 1)
    public List<String> addUsers(List<UserResource> userResources) {
        return authProvider.addUsers(userResources);
    }

    @MethodMetadata(irId = "custom:service:AuthService:assignClientRole(String,String)", hash = "ca3e1d88", zone = 1)
    public void assignClientRole(String username, String roleName) {
        authProvider.assignClientRole(username, roleName);
    }
}
