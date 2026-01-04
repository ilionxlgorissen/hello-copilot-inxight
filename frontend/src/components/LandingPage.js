import React from 'react';
import { useNavigate } from 'react-router-dom';
import './LandingPage.css';

function LandingPage() {
  const navigate = useNavigate();

  return (
    <div className="landing-page">
      <div className="landing-container">
        <h1 className="landing-title">Quiz Master</h1>
        <p className="landing-subtitle">Choose your role to get started</p>
        
        <div className="role-selection">
          <div className="role-card" onClick={() => navigate('/participant')}>
            <div className="role-icon">🎯</div>
            <h2>Join as Participant</h2>
            <p>Enter a quiz code and compete with others</p>
          </div>

          <div className="role-card" onClick={() => navigate('/admin')}>
            <div className="role-icon">👨‍💼</div>
            <h2>Login as Admin</h2>
            <p>Create and manage quizzes</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
