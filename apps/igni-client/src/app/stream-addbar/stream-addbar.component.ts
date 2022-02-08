import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'igni-stream-addbar',
  templateUrl: './stream-addbar.component.html',
  styleUrls: ['./stream-addbar.component.css']
})
export class StreamAddbarComponent implements OnInit {

  token: string;

  constructor( private http:HttpClient) {
    this.token = "";
  }

  ngOnInit(): void {
    return;
  }

  submit()
  {
    if(this.token.length < 1)
    {
      //show error TODO
      return;
    }

    this.http.post("http://localhost:8080/addstream", {
      "embedSiteId": 1,
      "token": this.token
    }, {
      headers: new HttpHeaders(),
      withCredentials: true,
      responseType: 'json',
      observe: 'response'
    }).subscribe(obs => {
      console.log(obs.body);
    });
  }

}
