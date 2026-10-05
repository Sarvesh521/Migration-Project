package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationEducationRepository extends JpaRepository<ApplicationEducation, String> {

    // repo_method_id: education findByApplicationId_6p0
    @MethodMetadata(irId = "education findByApplicationId_6p0", hash = "643fbb66", zone = 1)
    public List<ApplicationEducation> findByApplicationId(String applicationId);
}
