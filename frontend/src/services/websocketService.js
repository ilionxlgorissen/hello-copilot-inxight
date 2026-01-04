import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  constructor() {
    this.client = null;
    this.connected = false;
  }

  connect(sessionCode, onMessageReceived) {
    return new Promise((resolve, reject) => {
      this.client = new Client({
        webSocketFactory: () => new SockJS('/ws'),
        debug: (str) => {
          console.log('STOMP: ' + str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
      });

      this.client.onConnect = () => {
        console.log('Connected to WebSocket');
        this.connected = true;

        // Subscribe to quiz session topic
        this.client.subscribe(`/topic/quiz/${sessionCode}`, (message) => {
          const data = JSON.parse(message.body);
          onMessageReceived(data);
        });

        resolve();
      };

      this.client.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
        reject(new Error('WebSocket connection error'));
      };

      this.client.activate();
    });
  }

  sendMessage(destination, message) {
    if (this.client && this.connected) {
      this.client.publish({
        destination,
        body: JSON.stringify(message),
      });
    } else {
      console.error('WebSocket not connected');
    }
  }

  joinQuiz(sessionCode, participantId, participantName) {
    this.sendMessage('/app/quiz/join', {
      type: 'JOIN',
      sessionCode,
      participantId,
      participantName,
    });
  }

  startQuiz(sessionCode) {
    this.sendMessage('/app/quiz/start', {
      type: 'START',
      sessionCode,
    });
  }

  submitAnswer(sessionCode, participantId, questionId, answerId, responseTime) {
    this.sendMessage('/app/quiz/answer', {
      type: 'ANSWER',
      sessionCode,
      participantId,
      questionId,
      answerId,
      responseTime,
    });
  }

  nextQuestion(sessionCode) {
    this.sendMessage('/app/quiz/next', {
      type: 'NEXT',
      sessionCode,
    });
  }

  requestLeaderboard(sessionCode) {
    this.sendMessage('/app/quiz/leaderboard', {
      type: 'LEADERBOARD',
      sessionCode,
    });
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
      this.connected = false;
      console.log('Disconnected from WebSocket');
    }
  }
}

export default new WebSocketService();
