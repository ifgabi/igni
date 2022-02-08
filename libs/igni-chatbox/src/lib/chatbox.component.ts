import { Component, EventEmitter, Input, OnDestroy, OnInit } from '@angular/core';
import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Observable, Subscription } from 'rxjs';
import { loadChatboxContents, loadChatline, removeChatboxContents } from './chatbox-content.actions';
import { selectAllChatMessages } from './chatbox-content.selectors';
import { ChatboxContentState } from './chatbox-content.reducer';
import { ChatService } from './chatservice.service';

import { ChatMessage } from './data/ChatMessage';
import { ChatMessagesRecv } from './data/ChatMessagesRecv';

@Component({
  selector: 'igni-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css'],
})
export class ChatboxComponent implements OnInit, OnDestroy {

  @Input() loginEvent : Observable<any>;
  @Input() logoutEvent : Observable<any>;
  @Input() sessioncheckEvent: Observable<boolean>;
  loginEventSubscription: Subscription;
  logoutEventSubscription: Subscription;
  sessioncheckEventSubscription: Subscription;

  currentStatus: string;

  chatMessages$: Observable<Array<ChatMessage>> =
    this.store.select(selectAllChatMessages);

  textBoxMessage: string;

  showingLoginPrompt: boolean;

  statusMessage: string;

  scrollbox : any;

  constructor(
    private store: Store<ChatboxContentState>,
    private http: HttpClient,
    private chatService: ChatService
  ) {
    this.textBoxMessage = "";
    this.showingLoginPrompt = false;
    this.loginEvent = new Observable<any>();
    this.loginEventSubscription = new Subscription();
    this.logoutEvent = new Observable<any>();
    this.logoutEventSubscription = new Subscription();
    this.sessioncheckEvent = new Observable<boolean>();
    this.sessioncheckEventSubscription = new Subscription();

    this.currentStatus = "CLOSED";
    this.statusMessage = "";
  }

  ngOnInit(): void {
    this.scrollbox = document.getElementById("chat-scroll-box");

    this.chatService.receivedMessage.subscribe((chatMessage: ChatMessage) => {
      this.store.dispatch(loadChatline({ chatMessage }));


      setTimeout(() => {
        this.scrollToBottom();
      }, 10);
    })

    //event equilibrium parent(app) to child(chatbox)
    this.loginEvent.subscribe(this.onLogin.bind(this));
    this.logoutEvent.subscribe(this.onLogout.bind(this));
    this.sessioncheckEvent.subscribe(this.onSessioncheck.bind(this));

    this.chatService.chatStatus$.subscribe((payload) => {
      this.currentStatus = payload;
      if(payload === "CONNECTING")
      {
        this.statusMessage = "The chat is currently connecting.";
      }
      if(payload === "OPEN")
      {
        this.onChatConnected();
        this.statusMessage = "";
      }
      if(payload === "CLOSING")
      {
        this.statusMessage = "Disconnecting chat."
      }
      if(payload === "CLOSED")
      {
        this.statusMessage = "Chat disconnected."
      }
      setTimeout(() => {
        this.scrollToBottom();
      }, 10);
    });

    this.chatService.activate();
  }

  ngOnDestroy(): void {
    this.loginEventSubscription.unsubscribe();
    this.logoutEventSubscription.unsubscribe();
    this.sessioncheckEventSubscription.unsubscribe();

  }

  onLogin(test: any)
  {
    this.showingLoginPrompt = false;
    setTimeout(() => {
      this.chatService.forceReconnect()
    }, 2500);
  }

  onLogout(test: any)
  {
    this.showingLoginPrompt = true;
  }

  onSessioncheck(loggedin: boolean)
  {
    if(loggedin == false)
    {
      if(!this.showingLoginPrompt)
      {
        this.showingLoginPrompt = true;
        return;
      }
    }

    if(loggedin == true)
    {
      if(this.showingLoginPrompt)
      {
        this.showingLoginPrompt = false;
        return;
      }
    }

    return;
  }

  onChatConnected() {

    //empty
    this.store.dispatch(removeChatboxContents());

    const linesobs: Observable<HttpResponse<any>> = this.http.get("http://localhost:8080" + "/history",
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    });

    linesobs.subscribe((resp) => {
      const chatMessages: Array<ChatMessage> = resp.body?.messages
      this.store.dispatch(loadChatboxContents({ chatMessages }));
    });

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

  scrollToBottom()
  {
    this.scrollbox.scrollTop = this.scrollbox.scrollHeight;
  }
}

