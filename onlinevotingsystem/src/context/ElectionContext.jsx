import React, { createContext, useState, useEffect } from "react";
import { getAllElections, addElectionApi, deleteElectionApi } from "../services/electionService";

export const ElectionContext = createContext();

export const ElectionProvider = ({ children }) => {
  const [elections, setElections] = useState([]);
  const [candidates, setCandidates] = useState([]);

  useEffect(() => {
    const fetchElections = async () => {
      try {
        const data = await getAllElections();
        setElections(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Error fetching elections:", err);
        setElections([]);
      }
    };
    fetchElections();
  }, []);

  const addElection = async (election) => {
    try {
      const newElection = await addElectionApi(election);
      setElections((prev) => [...prev, newElection]);
    } catch (err) {
      console.error("Error adding election:", err);
    }
  };

  const deleteElection = async (id) => {
    try {
      await deleteElectionApi(id);
      setElections((prev) => prev.filter((e) => e.id !== id));
    } catch (err) {
      console.error("Error deleting election:", err);
    }
  };

  const addCandidate = (candidate) => {
    setCandidates((prev) => [...prev, candidate]);
  };

  const removeCandidate = (index) => {
    setCandidates((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <ElectionContext.Provider
      value={{
        elections,
        candidates,
        addElection,
        deleteElection,
        addCandidate,
        removeCandidate,
      }}
    >
      {children}
    </ElectionContext.Provider>
  );
};
