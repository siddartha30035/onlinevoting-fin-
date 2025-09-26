import React, { useState, useEffect } from "react";
import "../styles/CandidateManagement.css";
import { getAllCandidates, addCandidate, removeCandidate } from "../services/candidateService";

const CandidateManagement = () => {
  const [candidates, setCandidates] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    party: "",
    age: "",
  });
  const [error, setError] = useState(null);

  // Fetch candidates on mount
  useEffect(() => {
    fetchCandidates();
  }, []);

  const fetchCandidates = async () => {
    try {
      const data = await getAllCandidates();
      setCandidates(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      console.error("Error fetching candidates:", err);
      setError("Failed to fetch candidates. Check backend connection.");
      setCandidates([]);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleAdd = async (e) => {
    e.preventDefault();
    if (formData.name && formData.party && formData.age) {
      try {
        const candidateData = {
          ...formData,
          age: Number(formData.age),
        };
        await addCandidate(candidateData);

        setFormData({ name: "", party: "", age: "" });
        fetchCandidates();
        setError(null);
      } catch (err) {
        console.error("Error adding candidate:", err);
        setError("Failed to add candidate. Check backend or CORS.");
      }
    } else {
      setError("All fields are required.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this candidate?")) return;
    try {
      await removeCandidate(id);
      fetchCandidates();
      setError(null);
    } catch (err) {
      console.error("Error deleting candidate:", err);
      setError("Failed to delete candidate. Check backend or CORS.");
    }
  };

  return (
    <div className="candidate-container">
      <h1>Candidate Management</h1>

      {error && <p className="error-message">{error}</p>}

      <form onSubmit={handleAdd} className="candidate-form">
        <input
          type="text"
          name="name"
          placeholder="Candidate Name"
          value={formData.name}
          onChange={handleChange}
        />
        <input
          type="text"
          name="party"
          placeholder="Party"
          value={formData.party}
          onChange={handleChange}
        />
        <input
          type="number"
          name="age"
          placeholder="Age"
          value={formData.age}
          onChange={handleChange}
        />
        <button type="submit">Add Candidate</button>
      </form>

      <div className="candidate-list">
        {candidates && candidates.length > 0 ? (
          <ul>
            {candidates.map((c) => (
              <li key={c.id}>
                <span>
                  {c.name} - {c.party} ({c.age} years)
                </span>
                <button onClick={() => handleDelete(c.id)}>Delete</button>
              </li>
            ))}
          </ul>
        ) : (
          <p className="no-candidate">No candidates added yet.</p>
        )}
      </div>
    </div>
  );
};

export default CandidateManagement;
