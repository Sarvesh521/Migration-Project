package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface QueryRepository extends JpaRepository<Query, String> {

    // repo_method_id: query findByApplicationId_of2
    @MethodMetadata(irId = "query findByApplicationId_of2", hash = "dff238d9", zone = 1)
    public List<Query> findByApplicationId(String applicationId);
}
