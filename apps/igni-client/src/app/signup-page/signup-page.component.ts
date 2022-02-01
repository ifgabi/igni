import { Component, OnInit } from '@angular/core';
import { AccountService } from '../accountservice.service';

@Component({
  selector: 'igni-signup-page',
  templateUrl: './signup-page.component.html',
  styleUrls: ['./signup-page.component.css']
})
export class SignupPageComponent implements OnInit {

  username: string;
  email: string;
  password: string;

  usernameErrorMessage: string;
  passwordErrorMessage: string;
  emailErrorMessage: string;

  constructor(private accountService: AccountService) {
    this.username = "";
    this.email = "";
    this.password = "";
    this.usernameErrorMessage = "";
    this.passwordErrorMessage = "";
    this.emailErrorMessage = "";
   }

  ngOnInit(): void {
    this.accountService.signupUsernameErrorEvent.subscribe((message:string) => {
      this.usernameErrorMessage = message;
    });

    this.accountService.signupPasswordErrorEvent.subscribe((message:string) => {
      this.passwordErrorMessage = message;
    });

    this.accountService.signupEmailErrorEvent.subscribe((message:string) => {
      this.emailErrorMessage = message;
    });
  }

  signUp()
  {
    this.usernameErrorMessage = "";
    this.passwordErrorMessage = "";
    this.emailErrorMessage = "";
    this.accountService.signUp(this.username, this.email, this.password);
  }

}
