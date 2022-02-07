import { Component, Input, OnInit } from '@angular/core';
import { Embed } from '../data/Embed';

@Component({
  selector: 'igni-stream-embed',
  templateUrl: './stream-embed.component.html',
  styleUrls: ['./stream-embed.component.css']
})
export class StreamEmbedComponent implements OnInit {

  @Input() embed: Embed | null = null;



  constructor() {
    return;
   }

  ngOnInit(): void {
    return;
  }

}
