package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.Voter;
import com.voting.votingsystem.repository.VoterRepository;
import com.voting.votingsystem.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Replace with your frontend URL
public class AuthController {

    @Autowired
    private VoterRepository voterRepository;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<Voter> voterOpt = voterRepository.findByEmail(loginRequest.getEmail());

        if (voterOpt.isPresent()) {
            Voter voter = voterOpt.get();
            if (voter.getPassword().equals(loginRequest.getPassword())) {
                // Successful login -> return JSON with voter details (including numeric DB id)
                Map<String, Object> body = new HashMap<>();
                body.put("id", voter.getId()); // numeric ID used for voting
                body.put("voterCode", voter.getVoterId()); // human-readable voter code
                body.put("name", voter.getName());
                body.put("email", voter.getEmail());
                body.put("message", "Login successful. Welcome " + voter.getName());
                return ResponseEntity.ok(body);
            } else {
                return ResponseEntity.status(401).body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
