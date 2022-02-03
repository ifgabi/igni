import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChatService{

  webSocketEndPoint: string = this.appconfig.gatewayApiUrl + '/chat-websocket';
  client: Stomp.Client;
  headers: Stomp.StompHeaders = {'content-type': 'application/json'};

  subscription: Stomp.StompSubscription;

  constructor() {
    this.client = new Stomp.Client();
    this.client.debug = null;

    this.client.onConnect = (frame) => {

      this.subscription = client.subscribe(topicPrefix + this.channel.id, this.onMessageReceived.bind(this));
      //sets message handler to handle inside the service
      mainchat.setMessageHandler(this.messageHandler.bind(this));
      this.channelSubscriptions.set(mainchat.channel.id, mainchat);

      //TODO load currentuser upon connect
      //TODO connect to all the user channels on launch
      // this.currentUser.channels.forEach(channel => {
      //   let chansub: ChannelSubscription = new ChannelSubscription(channel.id, this.client, this.channelPrefix);
      //   //sets message handler to handle inside the chatservice's messageHandler func
      //   chansub.setMessageHandler(this.messageHandler.bind(this));
      //   this.channelSubscriptions.set(chansub.channel.id, chansub);
      // });

      if(this.chatConnectedEvent.observers.length > 0)
        this.chatConnectedEvent.emit(true);
    };

    this.client.onWebSocketClose = (frame) => {
      this.clearSubscriptions();
      this.chatDisconnectedEvent.emit(true);
      console.log("@@@@@Websocket Closed Event");
    }

    // this.client.webSocket. = (frame) => {
    //   this.clearSubscriptions();
    //   this.chatDisconnectedEvent.emit(true);
    // }

    this.client.debug = (str) => {
      //console.debug(str);
    };

    this.client.webSocketFactory = () => {
      return new SockJS(this.webSocketEndPoint);
    };

    this.client.reconnectDelay = 5000;
  }

  initWS()
  {
    return;
  }

}
