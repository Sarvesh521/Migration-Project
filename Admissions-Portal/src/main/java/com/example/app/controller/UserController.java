package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateUserRequest;
import com.example.app.dto.UpdateUserRequest;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/users")
public class UserController {

    @Autowired()
    private UserService userService;

    @MethodMetadata(irId = "create_user_ctrl_crg", hash = "b11e0a37", zone = 1)
    @PostMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<User> createUser(@RequestParam(required = false) CreateUserRequest request) {
        beforeCreateUser(request);
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(userService.createUser(request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateUser(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_user_ctrl_crg", hash = "130764ef", zone = 2)
    public void beforeCreateUser(@RequestParam(required = false) CreateUserRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_user_ctrl_crg", hash = "6adb8b13", zone = 2)
    public void afterCreateUser(@RequestParam(required = false) CreateUserRequest request) {
    }

    @MethodMetadata(irId = "get_user_by_id_ctrl_2fk", hash = "c1394f31", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        beforeGetUserById(id);
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetUserById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_user_by_id_ctrl_2fk", hash = "e5ebbb62", zone = 2)
    public void beforeGetUserById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_user_by_id_ctrl_2fk", hash = "a50908aa", zone = 2)
    public void afterGetUserById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_users_ctrl_79u", hash = "3062b047", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        beforeListUsers();
        try {
            return ResponseEntity.ok(userService.listUsers());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListUsers();
        }
    }

    @MethodMetadata(irId = "hook-before-list_users_ctrl_79u", hash = "1cc83613", zone = 2)
    public void beforeListUsers() {
    }

    @MethodMetadata(irId = "hook-after-list_users_ctrl_79u", hash = "b734b73a", zone = 2)
    public void afterListUsers() {
    }

    @MethodMetadata(irId = "update_user_ctrl_8z6", hash = "a05d70ed", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestParam(required = false) UpdateUserRequest request) {
        beforeUpdateUser(id, request);
        try {
            return ResponseEntity.ok(userService.updateUser(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateUser(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_user_ctrl_8z6", hash = "4191e3d1", zone = 2)
    public void beforeUpdateUser(@PathVariable String id, @RequestParam(required = false) UpdateUserRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_user_ctrl_8z6", hash = "3966aa79", zone = 2)
    public void afterUpdateUser(@PathVariable String id, @RequestParam(required = false) UpdateUserRequest request) {
    }

    @MethodMetadata(irId = "delete_user_ctrl_drs", hash = "1e15fa78", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        beforeDeleteUser(id);
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteUser(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_user_ctrl_drs", hash = "aebc8d08", zone = 2)
    public void beforeDeleteUser(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_user_ctrl_drs", hash = "6b1b0b4c", zone = 2)
    public void afterDeleteUser(@PathVariable String id) {
    }
}
