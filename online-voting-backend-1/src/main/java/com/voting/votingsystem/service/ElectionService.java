package com.voting.votingsystem.service;

import com.voting.votingsystem.model.Election;
import com.voting.votingsystem.repository.ElectionRepository;
import com.voting.votingsystem.model.Candidate;
import com.voting.votingsystem.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public ElectionService(ElectionRepository electionRepository, CandidateRepository candidateRepository) {
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
    }

    // Get all elections
    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    // Get election by ID
    public Election getElectionById(Long id) {
        return electionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Election not found with ID: " + id));
    }

    // Get only active elections
    public List<Election> getActiveElections() {
        return electionRepository.findAll().stream()
                .filter(election -> election.getStatus() == Election.ElectionStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    // Get elections by status
    public List<Election> getElectionsByStatus(Election.ElectionStatus status) {
        return electionRepository.findAll().stream()
                .filter(election -> election.getStatus() == status)
                .collect(Collectors.toList());
    }

    // Add new election and associate ALL existing candidates with it
    public Election addElection(Election election) {
        // Set default status if not provided
        if (election.getStatus() == null) {
            election.setStatus(Election.ElectionStatus.INACTIVE);
        }
        // Save election first to get its ID
        Election savedElection = electionRepository.save(election);

        // Associate all existing candidates with this election (owning side is Candidate)
        List<Candidate> allCandidates = candidateRepository.findAll();
        for (Candidate c : allCandidates) {
            c.getElections().add(savedElection);
        }
        if (!allCandidates.isEmpty()) {
            candidateRepository.saveAll(allCandidates);
        }

        return savedElection;
    }

    // Update election
    public Election updateElection(Long id, Election updatedElection) {
        Election existingElection = getElectionById(id);
        
        // Update fields
        existingElection.setName(updatedElection.getName());
        existingElection.setDate(updatedElection.getDate());
        existingElection.setDescription(updatedElection.getDescription());
        
        if (updatedElection.getStartTime() != null) {
            existingElection.setStartTime(updatedElection.getStartTime());
        }
        if (updatedElection.getEndTime() != null) {
            existingElection.setEndTime(updatedElection.getEndTime());
        }
        if (updatedElection.getStatus() != null) {
            existingElection.setStatus(updatedElection.getStatus());
        }
        
        return electionRepository.save(existingElection);
    }

    // Update election status
    public Election updateElectionStatus(Long id, String status) {
        Election election = getElectionById(id);
        
        try {
            Election.ElectionStatus newStatus = Election.ElectionStatus.valueOf(status.toUpperCase());
            election.setStatus(newStatus);
            
            // Set timestamps based on status
            if (newStatus == Election.ElectionStatus.ACTIVE && election.getStartTime() == null) {
                election.setStartTime(LocalDateTime.now());
            } else if (newStatus == Election.ElectionStatus.COMPLETED && election.getEndTime() == null) {
                election.setEndTime(LocalDateTime.now());
            }
            
            return electionRepository.save(election);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid election status: " + status);
        }
    }

    // Start election
    public Election startElection(Long id) {
        Election election = getElectionById(id);
        
        // Validate election can be started
        if (election.getStatus() == Election.ElectionStatus.COMPLETED) {
            throw new RuntimeException("Cannot start a completed election");
        }
        if (election.getStatus() == Election.ElectionStatus.CANCELLED) {
            throw new RuntimeException("Cannot start a cancelled election");
        }
        
        election.setStatus(Election.ElectionStatus.ACTIVE);
        election.setStartTime(LocalDateTime.now());
        
        return electionRepository.save(election);
    }

    // End election
    public Election endElection(Long id) {
        Election election = getElectionById(id);
        
        // Validate election can be ended
        if (election.getStatus() != Election.ElectionStatus.ACTIVE) {
            throw new RuntimeException("Only active elections can be ended");
        }
        
        election.setStatus(Election.ElectionStatus.COMPLETED);
        election.setEndTime(LocalDateTime.now());
        
        return electionRepository.save(election);
    }

    // Cancel election
    public Election cancelElection(Long id) {
        Election election = getElectionById(id);
        
        // Validate election can be cancelled
        if (election.getStatus() == Election.ElectionStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed election");
        }
        
        election.setStatus(Election.ElectionStatus.CANCELLED);
        
        return electionRepository.save(election);
    }

    // Delete election
    public void deleteElection(Long id) {
        Election election = getElectionById(id);
        
        // Prevent deletion of active elections with votes
        if (election.getStatus() == Election.ElectionStatus.ACTIVE) {
            throw new RuntimeException("Cannot delete an active election");
        }
        
        electionRepository.deleteById(id);
    }

    // Check if election is currently accepting votes
    public boolean isElectionVotingActive(Long id) {
        Election election = getElectionById(id);
        return election.canVote();
    }
}
