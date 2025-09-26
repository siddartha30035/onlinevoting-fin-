package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.Vote;
import com.voting.votingsystem.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "http://localhost:5173")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    // Cast a vote
    @PostMapping
    public ResponseEntity<?> castVote(@RequestBody Map<String, Long> voteRequest) {
        try {
            Long candidateId = voteRequest.get("candidateId");
            Long electionId = voteRequest.get("electionId");
            Long voterId = voteRequest.get("voterId");

            if (candidateId == null || electionId == null || voterId == null) {
                return ResponseEntity.badRequest().body("Missing required fields: candidateId, electionId, voterId");
            }

            Vote vote = voteService.castVote(candidateId, electionId, voterId);
            return ResponseEntity.ok(Map.of(
                "message", "Vote cast successfully",
                "voteId", vote.getId(),
                "candidateId", candidateId,
                "electionId", electionId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get all votes for an election
    @GetMapping("/election/{electionId}")
    public ResponseEntity<List<Vote>> getVotesByElection(@PathVariable Long electionId) {
        try {
            List<Vote> votes = voteService.getVotesByElection(electionId);
            return ResponseEntity.ok(votes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get vote count by candidate for an election
    @GetMapping("/results/election/{electionId}")
    public ResponseEntity<Map<String, Object>> getElectionResults(@PathVariable Long electionId) {
        try {
            Map<String, Object> results = voteService.getElectionResults(electionId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Check if voter has already voted in an election
    @GetMapping("/check/{voterId}/election/{electionId}")
    public ResponseEntity<Map<String, Boolean>> hasVoterVoted(
            @PathVariable Long voterId, 
            @PathVariable Long electionId) {
        try {
            boolean hasVoted = voteService.hasVoterVoted(voterId, electionId);
            return ResponseEntity.ok(Map.of("hasVoted", hasVoted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get total vote count for an election
    @GetMapping("/count/election/{electionId}")
    public ResponseEntity<Map<String, Long>> getTotalVotes(@PathVariable Long electionId) {
        try {
            long totalVotes = voteService.getTotalVotesByElection(electionId);
            return ResponseEntity.ok(Map.of("totalVotes", totalVotes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
