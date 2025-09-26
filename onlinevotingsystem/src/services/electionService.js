import axios from "axios";
import { API_BASE_URL } from "./api";

const API_URL = `${API_BASE_URL}/elections`;

// Get all elections
export const getAllElections = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// Add new election
export const addElectionApi = async (electionData) => {
  const response = await axios.post(API_URL, electionData);
  return response.data;
};

// Delete election
export const deleteElectionApi = async (id) => {
  await axios.delete(`${API_URL}/${id}`);
  return true;
};

// Get election by ID
export const getElectionById = async (id) => {
  const response = await axios.get(`${API_URL}/${id}`);
  return response.data;
};

// Get only active elections
export const getActiveElections = async () => {
  const response = await axios.get(`${API_URL}/active`);
  return response.data;
};

// Update election
export const updateElectionApi = async (id, data) => {
  const response = await axios.put(`${API_URL}/${id}`, data);
  return response.data;
};

// Update election status
export const updateElectionStatus = async (id, status) => {
  const response = await axios.patch(`${API_URL}/${id}/status`, { status });
  return response.data;
};

// Start election
export const startElection = async (id) => {
  const response = await axios.post(`${API_URL}/${id}/start`);
  return response.data;
};

// End election
export const endElection = async (id) => {
  const response = await axios.post(`${API_URL}/${id}/end`);
  return response.data;
};

// Get election results
export const getElectionResultsApi = async (id) => {
  const response = await axios.get(`${API_URL}/${id}/results`);
  return response.data;
};
