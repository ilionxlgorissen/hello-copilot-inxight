import authService from './authService';

const API_URL = '/api/quizzes';

const quizService = {
  async getAllQuizzes() {
    const response = await fetch(API_URL, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch quizzes');
    }

    return response.json();
  },

  async getPublishedQuizzes() {
    const response = await fetch(`${API_URL}/published`);

    if (!response.ok) {
      throw new Error('Failed to fetch published quizzes');
    }

    return response.json();
  },

  async getQuizById(id) {
    const response = await fetch(`${API_URL}/${id}`, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch quiz');
    }

    return response.json();
  },

  async createQuiz(quiz) {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader(),
      },
      body: JSON.stringify(quiz),
    });

    if (!response.ok) {
      throw new Error('Failed to create quiz');
    }

    return response.json();
  },

  async updateQuiz(id, quiz) {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader(),
      },
      body: JSON.stringify(quiz),
    });

    if (!response.ok) {
      throw new Error('Failed to update quiz');
    }

    return response.json();
  },

  async publishQuiz(id) {
    const response = await fetch(`${API_URL}/${id}/publish`, {
      method: 'PUT',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to publish quiz');
    }

    return response.json();
  },

  async deleteQuiz(id) {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE',
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to delete quiz');
    }
  },

  async getQuestions(quizId) {
    const response = await fetch(`${API_URL}/${quizId}/questions`, {
      headers: {
        ...authService.getAuthHeader(),
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch questions');
    }

    return response.json();
  },

  async createQuestion(quizId, question) {
    const response = await fetch(`${API_URL}/${quizId}/questions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authService.getAuthHeader(),
      },
      body: JSON.stringify(question),
    });

    if (!response.ok) {
      throw new Error('Failed to create question');
    }

    return response.json();
  },
};

export default quizService;
