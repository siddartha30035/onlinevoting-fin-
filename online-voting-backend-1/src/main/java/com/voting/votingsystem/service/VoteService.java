package com.voting.votingsystem.service;

import com.voting.votingsystem.model.*;
import com.voting.votingsystem.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;
    private final VoterRepository voterRepository;

    public VoteService(
            VoteRepository voteRepository,
            CandidateRepository candidateRepository,
            ElectionRepository electionRepository,
            VoterRepository voterRepository
    ) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
        this.voterRepository = voterRepository;
    }

    // Cast a vote with validation
    public Vote castVote(Long candidateId, Long electionId, Long voterId) {
        // Validate entities exist
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + candidateId));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found with ID: " + electionId));

        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found with ID: " + voterId));

        // Note: We do NOT enforce election ACTIVE status/time here per requirements.
        // Time/status are kept for reference only; voting is allowed as long as the election exists.

        // Check if voter has already voted in this election
        Optional<Vote> existingVote = voteRepository.findByVoterAndElection(voter, election);
        if (existingVote.isPresent()) {
            throw new RuntimeException("Voter has already voted in this election");
        }

        // Create and save the vote
        Vote vote = new Vote(voter, candidate, election);
        Vote savedVote = voteRepository.save(vote);

        // Update candidate vote count
        candidate.setVotes(candidate.getVotes() + 1);
        candidateRepository.save(candidate);

        return savedVote;
    }

    // Get all votes for a specific election
    public List<Vote> getVotesByElection(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));
        return voteRepository.findByElection(election);
    }

    // Get comprehensive election results (includes candidates with zero votes)
    public Map<String, Object> getElectionResults(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));

        // Fetch all votes for this election
        List<Vote> votes = voteRepository.findByElection(election);

        // Tally votes per candidate
        Map<Long, Long> voteCountsByCandidateId = votes.stream()
                .collect(Collectors.groupingBy(v -> v.getCandidate().getId(), Collectors.counting()));

        // Fetch all candidates participating in this election
        List<Candidate> electionCandidates = candidateRepository.findByElections_Id(electionId);

        // Build candidate results including zero-vote candidates
        List<Map<String, Object>> candidateResults = electionCandidates.stream()
                .map(candidate -> {
                    long voteCount = voteCountsByCandidateId.getOrDefault(candidate.getId(), 0L);
                    Map<String, Object> candidateResult = new HashMap<>();
                    candidateResult.put("candidateId", candidate.getId());
                    candidateResult.put("candidateName", candidate.getName());
                    candidateResult.put("party", candidate.getParty());
                    candidateResult.put("voteCount", voteCount);
                    return candidateResult;
                })
                .sorted((a, b) -> Long.compare((Long) b.get("voteCount"), (Long) a.get("voteCount")))
                .collect(Collectors.toList());

        // Determine winner (may be null if no candidates)
        Map<String, Object> winner = candidateResults.isEmpty() ? null : candidateResults.get(0);

        Map<String, Object> results = new HashMap<>();
        results.put("electionId", electionId);
        results.put("electionName", election.getName());
        results.put("totalVotes", votes.size());
        results.put("candidateResults", candidateResults);
        results.put("winner", winner);

        return results;
    }

    // Check if a voter has already voted in an election
    public boolean hasVoterVoted(Long voterId, Long electionId) {
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found"));
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));
        
        return voteRepository.findByVoterAndElection(voter, election).isPresent();
    }

    // Get total vote count for an election
    public long getTotalVotesByElection(Long electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));
        return voteRepository.countByElection(election);
    }

    // Get all votes (admin function)
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    // Delete a vote (admin function - use carefully)
    public void deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("Vote not found"));
        
        // Decrease candidate vote count
        Candidate candidate = vote.getCandidate();
        if (candidate.getVotes() > 0) {
            candidate.setVotes(candidate.getVotes() - 1);
            candidateRepository.save(candidate);
        }
        
        voteRepository.delete(vote);
    }
}
