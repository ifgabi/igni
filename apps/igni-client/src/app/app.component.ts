import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';

import { AccountService } from './account/accountservice.service';

import { ChatboxComponent } from '@igni/igni-chatbox';

@Component({
  selector: 'igni-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'Igni Website';
  serverError: boolean;

  loginEventSubscription: Subscription | undefined;
  logoutEventSubscription: Subscription | undefined;
  authenticatedUpdateEventSubscription: Subscription | undefined;
  serverUnresponsiveEventSubscription: Subscription | undefined;

  loginSubject: Subject<any>;
  logoutSubject: Subject<any>;
  sessioncheckSubject: Subject<boolean>;

  constructor(public accountService: AccountService, public router:Router)
  {
    this.serverError = false;
    this.loginSubject = new Subject<any>();
    this.logoutSubject = new Subject<any>();
    this.sessioncheckSubject = new Subject<boolean>();
  }

  ngOnInit()
  {
    this.loginEventSubscription = this.accountService.loginEvent.subscribe(this.loginSetup.bind(this));
    this.logoutEventSubscription = this.accountService.logoutEvent.subscribe(this.logoutSetup.bind(this));
    this.authenticatedUpdateEventSubscription = this.accountService.authenticatedUpdateEvent.subscribe(this.authenticatedUpdateSetup.bind(this));
    this.serverUnresponsiveEventSubscription = this.accountService.serverUnresponsiveEvent.subscribe(this.serverUnresponsiveSetup.bind(this));
  }

  authenticatedUpdateSetup(authenticated: boolean)
  {

    this.sessioncheckSubject.next(authenticated);
    //TODO implement chatservice dependence
    if(authenticated)
    {
      // if(!this.chatservice.client.active)
      // {
      //   this.chatservice.connect();
      // }
    }
    else
    {
      // if(this.chatservice.client.active)
      //   this.chatservice.disconnect();
    }

    //this setup is called whenever theres a successful heartbeat reply from api
    //will use this in case there was a previous error
    //lets reload page now that we've got connection
    if(this.serverError)
    {
      window.location.reload();
    }

  }

  serverUnresponsiveSetup(unresponsive: boolean)
  {
    //TODO create an overlay message on top
    //"Unresponsive server: try a few page refreshes, if it doesn't work in 5 minutes, please contact an administrator."
    this.serverError = true;
  }

  loginSetup(loggedIn: boolean)
  {
    if(loggedIn)
    {
      this.loginSubject.next(null);
      //this.chatservice.connect();
      this.navigate("streams-page");
    }
  }

  logoutSetup(loggedOut: boolean)
  {
    if(loggedOut)
    {
      this.logoutSubject.next(null);
      //this.chatservice.disconnect();
      this.navigate("login-page");
    }
  }

  navigate(pagename:string)
  {
    this.router.navigate([ pagename ]);
  }
}
