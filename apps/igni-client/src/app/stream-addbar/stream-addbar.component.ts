import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Embed } from '../stream/data/Embed';
import { StreamService } from '../stream/streamservice.service';

@Component({
  selector: 'igni-stream-addbar',
  templateUrl: './stream-addbar.component.html',
  styleUrls: ['./stream-addbar.component.css']
})
export class StreamAddbarComponent implements OnInit {

  token: string;
  embedSiteId: number;

  constructor( private streamService: StreamService, private router: Router ) {
    this.token = "";
    this.embedSiteId = 2;
  }

  ngOnInit(): void {
    return;
  }


  async submit()
  {
    if(this.token.length < 1)
    {
      //show error TODO
      return;
    }

    //TODO support more than youtube(1)
    const values$ = await this.streamService.addEmbed(this.embedSiteId, this.token);
    const embed: Embed | null = (await firstValueFrom(values$)).embed;

    if(embed !== null)
    {
      this.router.navigate(["stream-page", embed.id]);
    }


  }

}
