import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account/accountservice.service';

@Component({
  selector: 'igni-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  username:string;
  password:string;

  constructor(private accountService:AccountService){
    this.username = "";
    this.password = "";
  }

  ngOnInit() {
    return;
  }

  login()
  {
    this.accountService.authenticate( this.username, this.password );
  }

}
