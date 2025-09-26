package com.voting.votingsystem.service;

import com.voting.votingsystem.model.Voter;
import com.voting.votingsystem.repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    // Existing registration method
    public Voter registerVoter(Voter voter) {
        // your existing registration logic
        return voterRepository.save(voter);
    }

    // ✅ Get all voters
    public List<Voter> getAllVoters() {
        return voterRepository.findAll();
    }

    // ✅ Delete voter by ID
    public void deleteVoterById(Long id) throws Exception {
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new Exception("Voter not found"));
        voterRepository.delete(voter);
    }
}
