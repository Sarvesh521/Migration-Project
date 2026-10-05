package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationOfferHistoryRepository extends JpaRepository<ApplicationOfferHistory, String> {

    // repo_method_id: offerHistory findByApplicationId_wk7
    @MethodMetadata(irId = "offerHistory findByApplicationId_wk7", hash = "e50117bf", zone = 1)
    public List<ApplicationOfferHistory> findByApplicationId(String applicationId);
}
