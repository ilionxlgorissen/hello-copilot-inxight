import React from 'react';
import './UserList.css';

function UserList({ users, onEdit, onDelete }) {
  if (users.length === 0) {
    return (
      <div className="empty-state">
        <p>No users found. Add your first user to get started!</p>
      </div>
    );
  }

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <div className="user-list">
      {users.map((user) => (
        <div key={user.id} className="user-card">
          <div className="user-info">
            <h3>{user.name}</h3>
            <p className="user-email">{user.email}</p>
            <p className="user-date">Created: {formatDate(user.createdAt)}</p>
          </div>
          <div className="user-actions">
            <button
              className="btn-icon btn-edit"
              onClick={() => onEdit(user)}
              title="Edit user"
            >
              ✏️
            </button>
            <button
              className="btn-icon btn-delete"
              onClick={() => onDelete(user.id)}
              title="Delete user"
            >
              🗑️
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

export default UserList;
