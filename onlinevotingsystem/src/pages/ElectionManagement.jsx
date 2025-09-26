import React, { useState, useEffect } from "react";
import "../styles/ElectionManagement.css";
import { getAllElections, addElectionApi, deleteElectionApi } from "../services/electionService";

function ElectionManagement() {
  const [elections, setElections] = useState([]);
  const [electionName, setElectionName] = useState("");
  const [electionDate, setElectionDate] = useState("");

  // Fetch from backend
  const fetchElections = async () => {
    try {
      const data = await getAllElections();
      setElections(data);
    } catch (error) {
      console.error("Error fetching elections:", error);
    }
  };

  useEffect(() => {
    fetchElections();
  }, []);

  // Add election
  const handleAddElection = async () => {
    if (!electionName || !electionDate) {
      alert("Please fill in all fields!");
      return;
    }
    try {
      const newElection = await addElectionApi({ name: electionName, date: electionDate });
      setElections([...elections, newElection]);
      setElectionName("");
      setElectionDate("");
    } catch (error) {
      console.error("Error adding election:", error);
    }
  };

  // Delete election
  const handleDeleteElection = async (id) => {
    if (!window.confirm("Are you sure you want to delete this election?")) return;
    try {
      await deleteElectionApi(id);
      setElections(elections.filter((e) => e.id !== id));
    } catch (error) {
      console.error("Error deleting election:", error);
    }
  };

  return (
    <div className="election-container">
      <h2>Election Management</h2>

      <div className="form-container">
        <input
          type="text"
          placeholder="Election Name"
          value={electionName}
          onChange={(e) => setElectionName(e.target.value)}
        />
        <input
          type="date"
          value={electionDate}
          onChange={(e) => setElectionDate(e.target.value)}
        />
        <button className="add-btn" onClick={handleAddElection}>
          Add Election
        </button>
      </div>

      <div className="list-container">
        <h3>Available Elections</h3>
        {elections.length === 0 ? (
          <p className="empty-msg">No elections available</p>
        ) : (
          <ul>
            {elections.map((election) => (
              <li key={election.id}>
                <span>{election.name} — {election.date}</span>
                <button className="delete-btn" onClick={() => handleDeleteElection(election.id)}>
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default ElectionManagement;
