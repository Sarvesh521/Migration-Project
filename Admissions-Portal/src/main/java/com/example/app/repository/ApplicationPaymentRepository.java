package com.example.app.repository;

import com.example.app.annotation.MethodMetadata;
import com.example.app.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface ApplicationPaymentRepository extends JpaRepository<ApplicationPayment, String> {

    // repo_method_id: findPaymentByApplicationAndStatus_tx3
    @MethodMetadata(irId = "findPaymentByApplicationAndStatus_tx3", hash = "f0764dd4", zone = 1)
    public List<ApplicationPayment> findByApplicationIdAndPaymentStatus(String applicationId, String paymentStatus);

    // repo_method_id: payment findByApplicationId_50u
    @MethodMetadata(irId = "payment findByApplicationId_50u", hash = "c4af519f", zone = 1)
    public List<ApplicationPayment> findByApplicationId(String applicationId);
}
