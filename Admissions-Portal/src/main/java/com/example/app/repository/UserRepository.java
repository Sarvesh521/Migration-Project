package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface UserRepository extends JpaRepository<User, String> {

    @MethodMetadata(irId = "u_exists_by_email_bia", hash = "3880e5d4", zone = 1)
    public boolean existsByEmail(String email);
}
