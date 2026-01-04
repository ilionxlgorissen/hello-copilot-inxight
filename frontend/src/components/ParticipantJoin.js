import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';
import sessionService from '../services/sessionService';
import './ParticipantJoin.css';

function ParticipantJoin() {
  const [displayName, setDisplayName] = useState('');
  const [sessionCode, setSessionCode] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleJoin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // First check if session exists
      await sessionService.getSessionByCode(sessionCode.toUpperCase());
      
      // Create guest participant
      await authService.guestParticipant(displayName);
      
      // Navigate to quiz lobby
      navigate(`/participant/quiz/${sessionCode.toUpperCase()}`);
    } catch (err) {
      setError(err.message || 'Failed to join quiz');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="participant-join-page">
      <div className="participant-join-container">
        <Link to="/" className="back-button">← Back to Home</Link>
        
        <h1>Join Quiz</h1>
        <p className="subtitle">Enter your name and quiz code to participate</p>
        
        {error && <div className="error-message">{error}</div>}
        
        <form onSubmit={handleJoin} className="join-form">
          <div className="form-group">
            <label>Your Name</label>
            <input
              type="text"
              value={displayName}
              onChange={(e) => setDisplayName(e.target.value)}
              required
              placeholder="Enter your display name"
              maxLength="30"
            />
          </div>

          <div className="form-group">
            <label>Quiz Code</label>
            <input
              type="text"
              value={sessionCode}
              onChange={(e) => setSessionCode(e.target.value.toUpperCase())}
              required
              placeholder="Enter 6-digit code"
              maxLength="6"
              style={{ textTransform: 'uppercase', letterSpacing: '4px', fontSize: '1.5rem', textAlign: 'center' }}
            />
          </div>

          <button type="submit" className="join-button" disabled={loading}>
            {loading ? 'Joining...' : 'Join Quiz'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default ParticipantJoin;
