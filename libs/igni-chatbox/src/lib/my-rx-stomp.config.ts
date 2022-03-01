import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';

export const myRxStompConfig: InjectableRxStompConfig = {
  // Which server?
  brokerURL: 'ws://localhost:8080/chat-websocket',

  // Headers
  // Typical keys: login, passcode, host
  // I use JSESSIONID cookie via Spring Security validation
  connectHeaders: {
    // login: 'guest',
    // passcode: 'guest',
  },

  // Interval in milliseconds, set to 0 to disable
  heartbeatIncoming: 1000,
  heartbeatOutgoing: 1000,

  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 500 (500 milli seconds)
  reconnectDelay: 1000,

  // Will log diagnostics on console
  // It can be quite verbose, not recommended in production
  // Skip this key to stop logging to console
  debug: (msg: string): void => {
    console.log(new Date(), msg);
  },
};
