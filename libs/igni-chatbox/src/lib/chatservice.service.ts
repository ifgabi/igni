import { EventEmitter, Injectable } from '@angular/core';
import { RxStompService } from '@stomp/ng2-stompjs';
import { RxStompState } from '@stomp/rx-stomp'
import { map, Observable, withLatestFrom } from 'rxjs';
import { ChatMessage } from './data/ChatMessage';


@Injectable({
  providedIn: 'root'
})
export class ChatService{

  receivedMessage: EventEmitter<ChatMessage> = new EventEmitter();
  chatStatus$: Observable<string>;

  constructor(private rxStompService: RxStompService) {

    this.chatStatus$ = rxStompService.connectionState$.pipe(
      map((state) => {
        return RxStompState[state];
        })
      );


    this.rxStompService.watch('/channel').subscribe((message: any) => {
      this.receivedMessage.emit(JSON.parse(message.body).chatMessage);
    });
  }

  activate(){
    this.rxStompService.activate();
  }

  deactivate(){
    this.rxStompService.deactivate();
  }

  forceReconnect(){
    this.rxStompService.deactivate();
    setTimeout(() => {
      this.rxStompService.activate();
      return;
    }, 1000);
  }

  sendMessage(msg: string) {
    this.rxStompService.publish({ destination: '/app/sendToChannel', body: JSON.stringify(msg) });
  }

}
