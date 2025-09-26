// src/services/candidateService.js
  import axios from "axios";
  import { API_BASE_URL } from "./api";

  const API_URL = `${API_BASE_URL}/candidates`;

// Get all candidates
export const getAllCandidates = async () => {
  const res = await axios.get(API_URL);
  return res.data;
};

// Get candidates for a specific election
export const getCandidatesByElection = async (electionId) => {
  const res = await axios.get(`${API_URL}/election/${electionId}`);
  return res.data;
};

// Vote for a candidate
export const voteForCandidate = async (candidateId, electionId, voterId) => {
  const res = await axios.post(`${API_URL}/${candidateId}/vote`, {
    electionId,
    voterId,
  });
  return res.data;
};

// Add a new candidate
export const addCandidate = async (candidateData) => {
  const res = await axios.post(API_URL, candidateData);
  return res.data;
};

// Remove a candidate by ID
export const removeCandidate = async (candidateId) => {
  const res = await axios.delete(`${API_URL}/${candidateId}`);
  return res.data;
};
