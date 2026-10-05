package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationPreferenceRepository extends JpaRepository<ApplicationPreference, String> {

    // repo_method_id: preference findByApplicationId_lem
    @MethodMetadata(irId = "preference findByApplicationId_lem", hash = "cbfa436a", zone = 1)
    public List<ApplicationPreference> findByApplicationId(String applicationId);

    // repo_method_id: findByApplicationIdOrderByPreferenceOrderAsc_rbs | Retrieves candidate's programme preferences in order.
    @MethodMetadata(irId = "findByApplicationIdOrderByPreferenceOrderAsc_rbs", hash = "02d5008c", zone = 1)
    public List<ApplicationPreference> findByApplicationIdOrderByPreferenceOrderAsc(String applicationId);
}
