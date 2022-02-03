import { Component, Input, OnInit } from '@angular/core';
import { ChatMessage } from '../data/ChatMessage';

@Component({
  selector: 'igni-chatline-element',
  templateUrl: './chatline-element.component.html',
  styleUrls: ['./chatline-element.component.css'],
})
export class ChatlineElementComponent implements OnInit {
  @Input() chatMessage: ChatMessage | null = null;

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }
}
