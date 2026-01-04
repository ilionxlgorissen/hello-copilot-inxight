# Quiz Master - Kahoot-like Quiz Application

This application has been transformed from a simple user management system into a comprehensive Kahoot-like quiz platform with two distinct user groups: quiz participants and administrators.

## Features

### For Administrators
- **User Authentication**: Secure login and registration with JWT tokens
- **Quiz Management**: Create, edit, and publish quizzes
- **Question Builder**: Add multiple-choice questions with configurable time limits and points
- **Live Session Control**: Start quiz sessions, move between questions, and monitor participants
- **Real-time Dashboard**: View active sessions and participant engagement
- **Session Codes**: Unique 6-character codes for participants to join

### For Participants
- **Guest Access**: Join quizzes without creating an account
- **Quick Join**: Enter quiz code and display name to participate
- **Real-time Gameplay**: Answer questions with live feedback
- **Leaderboard**: See rankings based on accuracy and speed
- **Points System**: Earn points for correct answers with time bonuses

## Technology Stack

### Backend (Java/Spring Boot)
- **Spring Boot 3.2.1** with Java 21
- **Spring Security** with JWT authentication
- **Spring WebSocket** for real-time communication (STOMP over SockJS)
- **Spring Data JPA** with H2 (dev) and PostgreSQL (prod)
- **Lombok** for reduced boilerplate

### Frontend (React)
- **React 18.2.0** with React Router 6
- **STOMP.js** and SockJS for WebSocket connections
- **Modern CSS** with gradient themes and responsive design

## Project Structure

### Backend Structure
```
backend/src/main/java/com/inxight/
├── controller/
│   ├── AuthController.java          # Login/register endpoints
│   ├── QuizController.java          # Quiz CRUD operations
│   ├── QuestionController.java      # Question management
│   ├── SessionController.java       # Session lifecycle
│   └── WebSocketQuizController.java # Real-time quiz messages
├── model/
│   ├── Admin.java                   # Admin user entity
│   ├── Participant.java             # Participant entity
│   ├── Quiz.java                    # Quiz entity
│   ├── Question.java                # Question entity
│   ├── Answer.java                  # Answer option entity
│   ├── QuizSession.java             # Live session entity
│   └── ParticipantResponse.java     # Answer submissions
├── repository/
│   ├── AdminRepository.java
│   ├── ParticipantRepository.java
│   ├── QuizRepository.java
│   ├── QuestionRepository.java
│   ├── AnswerRepository.java
│   ├── QuizSessionRepository.java
│   └── ParticipantResponseRepository.java
├── service/
│   ├── QuizService.java
│   ├── QuestionService.java
│   ├── QuizSessionService.java
│   └── ParticipantResponseService.java
├── security/
│   ├── JwtUtil.java                 # JWT token management
│   ├── JwtRequestFilter.java        # JWT authentication filter
│   └── SecurityConfig.java          # Security configuration
├── websocket/
│   └── WebSocketConfig.java         # WebSocket configuration
└── dto/
    ├── AuthRequest.java
    ├── AuthResponse.java
    └── QuizMessage.java             # WebSocket message format
```

### Frontend Structure
```
frontend/src/
├── components/
│   ├── LandingPage.js              # Role selection entry
│   ├── AdminLogin.js               # Admin authentication
│   ├── AdminDashboard.js           # Admin quiz management
│   ├── ParticipantJoin.js          # Participant entry with code
│   └── *.css                       # Component styles
├── services/
│   ├── authService.js              # Authentication API
│   ├── quizService.js              # Quiz management API
│   ├── sessionService.js           # Session management API
│   └── websocketService.js         # Real-time communication
└── App.js                          # Router configuration
```

## Getting Started

### Prerequisites
- **Java 21** or higher
- **Node.js 16+** and npm
- **Maven 3.6+**

### Running the Application

#### Backend
```bash
cd backend
mvn spring-boot:run
```
The backend will start on `http://localhost:8080`

#### Frontend
```bash
cd frontend
npm install
npm start
```
The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/admin/register` - Register new admin
- `POST /api/auth/admin/login` - Admin login
- `POST /api/auth/participant/guest` - Join as guest participant

### Quiz Management (Admin only)
- `GET /api/quizzes` - Get all quizzes
- `POST /api/quizzes` - Create new quiz
- `PUT /api/quizzes/{id}` - Update quiz
- `PUT /api/quizzes/{id}/publish` - Publish quiz
- `DELETE /api/quizzes/{id}` - Delete quiz

### Questions (Admin only)
- `GET /api/quizzes/{quizId}/questions` - Get questions
- `POST /api/quizzes/{quizId}/questions` - Add question

### Sessions
- `POST /api/sessions/quiz/{quizId}` - Create session (Admin)
- `GET /api/sessions/code/{code}` - Get session by code
- `PUT /api/sessions/{id}/start` - Start session
- `PUT /api/sessions/{id}/next` - Next question
- `PUT /api/sessions/{id}/complete` - Complete session
- `GET /api/sessions/{id}/leaderboard` - Get leaderboard

### WebSocket Topics
- `/app/quiz/join` - Join quiz session
- `/app/quiz/start` - Start quiz
- `/app/quiz/answer` - Submit answer
- `/app/quiz/next` - Move to next question
- `/app/quiz/leaderboard` - Request leaderboard
- `/topic/quiz/{sessionCode}` - Subscribe to session events

## Database Schema

### Tables
- **admins** - Administrator accounts
- **participants** - Participant accounts (can be guest or registered)
- **quizzes** - Quiz definitions
- **questions** - Questions within quizzes
- **answers** - Answer options for questions
- **quiz_sessions** - Live quiz sessions with unique codes
- **participant_responses** - Participant answer submissions

## Configuration

### Backend Configuration (`application.properties`)
```properties
# JWT Configuration
jwt.secret=mySecretKeyThatIsAtLeast256BitsLongForHS256AlgorithmToWorkProperly
jwt.expiration=86400000  # 24 hours

# WebSocket Configuration
spring.websocket.allowed-origins=http://localhost:3000
```

### Database Profiles
- **dev profile**: Uses H2 in-memory database
- **prod profile**: Uses PostgreSQL

## Security Features

- **JWT Authentication**: Stateless authentication with Bearer tokens
- **Role-based Access Control**: Separate permissions for ADMIN and PARTICIPANT roles
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Controlled cross-origin access
- **Protected Routes**: Frontend route guards based on authentication

## Real-time Features

The application uses WebSocket (STOMP over SockJS) for real-time features:

1. **Live Participant Join**: See participants join in real-time
2. **Synchronized Questions**: All participants see questions at the same time
3. **Instant Answer Feedback**: Participants get immediate feedback
4. **Live Leaderboard**: Rankings update as participants answer
5. **Session Control**: Admins control quiz flow in real-time

## Points System

- Base points are configured per question (default: 100)
- Faster responses earn more points (up to 50% time bonus)
- Incorrect answers earn 0 points
- Leaderboard ranks by total score, then by response time

## Future Enhancements

- Quiz categories and tagging
- Image support for questions
- Multiple quiz formats (true/false, fill-in-blank)
- Detailed analytics and reports
- Team-based competitions
- Quiz templates and sharing
- Mobile app support
- Export results to CSV/PDF

## Contributing

This project was transformed to demonstrate:
- Dual user role architecture
- Real-time communication with WebSocket
- JWT-based authentication
- React routing and protected routes
- Responsive UI design

## License

This is an educational project demonstrating full-stack application development.
