package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationExamScoreRepository extends JpaRepository<ApplicationExamScore, String> {

    // repo_method_id: examScore findByApplicationId_ebs
    @MethodMetadata(irId = "examScore findByApplicationId_ebs", hash = "f44e9c12", zone = 1)
    public List<ApplicationExamScore> findByApplicationId(String applicationId);

    // repo_method_id: findByApplicationIdAndExamType_otg | Retrieves GATE score for a candidate.
    @MethodMetadata(irId = "findByApplicationIdAndExamType_otg", hash = "8ee0aad9", zone = 1)
    public Optional<ApplicationExamScore> findByApplicationIdAndExamType(String applicationId, String examType);
}
