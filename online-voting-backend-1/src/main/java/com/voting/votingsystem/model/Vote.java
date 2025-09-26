package com.voting.votingsystem.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "votes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"voter_id", "election_id"})
    }
)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    public Vote() {}

    public Vote(Voter voter, Candidate candidate, Election election) {
        this.voter = voter;
        this.candidate = candidate;
        this.election = election;
    }

    // Getters & setters
    public Long getId() { return id; }
    public Voter getVoter() { return voter; }
    public void setVoter(Voter voter) { this.voter = voter; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
    public Election getElection() { return election; }
    public void setElection(Election election) { this.election = election; }
}
