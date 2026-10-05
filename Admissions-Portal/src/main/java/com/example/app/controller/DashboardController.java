package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.DashboardService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "/dashboards")
public class DashboardController {

    @Autowired()
    private DashboardService dashboardService;

    /*
 * Operation    : View Dashboard Statistics
 * Usecase ID   : UC-20
 * Usecase Name : View Dashboard Statistics
 * Comment      : Public read-only endpoint for dashboard statistics; no principal required as it's admin-only and not user-scoped.
 */
    @MethodMetadata(irId = "getDashboardStatistics_gzs", hash = "1cc83c30", zone = 1)
    @GetMapping(value = "/statistics")
    public ResponseEntity<DashboardStatisticsResponse> getDashboardStatistics() {
        return ResponseEntity.ok(dashboardService.getDashboardStatistics());
    }
}
