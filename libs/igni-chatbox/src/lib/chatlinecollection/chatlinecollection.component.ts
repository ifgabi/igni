import { Component, Input, OnInit } from '@angular/core';
import { ChatMessage } from '../data/ChatMessage';

@Component({
  selector: 'igni-chatlinecollection',
  templateUrl: './chatlinecollection.component.html',
  styleUrls: ['./chatlinecollection.component.css'],
})
export class ChatlinecollectionComponent implements OnInit {
  @Input() chatMessages: Array<ChatMessage> | null = [];
  @Input() statusMessage: string;

  constructor() {
    this.statusMessage = "";
  }

  ngOnInit(): void {
    return;
  }
}
