package com.example.app.controller;

import com.example.app.annotation.MethodMetadata;
import com.example.app.dto.*;
import com.example.app.entity.*;
import com.example.app.service.RoundService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/rounds")
public class RoundController {

    @Autowired()
    private RoundService roundService;

    @MethodMetadata(irId = "get_round_by_id_ctrl_9t5", hash = "a484cb65", zone = 1)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Round> getRoundById(@PathVariable String id) {
        beforeGetRoundById(id);
        try {
            return ResponseEntity.ok(roundService.getRoundById(id));
        } catch (Exception e) {
            throw e;
        } finally {
            afterGetRoundById(id);
        }
    }

    @MethodMetadata(irId = "hook-before-get_round_by_id_ctrl_9t5", hash = "f99c21ad", zone = 2)
    public void beforeGetRoundById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-get_round_by_id_ctrl_9t5", hash = "9dc07482", zone = 2)
    public void afterGetRoundById(@PathVariable String id) {
    }

    @MethodMetadata(irId = "list_rounds_ctrl_6c4", hash = "91e91909", zone = 1)
    @GetMapping("")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<List<Round>> listRounds() {
        beforeListRounds();
        try {
            return ResponseEntity.ok(roundService.listRounds());
        } catch (Exception e) {
            throw e;
        } finally {
            afterListRounds();
        }
    }

    @MethodMetadata(irId = "hook-before-list_rounds_ctrl_6c4", hash = "e3e7cdee", zone = 2)
    public void beforeListRounds() {
    }

    @MethodMetadata(irId = "hook-after-list_rounds_ctrl_6c4", hash = "438aebe8", zone = 2)
    public void afterListRounds() {
    }

    @MethodMetadata(irId = "update_round_ctrl_qgf", hash = "925e71ec", zone = 1)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Round> updateRound(@PathVariable String id, @RequestParam(required = false) UpdateRoundRequest request) {
        beforeUpdateRound(id, request);
        try {
            return ResponseEntity.ok(roundService.updateRound(id, request));
        } catch (Exception e) {
            throw e;
        } finally {
            afterUpdateRound(id, request);
        }
    }

    @MethodMetadata(irId = "hook-before-update_round_ctrl_qgf", hash = "78ea483d", zone = 2)
    public void beforeUpdateRound(@PathVariable String id, @RequestParam(required = false) UpdateRoundRequest request) {
    }

    @MethodMetadata(irId = "hook-after-update_round_ctrl_qgf", hash = "22585ca5", zone = 2)
    public void afterUpdateRound(@PathVariable String id, @RequestParam(required = false) UpdateRoundRequest request) {
    }

    @MethodMetadata(irId = "delete_round_ctrl_vui", hash = "21f1689c", zone = 1)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('APPLICANT','ADMIN')")
    public ResponseEntity<Void> deleteRound(@PathVariable String id) {
        beforeDeleteRound(id);
        try {
            roundService.deleteRound(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw e;
        } finally {
            afterDeleteRound(id);
        }
    }

    @MethodMetadata(irId = "hook-before-delete_round_ctrl_vui", hash = "92828679", zone = 2)
    public void beforeDeleteRound(@PathVariable String id) {
    }

    @MethodMetadata(irId = "hook-after-delete_round_ctrl_vui", hash = "0441b33f", zone = 2)
    public void afterDeleteRound(@PathVariable String id) {
    }

    /*
 * Operation    : Create Admission Round with Validation
 * Usecase ID   : UC-11
 * Usecase Name : Create Admission Round
 */
    @MethodMetadata(irId = "createRound_3bx", hash = "bb75f3ed", zone = 1)
    @PostMapping(value = "/")
    public ResponseEntity<String> createRound(@RequestBody RoundRequest request, Principal principal) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(roundService.createRound(request, principal));
    }
}
