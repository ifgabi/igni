import { Component, Input, OnInit } from '@angular/core';
import { Embed } from '../data/Embed';

@Component({
  selector: 'igni-streamscollection',
  templateUrl: './streamscollection.component.html',
  styleUrls: ['./streamscollection.component.css']
})
export class StreamscollectionComponent implements OnInit {

  @Input() embeds: Array<Embed> | null = [];

  constructor() { return; }

  ngOnInit(): void {
    return;
  }

}
