import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { EmbedRecv } from './data/EmbedRecv';
import { EmbedsRecv } from "./data/EmbedsRecv"

@Injectable({
  providedIn: 'root'
})
export class StreamService {


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

  constructor( private http:HttpClient ) { }


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

}
