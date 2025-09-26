package com.voting.votingsystem.service;

import com.voting.votingsystem.model.*;
import com.voting.votingsystem.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;
    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository; // assuming you have this

    public CandidateService(
            CandidateRepository candidateRepository,
            ElectionRepository electionRepository,
            VoteRepository voteRepository,
            VoterRepository voterRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
        this.voteRepository = voteRepository;
        this.voterRepository = voterRepository;
    }

    // Add candidate and automatically associate with all existing elections
    public Candidate addCandidate(Candidate candidate) {
        List<Election> elections = electionRepository.findAll();
        candidate.setElections(new HashSet<>(elections)); // link candidate to ALL elections
        return candidateRepository.save(candidate);
    }

    // Get all candidates
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Get candidates for a specific election
    public List<Candidate> getCandidatesByElection(Long electionId) {
        return candidateRepository.findByElections_Id(electionId);
    }

    // Remove candidate
    public void removeCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    // ✅ Cast vote with "one vote per voter per election"
    public Candidate voteForCandidate(Long candidateId, Long electionId, Long voterId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));

        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Voter not found"));

        // Check if this voter has already voted in this election
        voteRepository.findByVoterAndElection(voter, election).ifPresent(v -> {
            throw new RuntimeException("Voter has already voted in this election");
        });

        // Save the vote
        Vote vote = new Vote();
        vote.setCandidate(candidate);
        vote.setElection(election);
        vote.setVoter(voter);
        voteRepository.save(vote);

        // Increment candidate votes
        candidate.setVotes(candidate.getVotes() + 1);
        return candidateRepository.save(candidate);
    }
}
