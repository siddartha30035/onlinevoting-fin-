// src/services/authService.js
  import { API_BASE_URL } from "./api";

  export const loginUser = async (email, password) => {
    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });
      if (response.ok) {
        const data = await response.json();
        return { success: true, data, message: data?.message || "Login successful" };
      } else {
        let errorMsg = "Login failed";
        try {
          errorMsg = await response.text();
        } catch (_) {}
        return { success: false, message: errorMsg };
      }
    } catch (error) {
      console.error("Login error:", error);
      return { success: false, message: "Server error. Please try again later." };
    }
  };
