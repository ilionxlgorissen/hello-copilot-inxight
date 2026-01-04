import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import quizService from '../services/quizService';
import sessionService from '../services/sessionService';
import './AdminDashboard.css';

function AdminDashboard() {
  const [quizzes, setQuizzes] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showNewQuizForm, setShowNewQuizForm] = useState(false);
  const [newQuiz, setNewQuiz] = useState({ title: '', description: '' });
  const navigate = useNavigate();
  const user = authService.getCurrentUser();

  useEffect(() => {
    if (!authService.isAuthenticated() || user.role !== 'ADMIN') {
      navigate('/admin');
      return;
    }
    loadData();
  }, [navigate, user.role]);

  const loadData = async () => {
    try {
      const [quizzesData, sessionsData] = await Promise.all([
        quizService.getAllQuizzes(),
        sessionService.getAllSessions(),
      ]);
      setQuizzes(quizzesData);
      setSessions(sessionsData);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateQuiz = async (e) => {
    e.preventDefault();
    try {
      await quizService.createQuiz(newQuiz);
      setNewQuiz({ title: '', description: '' });
      setShowNewQuizForm(false);
      loadData();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleCreateSession = async (quizId) => {
    try {
      const session = await sessionService.createSession(quizId);
      navigate(`/admin/session/${session.id}`);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleLogout = () => {
    authService.logout();
    navigate('/');
  };

  if (loading) return <div className="loading">Loading...</div>;

  return (
    <div className="admin-dashboard">
      <header className="dashboard-header">
        <h1>Admin Dashboard</h1>
        <div className="header-actions">
          <span className="welcome-text">Welcome, {user.username}</span>
          <button onClick={handleLogout} className="logout-button">Logout</button>
        </div>
      </header>

      {error && <div className="error-message">{error}</div>}

      <div className="dashboard-content">
        <section className="dashboard-section">
          <div className="section-header">
            <h2>My Quizzes</h2>
            <button onClick={() => setShowNewQuizForm(!showNewQuizForm)} className="create-button">
              + New Quiz
            </button>
          </div>

          {showNewQuizForm && (
            <form onSubmit={handleCreateQuiz} className="new-quiz-form">
              <input
                type="text"
                placeholder="Quiz Title"
                value={newQuiz.title}
                onChange={(e) => setNewQuiz({ ...newQuiz, title: e.target.value })}
                required
              />
              <textarea
                placeholder="Quiz Description"
                value={newQuiz.description}
                onChange={(e) => setNewQuiz({ ...newQuiz, description: e.target.value })}
                rows="3"
              />
              <div className="form-actions">
                <button type="submit" className="submit-button">Create</button>
                <button type="button" onClick={() => setShowNewQuizForm(false)} className="cancel-button">
                  Cancel
                </button>
              </div>
            </form>
          )}

          <div className="quiz-grid">
            {quizzes.length === 0 ? (
              <p className="empty-state">No quizzes yet. Create your first quiz!</p>
            ) : (
              quizzes.map((quiz) => (
                <div key={quiz.id} className="quiz-card">
                  <h3>{quiz.title}</h3>
                  <p>{quiz.description || 'No description'}</p>
                  <div className="quiz-meta">
                    <span className={`status-badge ${quiz.published ? 'published' : 'draft'}`}>
                      {quiz.published ? 'Published' : 'Draft'}
                    </span>
                    <span>{quiz.questions?.length || 0} questions</span>
                  </div>
                  <div className="quiz-actions">
                    <button onClick={() => navigate(`/admin/quiz/${quiz.id}`)} className="edit-button">
                      Edit
                    </button>
                    {quiz.published && (
                      <button onClick={() => handleCreateSession(quiz.id)} className="start-button">
                        Start Session
                      </button>
                    )}
                  </div>
                </div>
              ))
            )}
          </div>
        </section>

        <section className="dashboard-section">
          <h2>Recent Sessions</h2>
          <div className="sessions-list">
            {sessions.length === 0 ? (
              <p className="empty-state">No sessions yet</p>
            ) : (
              sessions.slice(0, 5).map((session) => (
                <div key={session.id} className="session-item">
                  <div>
                    <strong>{session.quiz?.title}</strong>
                    <p>Code: {session.sessionCode}</p>
                  </div>
                  <span className={`status-badge ${session.status.toLowerCase()}`}>
                    {session.status}
                  </span>
                </div>
              ))
            )}
          </div>
        </section>
      </div>
    </div>
  );
}

export default AdminDashboard;
