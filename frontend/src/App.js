import React, { useState, useEffect } from 'react';
import './App.css';
import UserList from './components/UserList';
import UserForm from './components/UserForm';
import { getUsers, createUser, updateUser, deleteUser } from './services/userService';

function App() {
  const [users, setUsers] = useState([]);
  const [editingUser, setEditingUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getUsers();
      setUsers(data);
    } catch (err) {
      setError('Failed to load users. Please ensure the backend is running.');
      console.error('Error loading users:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (userData) => {
    try {
      setError(null);
      await createUser(userData);
      await loadUsers();
    } catch (err) {
      setError('Failed to create user: ' + (err.message || 'Unknown error'));
      throw err;
    }
  };

  const handleUpdateUser = async (id, userData) => {
    try {
      setError(null);
      await updateUser(id, userData);
      setEditingUser(null);
      await loadUsers();
    } catch (err) {
      setError('Failed to update user: ' + (err.message || 'Unknown error'));
      throw err;
    }
  };

  const handleDeleteUser = async (id) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        setError(null);
        await deleteUser(id);
        await loadUsers();
      } catch (err) {
        setError('Failed to delete user: ' + (err.message || 'Unknown error'));
        console.error('Error deleting user:', err);
      }
    }
  };

  const handleEditUser = (user) => {
    setEditingUser(user);
  };

  const handleCancelEdit = () => {
    setEditingUser(null);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>👋 Hello Copilot - User Management</h1>
        <p>Manage users with Spring Boot backend and React frontend</p>
      </header>

      <main className="App-main">
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        <div className="content-container">
          <section className="form-section">
            <h2>{editingUser ? 'Edit User' : 'Add New User'}</h2>
            <UserForm
              user={editingUser}
              onSubmit={editingUser ? handleUpdateUser : handleCreateUser}
              onCancel={editingUser ? handleCancelEdit : null}
            />
          </section>

          <section className="list-section">
            <h2>Users List</h2>
            {loading ? (
              <div className="loading">Loading users...</div>
            ) : (
              <UserList
                users={users}
                onEdit={handleEditUser}
                onDelete={handleDeleteUser}
              />
            )}
          </section>
        </div>
      </main>
    </div>
  );
}

export default App;
