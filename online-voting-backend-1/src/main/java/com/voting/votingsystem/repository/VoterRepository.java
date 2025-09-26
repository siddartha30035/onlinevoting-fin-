package com.voting.votingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.voting.votingsystem.model.Voter;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Long> {
    boolean existsByVoterId(String voterId);
    boolean existsByEmail(String email);
    
    Optional<Voter> findByEmail(String email); // Add this for login
}
