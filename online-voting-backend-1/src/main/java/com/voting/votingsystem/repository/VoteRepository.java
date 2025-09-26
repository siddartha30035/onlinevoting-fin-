package com.voting.votingsystem.repository;

import com.voting.votingsystem.model.Vote;
import com.voting.votingsystem.model.Voter;
import com.voting.votingsystem.model.Election;
import com.voting.votingsystem.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    
    // Find vote by voter and election (to prevent duplicate voting)
    Optional<Vote> findByVoterAndElection(Voter voter, Election election);
    
    // Find all votes for a specific election
    List<Vote> findByElection(Election election);
    
    // Find all votes for a specific candidate
    List<Vote> findByCandidate(Candidate candidate);
    
    // Count total votes for an election
    long countByElection(Election election);
    
    // Count votes for a specific candidate in an election
    long countByCandidateAndElection(Candidate candidate, Election election);
    
    // Find all votes by a specific voter
    List<Vote> findByVoter(Voter voter);
    
    // Custom query to get vote counts by candidate for an election
    @Query("SELECT v.candidate, COUNT(v) FROM Vote v WHERE v.election = :election GROUP BY v.candidate")
    List<Object[]> getVoteCountsByElection(@Param("election") Election election);
    
    // Check if voter has voted in any election
    boolean existsByVoter(Voter voter);
    
    // Get total number of unique voters who have voted in an election
    @Query("SELECT COUNT(DISTINCT v.voter) FROM Vote v WHERE v.election = :election")
    long countDistinctVotersByElection(@Param("election") Election election);
}
