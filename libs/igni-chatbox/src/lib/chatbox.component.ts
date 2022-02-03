import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { HttpClient, HttpContext, HttpHeaders } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { loadChatboxContents, loadChatline } from './chatbox-content.actions';
import { selectAllChatMessages } from './chatbox-content.selectors';
import { ChatboxContentState } from './chatbox-content.reducer';
import { ChatService } from './chatservice.service';

import { ChatMessage } from './data/ChatMessage';

@Component({
  selector: 'igni-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css'],
})
export class ChatboxComponent implements OnInit {

  @Input() loginEvent : EventEmitter<any>;
  @Input() logoutEvent : EventEmitter<any>;
  @Input() sessioncheckEvent: EventEmitter<boolean>;

  chatMessages$: Observable<Array<ChatMessage>> =
    this.store.select(selectAllChatMessages);

  textBoxMessage: string;

  constructor(
    private store: Store<ChatboxContentState>,
    private http: HttpClient,
    private chatService: ChatService
  ) {
    this.textBoxMessage = "";
    this.loginEvent = new EventEmitter<any>();
    this.logoutEvent = new EventEmitter<any>();
    this.sessioncheckEvent = new EventEmitter<boolean>();
  }

  ngOnInit(): void {


    this.chatService.receivedMessage.subscribe((chatMessage: ChatMessage) => {
      console.log("BEFORE DISPATCH???: " + chatMessage);
      this.store.dispatch(loadChatline({ chatMessage }));
    })

    // linesobs.subscribe((chatLines) => {
    //   this.store.dispatch(loadChatboxContents({ chatLines }));
    // });

    this.loginEvent.subscribe(this.onLogin.bind(this));
    this.logoutEvent.subscribe(this.onLogout.bind(this));
    this.sessioncheckEvent.subscribe(this.onSessioncheck.bind(this));
  }

  onLogin(test: any)
  {
    //TODO tell the chatservice what to do on login
    return;
  }

  onLogout(test: any)
  {
    //TODO tell the chatservice what to do on logout
    return;
  }

  onSessioncheck(loggedin: boolean)
  {
    //TODO tell chatservice what to do on sessionchecks
    return;
  }

  keydownEvent(event: any) {
    if (event.shiftKey && event.key === 'Enter') {
      this.textBoxMessage += '\n';
    } else if (event.key === 'Enter') {
      event.preventDefault();
      this.sendMessage();
    }
  }

  sendMessage() {
    if(this.textBoxMessage !== null)
      if(this.textBoxMessage !== undefined)
        if(this.textBoxMessage !== "")
        {
          this.chatService.sendMessage(this.textBoxMessage);
          this.textBoxMessage = "";
        }
    return;
  }
}
