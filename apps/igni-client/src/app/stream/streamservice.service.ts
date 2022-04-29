import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, Subject } from 'rxjs';
import { EmbedRecv } from './data/EmbedRecv';
import { EmbedsRecv } from "./data/EmbedsRecv"
import { HeartbeatRecv } from './data/HeartbeatRecv';

@Injectable({
  providedIn: 'root'
})
export class StreamService {

  currentPageViewsChanged : Subject<number | null> = new Subject<number | null>();;

  constructor( private http:HttpClient ) {
  }

  public getEmbed(id: string | null) {

    const recv$: Observable<EmbedRecv> = this.http.get("http://localhost:8080/stream/" + id,
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).pipe(
      map(resp => resp.body as EmbedRecv)
    );

    return recv$;
  }


  public addEmbed(embedSiteId: number, token: string)
  {
    const recv$: Observable<EmbedRecv> = this.http.post("http://localhost:8080/addstream", {
      "embedSiteId": embedSiteId,
      "token": token
    }, {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).pipe(
        map((resp) => resp.body as EmbedRecv)
      );

    return recv$;
  }

  public getEmbeds(page: number){
    const recv$ = this.http.get("http://localhost:8080/streams/" + page,
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).pipe(
      map(
        (resp) => resp.body as EmbedsRecv
      )
    );

    return recv$;
  }

  public heartBeatView(embedId: number)
  {
    const recv$ = this.http.post("http://localhost:8080/heartbeatStream",
    {
      "embedId": embedId
    },
    {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).pipe(
      map(resp => {
        const result : HeartbeatRecv = resp.body as HeartbeatRecv;
        this.currentPageViewsChanged.next(result.count);
        return result;
      })
    );

    return recv$;
  }

}
