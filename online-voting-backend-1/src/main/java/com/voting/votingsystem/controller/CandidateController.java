package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.Candidate;
import com.voting.votingsystem.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173") // React dev server
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    // ✅ Add candidate (automatically associated with all elections)
    @PostMapping
    public ResponseEntity<Candidate> addCandidate(@RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateService.addCandidate(candidate);
        return ResponseEntity.ok(savedCandidate);
    }

    // ✅ Get all candidates
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    // ✅ Get candidates for a specific election
    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<Candidate>> getCandidatesByElection(@PathVariable Long electionId) {
        return ResponseEntity.ok(candidateService.getCandidatesByElection(electionId));
    }

    // ✅ Remove candidate
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCandidate(@PathVariable Long id) {
        candidateService.removeCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Vote for candidate (expects JSON body { "electionId": 1, "voterId": 2 })
    @PostMapping("/{candidateId}/vote")
    public ResponseEntity<Candidate> voteForCandidate(
            @PathVariable Long candidateId,
            @RequestBody Map<String, Long> requestBody) {

        Long electionId = requestBody.get("electionId");
        Long voterId = requestBody.get("voterId");

        if (electionId == null || voterId == null) {
            return ResponseEntity.badRequest().build();
        }

        Candidate updatedCandidate = candidateService.voteForCandidate(candidateId, electionId, voterId);
        return ResponseEntity.ok(updatedCandidate);
    }
}
