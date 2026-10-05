package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, String> {

    // repo_method_id: document findByApplicationId_14y
    @MethodMetadata(irId = "document findByApplicationId_14y", hash = "58f8fba5", zone = 1)
    public List<ApplicationDocument> findByApplicationId(String applicationId);
}
