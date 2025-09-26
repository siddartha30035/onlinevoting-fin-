import React from "react";
import { Link, useNavigate, Outlet } from "react-router-dom"; // import Outlet
import "../styles/dashboard.css";

function Dashboard() {
  const navigate = useNavigate();

  return (
    <div>
      {/* Navbar */}
      <div className="navbar">
        <div className="nav-left">
          <Link to="/dashboard">🏠 Home</Link>
          <Link to="/dashboard/voters">Voter Management</Link>
          <Link to="/dashboard/candidates">Candidate Management</Link>
          <Link to="/dashboard/elections">Fetch Elections</Link>
          <Link to="/dashboard/manage-elections">Election Management</Link>
          <Link to="/dashboard/results">📊 Results</Link>
        </div>

        <div className="nav-right">
          <div className="profile-menu">
            <span className="profile-icon">👤</span>
            <div className="dropdown">
              <button onClick={() => navigate("/dashboard/profile")}>Profile</button>
              <button
                onClick={() => {
                  try {
                    localStorage.removeItem("voterId");
                    localStorage.removeItem("voterName");
                    localStorage.removeItem("voterEmail");
                  } catch (_) {}
                  navigate("/");
                }}
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Dashboard Content */}
      <div className="dashboard-content">
        <h1 className="welcome-message">Welcome, Voter!</h1>
        <p className="subtext">Manage elections, candidates, and voters from here.</p>

        {/* This is where nested pages like VoterManagement will render */}
        <Outlet />
      </div>
    </div>
  );
}

export default Dashboard;
