import { EventEmitter, Injectable } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { ChatMessage } from './data/ChatMessage';


@Injectable({
  providedIn: 'root'
})
export class ChatService{

  receivedMessage: EventEmitter<ChatMessage> = new EventEmitter();

  constructor(private rxStompService: RxStompService) {
    this.rxStompService.watch('/channel').subscribe((message: any) => {
      this.receivedMessage.emit(JSON.parse(message.body).chatMessage);
    });
  }

  sendMessage(msg: string) {
    this.rxStompService.publish({ destination: '/app/sendToChannel', body: JSON.stringify(msg) });
  }

}
