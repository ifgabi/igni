import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RecurseVisitor } from '@angular/compiler/src/i18n/i18n_ast';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { firstValueFrom, Observable } from 'rxjs';
import { StreamService } from '../stream/streamservice.service';
import { Embed } from '../stream/data/Embed';
import { EmbedRecv } from '../stream/data/EmbedRecv';
import { loadStreamsContents } from './streams-content.actions';
import { StreamsContentState } from './streams-content.reducer';
import { selectAllEmbeds } from './streams-content.selectors';

@Component({
  selector: 'igni-streams-page',
  templateUrl: './streams-page.component.html',
  styleUrls: ['./streams-page.component.css']
})
export class StreamsPageComponent implements OnInit, OnDestroy {

  embeds$: Observable<Array<Embed>> = this.store.select(selectAllEmbeds);
  numberOfPages: number;
  currentPage: number;

  constructor(
    private store : Store<StreamsContentState>,
    private streamService: StreamService,
    private activatedRoute: ActivatedRoute) {
      this.loadMiliseconds = 1000
      this.refreshEmbeds = -1;
      this.numberOfPages = -1;
      this.currentPage = 0;
      this.pagesToShow = [];
    }

  loadMiliseconds: number;
  refreshEmbeds: number;

  pagesToShow: number[];

  async ngOnInit(): Promise<void> {
    this.refreshEmbeds = window.setTimeout(() => {
      const idstr: string = (this.activatedRoute.snapshot.paramMap.get("pageId")?.valueOf()) ?? "0";
      const idnum: number = Number.parseInt(idstr);
      this.loadEmbeds(idnum);
    }, this.loadMiliseconds);
  }

  ngOnDestroy(): void {
      window.clearTimeout(this.refreshEmbeds);
  }

  async loadEmbeds(page: number)
  {
    const recv$ = await this.streamService.getEmbeds(page);

    firstValueFrom(recv$).then( recv => {
      if(recv !== null)
      {
        const embeds: Array<Embed> = recv.embeds;
        this.numberOfPages = recv.numberOfPages;
        this.currentPage = page;
        this.makePagesToShow();
        this.store.dispatch(loadStreamsContents({ embeds }));
      }
    });
  }

  makePagesToShow(){
    this.pagesToShow = [];
    let prev = -2;
    for(let i=0; i< this.numberOfPages; i++){
      if((0 + i < 1 || this.numberOfPages - i < 2 ) || ( i > this.currentPage - 2 &&  i < this.currentPage + 4)){
        this.pagesToShow.push(i);
        prev = i;
      }
      else {
        if( prev !== -1 )
        {
          prev = -1;
          this.pagesToShow.push(prev);
        }
      }
    }
    return;
  }

}
