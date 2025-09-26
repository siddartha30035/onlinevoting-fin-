import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/VoterRegistration.css";
import { registerVoter } from "../services/voterService"; // API service

const VoterRegistration = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    voterId: "",
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    age: "",
    gender: "",
    address: "",
  });
  const [voterIdError, setVoterIdError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "voterId") {
      const regex = /^[A-Z0-9]+$/;
      setVoterIdError(!regex.test(value) ? "Voter ID must contain only uppercase letters and numbers" : "");
    }

    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (voterIdError) {
      alert("Please fix errors before submitting!");
      return;
    }
    if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    try {
      await registerVoter(formData);
      alert("Registration successful!");
      navigate("/"); // redirect to login
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <div className="registration-container">
      <div className="registration-box">
        <h2>Voter Registration</h2>
        <form onSubmit={handleSubmit}>
          {/* Voter ID */}
          <div className="input-group">
            <label>Voter ID</label>
            <input
              type="text"
              name="voterId"
              value={formData.voterId}
              onChange={handleChange}
              placeholder="Enter Voter ID (e.g., 2C3D23GF)"
              required
            />
          </div>
          {voterIdError && <p className="error">{voterIdError}</p>}

          {/* Full Name */}
          <div className="input-group">
            <label>Full Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Enter your full name"
              required
            />
          </div>

          {/* Email */}
          <div className="input-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="Enter your email"
              required
            />
          </div>

          {/* Password */}
          <div className="input-group password-group">
            <label>Password</label>
            <div className="password-wrapper">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter password"
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

          {/* Confirm Password */}
          <div className="input-group password-group">
            <label>Confirm Password</label>
            <div className="password-wrapper">
              <input
                type={showPassword ? "text" : "password"}
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Confirm password"
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

          {/* Age */}
          <div className="input-group">
            <label>Age</label>
            <input
              type="number"
              name="age"
              value={formData.age}
              onChange={handleChange}
              placeholder="Enter your age"
              required
            />
          </div>

          {/* Gender */}
          <div className="input-group">
            <label>Gender</label>
            <div className="gender-options">
              <label>
                <input
                  type="radio"
                  name="gender"
                  value="Male"
                  checked={formData.gender === "Male"}
                  onChange={handleChange}
                  required
                />
                Male
              </label>
              <label>
                <input
                  type="radio"
                  name="gender"
                  value="Female"
                  checked={formData.gender === "Female"}
                  onChange={handleChange}
                />
                Female
              </label>
              <label>
                <input
                  type="radio"
                  name="gender"
                  value="Other"
                  checked={formData.gender === "Other"}
                  onChange={handleChange}
                />
                Other
              </label>
            </div>
          </div>

          {/* Address */}
          <div className="input-group">
            <label>Address</label>
            <textarea
              name="address"
              value={formData.address}
              onChange={handleChange}
              placeholder="Enter your address"
              rows="3"
              required
            ></textarea>
          </div>

          {/* Submit Button */}
          <button type="submit">Register</button>
        </form>
      </div>
    </div>
  );
};

export default VoterRegistration;
