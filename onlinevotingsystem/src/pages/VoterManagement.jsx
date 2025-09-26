import React, { useEffect, useState } from "react";
import "../styles/VoterManagement.css";
import { getAllVoters, deleteVoterById } from "../services/voterService";

const VoterManagement = () => {
  const [voters, setVoters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch voters from backend
  const fetchVoters = async () => {
    try {
      const data = await getAllVoters(); // use axios service
      setVoters(data);
    } catch (err) {
      console.error("Error fetching voters:", err);
      setError("Failed to fetch voters");
    } finally {
      setLoading(false);
    }
  };

  // Delete voter
  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this voter?")) return;
    try {
      await deleteVoterById(id); // use axios service
      setVoters(voters.filter((voter) => voter.id !== id));
    } catch (err) {
      console.error("Error deleting voter:", err);
      setError("Failed to delete voter");
    }
  };

  useEffect(() => {
    fetchVoters();
  }, []);

  if (loading) return <p className="loading">Loading voters...</p>;
  if (error) return <p className="error-message">{error}</p>;
  if (voters.length === 0) return <p className="empty">No voters found</p>;

  return (
    <div className="voter-container">
      <h1 className="page-title">Voter Management</h1>
      <table className="voter-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Age</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {voters.map((voter) => (
            <tr key={voter.id}>
              <td>{voter.id}</td>
              <td>{voter.name}</td>
              <td>{voter.email}</td>
              <td>{voter.age}</td>
              <td>
                <button
                  className="delete-btn"
                  onClick={() => handleDelete(voter.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default VoterManagement;
