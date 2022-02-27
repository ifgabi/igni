import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Embed } from '../stream/data/Embed';
import { StreamService } from '../stream/streamservice.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'igni-stream-page',
  templateUrl: './stream-page.component.html',
  styleUrls: ['./stream-page.component.css']
})
export class StreamPageComponent implements OnInit, OnDestroy {

  embed: Embed | null;
  viewCount: number | null;

  @ViewChild('streamViewer')
  viewerElement!: ElementRef<HTMLInputElement>;
  @ViewChild('streamViewerCount')
  viewerCountElement!: ElementRef<HTMLInputElement>;
  lastHeightViewer: number;

  heartbeatInterval: number;

  url: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private streamService: StreamService,
    private element: ElementRef
    ){
      this.embed = null;
      this.viewCount = null;
      this.url = "";
      this.heartbeatInterval = -1;
      this.lastHeightViewer = 0;
  }
  ngOnDestroy(): void {
    window.clearInterval(this.heartbeatInterval);
  }

  ngDoCheck(): void {
    this.lastHeightViewer = this.element.nativeElement.offsetHeight;
    // this.viewerElement.nativeElement.style.background = "blue;";
    // this.viewerCountElement.nativeElement.style.position = "relative";
    // this.viewerCountElement.nativeElement.style = "relative";
  }

  ngAfterViewChecked(): void {
    this.viewerCountElement.nativeElement.style.position = "relative";
    this.viewerCountElement.nativeElement.style.width = "100%";
    this.viewerCountElement.nativeElement.style.top = this.lastHeightViewer + "px";
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
      this.heartbeat()
    });
    this.heartbeatInterval = window.setInterval(() => {
      this.heartbeat();
    }, 30000);
  }

  async heartbeat(): Promise<void> {

    console.log("EMBEDID: " + this.embed?.id);

    const viewCount$ = await this.streamService.heartBeatView(this.embed?.id ?? -1);
    firstValueFrom(viewCount$).then(recv =>
      {
        this.viewCount = recv.count;
      });
  }

}
