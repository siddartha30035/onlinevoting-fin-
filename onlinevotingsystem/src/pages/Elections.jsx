// src/pages/Elections.jsx
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Elections.css";
import { getAllElections } from "../services/electionService";
import { getCandidatesByElection } from "../services/candidateService";
import { castVote, hasVoted } from "../services/voteService";

const Elections = () => {
  const [elections, setElections] = useState([]);
  const [candidates, setCandidates] = useState([]);
  const [selectedElection, setSelectedElection] = useState(null);
  const [selectedCandidateId, setSelectedCandidateId] = useState(null);
  const [voterId, setVoterId] = useState(() => {
    const s = localStorage.getItem("voterId");
    return s ? parseInt(s) : null;
  });
  const [voterName, setVoterName] = useState(() => localStorage.getItem("voterName") || "");
  const [loading, setLoading] = useState(false);
  const [alreadyVoted, setAlreadyVoted] = useState(false);
  const isLoggedIn = typeof voterId === "number" && !Number.isNaN(voterId);
  const navigate = useNavigate();

  // Keep a sync with localStorage if it changes elsewhere
  useEffect(() => {
    const storedVoterId = localStorage.getItem("voterId");
    const storedVoterName = localStorage.getItem("voterName");
    setVoterId(storedVoterId ? parseInt(storedVoterId) : null);
    setVoterName(storedVoterName || "");
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("voterId");
    localStorage.removeItem("voterName");
    localStorage.removeItem("voterEmail");
    navigate("/");
  };

  // Fetch all elections initially
  useEffect(() => {
    const fetchData = async () => {
      try {
        const electionsData = await getAllElections();
        setElections(Array.isArray(electionsData) ? electionsData : []);
      } catch (err) {
        console.error("Error fetching elections:", err);
      }
    };
    fetchData();
  }, []);

  // ✅ Fetch candidates only when an election is selected
  const handleSelectElection = async (election) => {
    setSelectedElection(election);
    setSelectedCandidateId(null);
    setAlreadyVoted(false);
    try {
      const candidatesData = await getCandidatesByElection(election.id);
      setCandidates(Array.isArray(candidatesData) ? candidatesData : []);
      if (isLoggedIn) {
        const voted = await hasVoted(voterId, election.id);
        setAlreadyVoted(voted);
      }
    } catch (err) {
      console.error("Error fetching candidates:", err);
    }
  };

  const handleVote = async () => {
    if (!selectedCandidateId) {
      alert("⚠️ Please select a candidate!");
      return;
    }

    if (!isLoggedIn || !selectedElection) {
      alert("⚠️ Please login first and select an election!");
      return;
    }

    try {
      setLoading(true);
      // Check again before casting vote
      const voted = await hasVoted(voterId, selectedElection.id);
      if (voted) {
        setAlreadyVoted(true);
        alert("You have already voted in this election.");
        setLoading(false);
        return;
      }

      await castVote({
        candidateId: selectedCandidateId,
        electionId: selectedElection.id,
        voterId,
      });
      alert(
        `✅ Thank you for voting for candidate ID: ${selectedCandidateId} in election: ${selectedElection.name}`
      );
      setSelectedElection(null);
      setSelectedCandidateId(null);
      setCandidates([]);
    } catch (err) {
      console.error("Error voting:", err);
      const msg = err.response?.data?.error || err.response?.data?.message || "❌ Failed to submit vote. Try again.";
      alert(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="elections-container">
      <h1>Available Elections</h1>
      {isLoggedIn && (
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12 }}>
          <p style={{ margin: 0 }}>Logged in as <strong>{voterName || "Voter"}</strong></p>
          <button onClick={handleLogout}>Logout</button>
        </div>
      )}

      {!selectedElection ? (
        <ul>
          {elections.length === 0 ? (
            <p>No elections available</p>
          ) : (
            elections.map((e) => (
              <li key={e.id}>
                <span>
                  {e.name} — {e.date}
                </span>
                <button onClick={() => handleSelectElection(e)}>Vote</button>
              </li>
            ))
          )}
        </ul>
      ) : (
        <div className="vote-container">
          <h2>Vote in {selectedElection.name}</h2>
          {alreadyVoted && (
            <p style={{ color: "#b91c1c", marginBottom: 8 }}>
              You have already voted in this election.
            </p>
          )}
          {candidates.length === 0 ? (
            <p>No candidates available for this election</p>
          ) : (
            <form
              onSubmit={(e) => {
                e.preventDefault();
                if (!alreadyVoted && !loading) handleVote();
              }}
            >
              {candidates.map((c) => (
                <div key={c.id}>
                  <input
                    type="radio"
                    id={`c${c.id}`}
                    name="candidate"
                    value={c.id}
                    checked={selectedCandidateId === c.id}
                    onChange={() => setSelectedCandidateId(c.id)}
                  />
                  <label htmlFor={`c${c.id}`}>
                    {c.name} ({c.party})
                  </label>
                </div>
              ))}
              <button type="submit" disabled={alreadyVoted || loading || !isLoggedIn}>
                {loading ? "Submitting..." : alreadyVoted ? "Already Voted" : "Submit Vote"}
              </button>
              <button type="button" onClick={() => setSelectedElection(null)}>
                Back
              </button>
            </form>
          )}
        </div>
      )}
    </div>
  );
};

export default Elections;
