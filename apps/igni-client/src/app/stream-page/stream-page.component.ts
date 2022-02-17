import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Embed } from '../stream/data/Embed';
import { StreamService } from '../stream/streamservice.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'igni-stream-page',
  templateUrl: './stream-page.component.html',
  styleUrls: ['./stream-page.component.css']
})
export class StreamPageComponent implements OnInit {

  embed: Embed | null;

  url: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private streamService: StreamService
    ){
      this.embed = null;
      this.url = "";
  }

  async ngOnInit(): Promise<void> {
    const embed$ = await this.streamService.getEmbed(this.activatedRoute.snapshot.paramMap.get("embedId"));

    firstValueFrom(embed$).then(recv => {
      this.embed = recv.embed;
      if(recv.embed.embedSite.code === "TWITCH_TOKEN")
      {
        this.url = 'https://player.twitch.tv/?channel=' + this.embed?.token + '&autoplay=true&parent=localhost';
      }
      if(recv.embed.embedSite.code === "YOUTUBE_TOKEN")
      {
        this.url = 'https://www.youtube.com/embed/' + this.embed?.token + '?autoplay=1';
      }
    });
  }

}
