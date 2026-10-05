package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationRoundRepository extends JpaRepository<ApplicationRound, String> {

    // repo_method_id: round findByApplicationId_0pu
    @MethodMetadata(irId = "round findByApplicationId_0pu", hash = "ae1d9d69", zone = 1)
    public List<ApplicationRound> findByApplicationId(String applicationId);

    // repo_method_id: findByRoundIdAndOfferStatusSliding_g4k
    @MethodMetadata(irId = "findByRoundIdAndOfferStatusSliding_g4k", hash = "5600d4be", zone = 1)
    @Query("SELECT ar FROM ApplicationRound ar WHERE ar.roundId = :roundId AND ar.offerStatus = :offerStatus")
    public List<ApplicationRound> findByRoundIdAndOfferStatusSliding(String roundId, String offerStatus);

    // repo_method_id: findByApplicationIdInAndRoundId_xq9
    @MethodMetadata(irId = "findByApplicationIdInAndRoundId_xq9", hash = "4a9bcc60", zone = 1)
    @Query(value = "SELECT e FROM ApplicationRound e WHERE e.applicationId IN :applicationIds AND e.roundId = :roundId", nativeQuery = false)
    public List<ApplicationRound> findByApplicationIdInAndRoundId(List<String> applicationIds, String roundId);

    // repo_method_id: findByApplicationIdAndRoundId_applicationRound_3d4 | Derived query to find ApplicationRound by application and round IDs
    @MethodMetadata(irId = "findByApplicationIdAndRoundId_applicationRound_3d4", hash = "08662c2e", zone = 1)
    public Optional<ApplicationRound> findByApplicationIdAndRoundId(String applicationId, String roundId);
}
