import { Component, Input, OnInit } from '@angular/core';
import { Chatline } from '../chatbox-content.models';

@Component({
  selector: 'igni-chatlinecollection',
  templateUrl: './chatlinecollection.component.html',
  styleUrls: ['./chatlinecollection.component.css'],
})
export class ChatlinecollectionComponent implements OnInit {
  @Input() chatlines: Array<Chatline> | null = [];

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }
}
