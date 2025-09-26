// src/services/voteService.js
import axios from "axios";
import { API_BASE_URL } from "./api";

const API_URL = `${API_BASE_URL}/votes`;

// Cast a vote
export const castVote = async ({ candidateId, electionId, voterId }) => {
  const res = await axios.post(`${API_URL}`, { candidateId, electionId, voterId });
  return res.data;
};

// Check if voter has already voted in an election
export const hasVoted = async (voterId, electionId) => {
  const res = await axios.get(`${API_URL}/check/${voterId}/election/${electionId}`);
  return res.data?.hasVoted === true;
};

// Get votes by election
export const getVotesByElection = async (electionId) => {
  const res = await axios.get(`${API_URL}/election/${electionId}`);
  return res.data;
};

// Get election results
export const getElectionResults = async (electionId) => {
  const res = await axios.get(`${API_URL}/results/election/${electionId}`);
  return res.data;
};

// Get total votes in an election
export const getTotalVotes = async (electionId) => {
  const res = await axios.get(`${API_URL}/count/election/${electionId}`);
  return res.data?.totalVotes ?? 0;
};
