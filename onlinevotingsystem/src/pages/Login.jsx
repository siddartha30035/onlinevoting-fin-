import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Login.css";
import { loginUser } from "../services/authService"; // import service

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const result = await loginUser(email, password);

    if (result.success) {
      try {
        // Persist voter info for later use (e.g., Elections page needs numeric voterId)
        const voter = result.data;
        if (voter && typeof voter.id !== "undefined") {
          localStorage.setItem("voterId", String(voter.id));
        }
        if (voter?.name) localStorage.setItem("voterName", voter.name);
        if (voter?.email) localStorage.setItem("voterEmail", voter.email);
      } catch (_) {}
      alert(result.message); // optional
      navigate("/dashboard");
    } else {
      setError(result.message);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h2>Online Voting Login</h2>
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Enter your email"
              required
            />
          </div>

          <div className="input-group password-group">
            <label>Password</label>
            <div className="password-wrapper">
              <input
                type={showPassword ? "text" : "password"}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
                required
              />
              <span
                className="toggle-password"
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? "🙈" : "👁️"}
              </span>
            </div>
          </div>

          {error && <p className="error-message">{error}</p>}
          <button type="submit">Login</button>
        </form>

        <p>
          Don't have an account?{" "}
          <Link className="register-link" to="/register">
            Register Here
          </Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
