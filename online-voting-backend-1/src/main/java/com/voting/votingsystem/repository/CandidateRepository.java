package com.voting.votingsystem.repository;

import com.voting.votingsystem.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    // Fetch candidates associated with a specific election
    List<Candidate> findByElections_Id(Long electionId);
}
