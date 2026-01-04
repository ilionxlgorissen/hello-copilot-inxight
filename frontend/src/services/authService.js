const API_URL = '/api/auth';

const authService = {
  // Admin authentication
  async registerAdmin(username, email, password) {
    const response = await fetch(`${API_URL}/admin/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, email, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Registration failed');
    }

    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('username', data.username);
    return data;
  },

  async loginAdmin(username, password) {
    const response = await fetch(`${API_URL}/admin/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Login failed');
    }

    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('username', data.username);
    return data;
  },

  // Participant authentication
  async registerParticipant(displayName, email, password) {
    const response = await fetch(`${API_URL}/participant/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ displayName, email, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Registration failed');
    }

    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('username', data.username);
    return data;
  },

  async loginParticipant(email, password) {
    const response = await fetch(`${API_URL}/participant/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Login failed');
    }

    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('username', data.username);
    return data;
  },

  async guestParticipant(displayName) {
    const response = await fetch(`${API_URL}/participant/guest`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ displayName }),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Guest login failed');
    }

    const data = await response.json();
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('username', data.username);
    return data;
  },

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  },

  getCurrentUser() {
    return {
      token: localStorage.getItem('token'),
      role: localStorage.getItem('role'),
      userId: localStorage.getItem('userId'),
      username: localStorage.getItem('username'),
    };
  },

  isAuthenticated() {
    return !!localStorage.getItem('token');
  },

  getAuthHeader() {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
  },
};

export default authService;
