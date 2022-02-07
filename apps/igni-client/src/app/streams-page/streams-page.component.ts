import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RecurseVisitor } from '@angular/compiler/src/i18n/i18n_ast';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Embed } from './data/Embed';
import { EmbedRecv } from './data/EmbedRecv';
import { loadStreamsContents } from './streams-content.actions';
import { StreamsContentState } from './streams-content.reducer';
import { selectAllEmbeds } from './streams-content.selectors';

@Component({
  selector: 'igni-streams-page',
  templateUrl: './streams-page.component.html',
  styleUrls: ['./streams-page.component.css']
})
export class StreamsPageComponent implements OnInit {

  embeds$: Observable<Array<Embed>> = this.store.select(selectAllEmbeds);

  constructor(
    private store : Store<StreamsContentState>,
    private http: HttpClient) {
      this.refreshMilliseconds = 3000
      this.refreshEmbeds = null;
    }

  refreshMilliseconds: number;
  refreshEmbeds: any;

  ngOnInit(): void {
    this.refreshEmbeds = setInterval(() => {

      this.http.get("http://localhost:8080/streams/0",
      {
        headers: new HttpHeaders(),
        withCredentials: true,
        responseType: 'json',
        observe: 'response'
      }).subscribe( observer => {
        const recv: EmbedRecv | null = <EmbedRecv> (observer?.body ?? null);
        if(recv !== null)
        {
          const embeds: Array<Embed> = recv.embeds;
          this.store.dispatch(loadStreamsContents({ embeds }));
        }
      })

    }, this.refreshMilliseconds);
  }

  //TODO end timer on destroy

}
