import { Component, Input, OnInit } from '@angular/core';
import { Chatline } from '../chatbox-content.models';

@Component({
  selector: 'memzgg-chatline-element',
  templateUrl: './chatline-element.component.html',
  styleUrls: ['./chatline-element.component.css']
})
export class ChatlineElementComponent implements OnInit {
  @Input() chatLine : Chatline = {id: -1, author: "", message: ""};

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }

}
