import React, { useEffect, useState } from "react";
import "../styles/Results.css";
import { getAllElections } from "../services/electionService";
import { getElectionResults } from "../services/voteService";

const Results = () => {
  const [elections, setElections] = useState([]);
  const [selectedElectionId, setSelectedElectionId] = useState(null);
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchElections = async () => {
      try {
        const data = await getAllElections();
        setElections(Array.isArray(data) ? data : []);
        if (Array.isArray(data) && data.length > 0) {
          setSelectedElectionId(data[0].id);
        }
      } catch (e) {
        setError("Failed to load elections");
      }
    };
    fetchElections();
  }, []);

  useEffect(() => {
    const fetchResults = async () => {
      if (!selectedElectionId) return;
      setLoading(true);
      setError("");
      try {
        const data = await getElectionResults(selectedElectionId);
        setResults(data);
      } catch (e) {
        setError("Failed to load results");
      } finally {
        setLoading(false);
      }
    };
    fetchResults();
  }, [selectedElectionId]);

  const candidateResults = results?.candidateResults || [];
  const winner = results?.winner || null;

  return (
    <div className="results-container">
      <h1>Election Results</h1>

      <div className="results-controls">
        <label htmlFor="election-select">Select Election:</label>
        <select
          id="election-select"
          value={selectedElectionId || ""}
          onChange={(e) => setSelectedElectionId(parseInt(e.target.value))}
        >
          {elections.map((e) => (
            <option key={e.id} value={e.id}>
              {e.name} — {e.date}
            </option>
          ))}
        </select>
      </div>

      {error && <p className="no-results">{error}</p>}
      {loading && <p>Loading results...</p>}

      {!loading && candidateResults.length === 0 && (
        <p className="no-results">No results available</p>
      )}

      {!loading && candidateResults.length > 0 && (
        <>
          <table className="results-table">
            <thead>
              <tr>
                <th>Candidate</th>
                <th>Party</th>
                <th>Votes</th>
              </tr>
            </thead>
            <tbody>
              {candidateResults.map((c) => (
                <tr
                  key={c.candidateId}
                  className={winner && winner.candidateId === c.candidateId ? "winner-row" : ""}
                >
                  <td>{c.candidateName}</td>
                  <td>{c.party}</td>
                  <td>{c.voteCount}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {winner && (
            <h2 className="winner-message">
              🏆 Winner: {winner.candidateName} ({winner.party}) — {winner.voteCount} votes
            </h2>
          )}
        </>
      )}
    </div>
  );
};

export default Results;
