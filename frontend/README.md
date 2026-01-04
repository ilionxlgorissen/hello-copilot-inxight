# Hello Copilot Frontend

React frontend for the Hello Copilot user management application.

## Prerequisites

- Node.js (v14 or higher)
- npm or yarn
- Backend server running on http://localhost:8080

## Installation

```bash
npm install
```

## Running the Application

```bash
npm start
```

The application will open at [http://localhost:3000](http://localhost:3000).

## Features

- View all users
- Add new users
- Edit existing users
- Delete users
- Real-time form validation
- Responsive design

## API Integration

The frontend connects to the Spring Boot backend API at `http://localhost:8080/api/users` with the following endpoints:

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Building for Production

```bash
npm run build
```

This creates an optimized production build in the `build/` directory.
