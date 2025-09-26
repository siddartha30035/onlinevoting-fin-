package com.voting.votingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.voting.votingsystem.model.Voter;
import com.voting.votingsystem.service.VoterService;

import java.util.List;

@RestController
@RequestMapping("/api/voters")
@CrossOrigin(origins = "http://localhost:5173")
public class VoterController {

    @Autowired
    private VoterService voterService;

    // ✅ Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerVoter(@RequestBody Voter voter) {
        try {
            Voter savedVoter = voterService.registerVoter(voter);
            return ResponseEntity.ok(savedVoter);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Get all voters
    @GetMapping
    public ResponseEntity<List<Voter>> getAllVoters() {
        return ResponseEntity.ok(voterService.getAllVoters());
    }

    // ✅ Delete voter by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoter(@PathVariable Long id) {
        try {
            voterService.deleteVoterById(id);
            return ResponseEntity.ok("Voter deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
