// Centralized API base URL for frontend services
// Reads Vite env var and defaults to proxied path
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";
export default API_BASE_URL;
