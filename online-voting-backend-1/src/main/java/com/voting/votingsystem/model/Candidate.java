package com.voting.votingsystem.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String party;
    private int age;

    // Total votes (shared across all elections for now)
    private int votes = 0;

    // Many-to-Many relationship with Election
    @ManyToMany
    @JoinTable(
        name = "candidate_election",
        joinColumns = @JoinColumn(name = "candidate_id"),
        inverseJoinColumns = @JoinColumn(name = "election_id")
    )
    private Set<Election> elections = new HashSet<>();

    // ✅ Constructors
    public Candidate() {}

    public Candidate(String name, String party, int age) {
        this.name = name;
        this.party = party;
        this.age = age;
        this.votes = 0;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }

    public Set<Election> getElections() { return elections; }
    public void setElections(Set<Election> elections) { this.elections = elections; }

    // ✅ Helper method to increment votes
    public void incrementVotes() {
        this.votes++;
    }
}
