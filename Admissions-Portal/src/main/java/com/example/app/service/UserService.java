package com.example.app.service;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.CreateUserRequest;
import com.example.app.dto.UpdateUserRequest;
import com.example.app.entity.User;
import com.example.app.exception.AlreadyExistsException;
import com.example.app.exception.NotFoundException;
import com.example.app.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service()
public class UserService {

    @Autowired()
    private UserRepository userRepository;

    @MethodMetadata(irId = "create_user_svc_nkt", hash = "a6e7b443", zone = 1)
    public User createUser(CreateUserRequest request) {
        beforeCreateUser(request);
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AlreadyExistsException("User with email already exists");
            }
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPasswordHash(request.getPasswordHash());
            user.setIsActive(request.getIsActive());
            return userRepository.save(user);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCreateUser(request);
        }
    }

    @MethodMetadata(irId = "hook-before-create_user_svc_nkt", hash = "a77456ff", zone = 2)
    public void beforeCreateUser(CreateUserRequest request) {
    }

    @MethodMetadata(irId = "hook-after-create_user_svc_nkt", hash = "e85b5996", zone = 2)
    public void afterCreateUser(CreateUserRequest request) {
    }

    @MethodMetadata(irId = "get_user_by_id_svc_8rw", hash = "0d17722b", zone = 1)
    public User getUserById(String id) {
        beforeGetUserById(id);
        try {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetUserById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_user_by_id_svc_8rw", hash = "5f3ff602", zone = 2)
    public void beforeGetUserById(String id) {
    }

    @MethodMetadata(irId = "hook-after-get_user_by_id_svc_8rw", hash = "20272a4d", zone = 2)
    public void afterGetUserById(String id) {
    }

    @MethodMetadata(irId = "list_users_svc_dq0", hash = "ca7cc736", zone = 1)
    public List<User> listUsers() {
        beforeListUsers();
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw e;
        } finally {
            afterListUsers();
        }
    }

    @MethodMetadata(irId = "hook-before-list_users_svc_dq0", hash = "1cc83613", zone = 2)
    public void beforeListUsers() {
    }

    @MethodMetadata(irId = "hook-after-list_users_svc_dq0", hash = "b734b73a", zone = 2)
    public void afterListUsers() {
    }

    @MethodMetadata(irId = "update_user_svc_0hz", hash = "b73f636e", zone = 1)
    public User updateUser(String id, UpdateUserRequest request) {
        beforeUpdateUser(id, request);
        try {
            User existing = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
            if (request.getEmail() != null && !request.getEmail().equals(existing.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new AlreadyExistsException("User with email already exists");
            }
            if (request.getEmail() != null) {
                existing.setEmail(request.getEmail());
            }
            if (request.getPasswordHash() != null) {
                existing.setPasswordHash(request.getPasswordHash());
            }
            return userRepository.save(existing);
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateUser(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_user_svc_0hz", hash = "036002a6", zone = 2)
    public void beforeUpdateUser(String id, UpdateUserRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_user_svc_0hz", hash = "84465cd3", zone = 2)
    public void afterUpdateUser(String id, UpdateUserRequest request) {
    }

    @MethodMetadata(irId = "delete_user_svc_yrj", hash = "57387f77", zone = 1)
    public void deleteUser(String id) {
        beforeDeleteUser(id);
        try {
            userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteUser(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_user_svc_yrj", hash = "5ef88533", zone = 2)
    public void beforeDeleteUser(String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_user_svc_yrj", hash = "7f8f45f2", zone = 2)
    public void afterDeleteUser(String id) {
    }
}
