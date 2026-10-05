package com.example.app.controller;

import com.example.app.dto.KeycloakAuthResponse;
import com.example.app.dto.LoginUserDto;
import com.example.app.dto.RegisterUserDto;
import com.example.app.dto.UserResource;
import com.example.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;
import com.example.app.annotation.MethodMetadata;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Admin-only: creates the entity record in the database and a matching
     * Keycloak user, then assigns the default role.
     *
     * POST /add_user
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'superAdmin', 'SUPER_ADMIN', 'admin', 'SUPERADMIN')")
    @PostMapping("/add_user")
    @MethodMetadata(irId = "custom:controller:AuthController:addUser(UserResource)", hash = "6d1ffc0c", zone = 1)
    public ResponseEntity<String> addUser(@RequestBody UserResource userResource) {
        String result = authService.addUser(userResource);
        if ("User already exists".equals(result)) {
            return ResponseEntity.status(409).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Self-registration: user creates their own account.
     *
     * POST /register
     */
    @PostMapping("/register")
    @MethodMetadata(irId = "custom:controller:AuthController:register(UserResource)", hash = "e65cae6c", zone = 1)
    public ResponseEntity<String> register(@RequestBody UserResource userResource) {
        String result = authService.register(userResource);
        if ("User already exists".equals(result)) {
            return ResponseEntity.status(409).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Authenticates the user via the Keycloak ROPC grant and returns an
     * access token + refresh token.
     *
     * POST /login
     */
    @PostMapping("/login")
    @MethodMetadata(irId = "custom:controller:AuthController:login(LoginUserDto)", hash = "9b6de906", zone = 1)
    public ResponseEntity<KeycloakAuthResponse> login(@RequestBody LoginUserDto loginUserDto) {
        KeycloakAuthResponse response = authService.login(loginUserDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Revokes the current Keycloak session / token.
     *
     * POST /logout
     * Header: Authorization: Bearer &lt;token&gt;
     */
    @PostMapping("/logout")
    @MethodMetadata(irId = "custom:controller:AuthController:logout(String)", hash = "a7377714", zone = 1)
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok("Logged out successfully.");
    }

    /**
     * Admin-only bulk user creation.
     *
     * POST /add_users
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'superAdmin', 'SUPER_ADMIN', 'admin', 'SUPERADMIN')")
    @PostMapping("/add_users")
    @MethodMetadata(irId = "custom:controller:AuthController:addUsers(List<UserResource>)", hash = "67751f21", zone = 1)
    public ResponseEntity<List<String>> addUsers(@RequestBody List<UserResource> userResources) {
        List<String> results = authService.addUsers(userResources);
        boolean hasFailure = results.stream().anyMatch(r -> r.contains("Failed") || r.contains("already exists"));
        if (hasFailure) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(results);
        }
        return ResponseEntity.ok(results);
    }

    /**
     * Admin-only: looks up a Keycloak user by username and assigns a
     * client-level role.
     *
     * POST /assign-role
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'superAdmin', 'SUPER_ADMIN', 'admin', 'SUPERADMIN')")
    @PostMapping("/assign-role")
    @MethodMetadata(irId = "custom:controller:AuthController:assignRoleByAdmin(Map<String,String>)", hash = "c2b3e837", zone = 1)
    public ResponseEntity<String> assignRoleByAdmin(@RequestBody Map<String, String> body) {
        String userName = body.get("userName");
        String roleName = body.get("roleName");
        authService.assignClientRole(userName, roleName);
        return ResponseEntity.ok("Role '" + roleName + "' assigned to user '" + userName + "' successfully.");
    }
}
