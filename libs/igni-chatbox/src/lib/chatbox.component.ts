import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { HttpClient, HttpContext, HttpHeaders } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Chatline } from './chatbox-content.models';
import { loadChatboxContents, loadChatline } from './chatbox-content.actions';
import { selectAllChatlines } from './chatbox-content.selectors';
import { ChatboxContentState } from './chatbox-content.reducer';

@Component({
  selector: 'igni-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css'],
})
export class ChatboxComponent implements OnInit {

  @Input() loginEvent : EventEmitter<any>;
  @Input() logoutEvent : EventEmitter<any>;
  @Input() sessioncheckEvent: EventEmitter<boolean>;

  chatlines$: Observable<Array<Chatline>> =
    this.store.select(selectAllChatlines);

  textBoxMessage: string;

  constructor(
    private store: Store<ChatboxContentState>,
    private http: HttpClient
  ) {
    this.textBoxMessage = "";
    this.loginEvent = new EventEmitter<any>();
    this.logoutEvent = new EventEmitter<any>();
    this.sessioncheckEvent = new EventEmitter<boolean>();
  }

  ngOnInit(): void {
    //TODO get from websocket instead
    const linesobs: Observable<Array<Chatline>> = this.http.get<
      Array<Chatline>
    >('http://localhost:8080/test', {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'body',
    });

    linesobs.subscribe((chatLines) => {
      this.store.dispatch(loadChatboxContents({ chatLines }));
    });

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
    //TODO chatservice send message to ws
    return;
  }
}
