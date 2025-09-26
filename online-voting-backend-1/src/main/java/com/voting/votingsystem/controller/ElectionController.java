package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.Election;
import com.voting.votingsystem.service.ElectionService;
import com.voting.votingsystem.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elections")
@CrossOrigin(origins = "http://localhost:5173") // adjust to your React dev server port
public class ElectionController {

    private final ElectionService electionService;
    private final VoteService voteService;

    public ElectionController(ElectionService electionService, VoteService voteService) {
        this.electionService = electionService;
        this.voteService = voteService;
    }

    // Get all elections
    @GetMapping
    public ResponseEntity<List<Election>> getAllElections() {
        return ResponseEntity.ok(electionService.getAllElections());
    }

    // Get election by ID
    @GetMapping("/{id}")
    public ResponseEntity<Election> getElectionById(@PathVariable Long id) {
        try {
            Election election = electionService.getElectionById(id);
            return ResponseEntity.ok(election);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get only active elections
    @GetMapping("/active")
    public ResponseEntity<List<Election>> getActiveElections() {
        return ResponseEntity.ok(electionService.getActiveElections());
    }

    // Add election
    @PostMapping
    public ResponseEntity<Election> addElection(@RequestBody Election election) {
        return ResponseEntity.ok(electionService.addElection(election));
    }

    // Update election
    @PutMapping("/{id}")
    public ResponseEntity<Election> updateElection(@PathVariable Long id, @RequestBody Election election) {
        try {
            Election updatedElection = electionService.updateElection(id, election);
            return ResponseEntity.ok(updatedElection);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update election status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Election> updateElectionStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> statusUpdate) {
        try {
            String status = statusUpdate.get("status");
            Election updatedElection = electionService.updateElectionStatus(id, status);
            return ResponseEntity.ok(updatedElection);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Start election (set status to ACTIVE)
    @PostMapping("/{id}/start")
    public ResponseEntity<Election> startElection(@PathVariable Long id) {
        try {
            Election election = electionService.startElection(id);
            return ResponseEntity.ok(election);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // End election (set status to COMPLETED)
    @PostMapping("/{id}/end")
    public ResponseEntity<Election> endElection(@PathVariable Long id) {
        try {
            Election election = electionService.endElection(id);
            return ResponseEntity.ok(election);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get election results
    @GetMapping("/{id}/results")
    public ResponseEntity<Map<String, Object>> getElectionResults(@PathVariable Long id) {
        try {
            Map<String, Object> results = voteService.getElectionResults(id);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete election
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
        try {
            electionService.deleteElection(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
