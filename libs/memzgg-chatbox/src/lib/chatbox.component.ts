import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpContext, HttpHeaders } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Chatline } from './chatbox-content.models';
import { loadChatboxContents, loadChatline } from './chatbox-content.actions'
import { selectAllChatlines } from './chatbox-content.selectors';
import { ChatboxContentState } from './chatbox-content.reducer';

@Component({
  selector: 'memzgg-chatbox',
  templateUrl: './chatbox.component.html',
  styleUrls: ['./chatbox.component.css']
})
export class ChatboxComponent implements OnInit {

  chatlines$: Observable<Array<Chatline>> = this.store.select(selectAllChatlines);

  textBoxMessage: string;

  constructor( private store: Store<ChatboxContentState>, private http: HttpClient) {
    this.textBoxMessage = "";
  }

  ngOnInit(): void {
    //TODO get from websocket instead
    const linesobs: Observable<Array<Chatline>> = this.http.get<Array<Chatline>>("http://localhost:8080/test", {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'body'
    });

    linesobs.subscribe( (chatLines) => {
      console.log("BEFORE DISPATCH" + chatLines);
      this.store.dispatch( loadChatboxContents({ chatLines }) );
    });
  }

  keydownEvent(event: any)
  {
    if (event.shiftKey && event.key === 'Enter')
    {
      this.textBoxMessage += "\n";
    } else if (event.key === 'Enter') {
      event.preventDefault();
      this.sendMessage();
    }
  }

  sendMessage()
  {
    //TODO chatservice send message to ws
    return;
  }

}
