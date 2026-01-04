import authService from './authService';

const API_URL = '/api/sessions';

const sessionService = {
  async getAllSessions() {
    const response = await fetch(API_URL, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch sessions');
    }

    return response.json();
  },

  async getSessionById(id) {
    const response = await fetch(`${API_URL}/${id}`, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch session');
    }

    return response.json();
  },

  async getSessionByCode(code) {
    const response = await fetch(`${API_URL}/code/${code}`);

    if (!response.ok) {
      throw new Error('Failed to fetch session');
    }

    return response.json();
  },

  async createSession(quizId) {
    const response = await fetch(`${API_URL}/quiz/${quizId}`, {
      method: 'POST',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to create session');
    }

    return response.json();
  },

  async startSession(id) {
    const response = await fetch(`${API_URL}/${id}/start`, {
      method: 'PUT',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to start session');
    }

    return response.json();
  },

  async nextQuestion(id) {
    const response = await fetch(`${API_URL}/${id}/next`, {
      method: 'PUT',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to move to next question');
    }

    return response.json();
  },

  async completeSession(id) {
    const response = await fetch(`${API_URL}/${id}/complete`, {
      method: 'PUT',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to complete session');
    }

    return response.json();
  },

  async getLeaderboard(id) {
    const response = await fetch(`${API_URL}/${id}/leaderboard`, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch leaderboard');
    }

    return response.json();
  },
};

export default sessionService;
