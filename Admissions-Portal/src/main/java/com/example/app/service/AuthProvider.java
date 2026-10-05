package com.example.app.service;

import com.example.app.dto.KeycloakAuthResponse;
import com.example.app.dto.LoginUserDto;
import com.example.app.dto.UserResource;
import java.util.List;
import com.example.app.annotation.MethodMetadata;

/**
 * Stable authentication provider abstraction.
 *
 * The available operations are derived from:
 *
 * integrations[].apis
 *
 * If no authentication integration is configured,
 * this interface contains no provider-specific operations.
 */
public interface AuthProvider {

    @MethodMetadata(irId = "custom:service:AuthProvider:addUser(UserResource)", hash = "513a2060", zone = 1)
    String addUser(UserResource userResource);

    @MethodMetadata(irId = "custom:service:AuthProvider:register(UserResource)", hash = "85422da0", zone = 1)
    String register(UserResource userResource);

    @MethodMetadata(irId = "custom:service:AuthProvider:login(LoginUserDto)", hash = "30bdb95a", zone = 1)
    KeycloakAuthResponse login(LoginUserDto loginUserDto);

    @MethodMetadata(irId = "custom:service:AuthProvider:logout(String)", hash = "5a5152c8", zone = 1)
    void logout(String authHeader);

    @MethodMetadata(irId = "custom:service:AuthProvider:addUsers(List<UserResource>)", hash = "0dd21946", zone = 1)
    List<String> addUsers(List<UserResource> userResources);

    @MethodMetadata(irId = "custom:service:AuthProvider:assignClientRole(String,String)", hash = "e6ada75f", zone = 1)
    void assignClientRole(String username, String roleName);
}
