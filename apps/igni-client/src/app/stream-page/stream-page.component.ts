import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeUrl} from '@angular/platform-browser';
import { Embed } from './data/Embed';
import { EmbedRecv } from './data/EmbedRecv';

@Component({
  selector: 'igni-stream-page',
  templateUrl: './stream-page.component.html',
  styleUrls: ['./stream-page.component.css']
})
export class StreamPageComponent implements OnInit {

  embed: Embed | null;

  yturl: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private http: HttpClient,
    private sanitizer: DomSanitizer
    ){
      this.embed = null;
      this.yturl = "";
      return;
  }

  ngOnInit(): void {

    this.http.get("http://localhost:8080/stream/" + this.activatedRoute.snapshot.paramMap.get("embedId"),
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).subscribe( observer => {
      const recv: EmbedRecv | null = <EmbedRecv> (observer?.body ?? null);
      this.embed = recv?.embed ?? null;

      this.yturl = 'https://www.youtube.com/embed/' + this.embed?.token + '?autoplay=1';

    });

    return;
  }

}
