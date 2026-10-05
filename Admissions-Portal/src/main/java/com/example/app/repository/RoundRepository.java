package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository()
public interface RoundRepository extends JpaRepository<Round, String> {

    // repo_method_id: findMaxRoundNumberByDriveId_z3c
    @MethodMetadata(irId = "findMaxRoundNumberByDriveId_z3c", hash = "963e7f47", zone = 1)
    @Query("SELECT MAX(r.roundNumber) FROM Round r WHERE r.driveId = :driveId")
    public Integer findMaxRoundNumberByDriveId(String driveId);
}
