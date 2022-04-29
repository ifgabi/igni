import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';

import { AccountService } from './account/accountservice.service';

import { ChatboxComponent } from '@igni/igni-chatbox';
import { StreamService } from './stream/streamservice.service';

@Component({
  selector: 'igni-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Igni Website';
  serverError: boolean;
  currentViews: number | null;

  loginEventSubscription: Subscription | undefined;
  logoutEventSubscription: Subscription | undefined;
  authenticatedUpdateEventSubscription: Subscription | undefined;
  serverUnresponsiveEventSubscription: Subscription | undefined;

  currentStreamViewsChangedSubscription: Subscription | undefined;

  loginSubject: Subject<any>;
  logoutSubject: Subject<any>;
  sessioncheckSubject: Subject<boolean>;

  user: string;

  constructor(
    public accountService: AccountService,
    public streamService: StreamService,
    public router:Router)
  {
    this.user = "not yet";
    this.currentViews = null;

    this.serverError = false;
    this.loginSubject = new Subject<boolean>();
    this.logoutSubject = new Subject<boolean>();
    this.sessioncheckSubject = new Subject<boolean>();
  }

  ngOnInit()
  {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;

    this.loginEventSubscription = this.accountService.loginEvent.subscribe(this.loginSetup.bind(this));
    this.logoutEventSubscription = this.accountService.logoutEvent.subscribe(this.logoutSetup.bind(this));
    this.serverUnresponsiveEventSubscription = this.accountService.serverUnresponsiveEvent.subscribe(this.serverUnresponsiveSetup.bind(this));
    this.authenticatedUpdateEventSubscription = this.accountService.authenticatedUpdateEvent.subscribe(
      (userDto) => {
        this.authenticatedUpdateSetup(userDto !== null);
      }
    );

    this.currentStreamViewsChangedSubscription = this.streamService.currentPageViewsChanged.subscribe(this.currentPageViewsChangedSetup.bind(this));
  }

  ngOnDestroy(){
      this.loginEventSubscription?.unsubscribe();
      this.logoutEventSubscription?.unsubscribe();
      this.serverUnresponsiveEventSubscription?.unsubscribe();
      this.authenticatedUpdateEventSubscription?.unsubscribe();
      this.currentStreamViewsChangedSubscription?.unsubscribe();

  }

  authenticatedUpdateSetup(authenticated: boolean)
  {

    this.sessioncheckSubject.next(authenticated);

  }

  serverUnresponsiveSetup(unresponsive: boolean)
  {
    //TODO create an overlay message on top
    //"Unresponsive server: try a few page refreshes, if it doesn't work in 5 minutes, please contact an administrator."
    this.serverError = true;
  }

  loginSetup(success: boolean)
  {
    if(success)
    {
      this.loginSubject.next(null);
      //this.chatservice.connect();
      this.navigate("streams-page");
      console.log("SHOULD HAVE NAVIGATED");
    }
  }

  logoutSetup(success: boolean)
  {
    if(success)
    {
      this.logoutSubject.next(null);
      //this.chatservice.disconnect();
      this.navigate("login-page");
    }
  }

  currentPageViewsChangedSetup(count: number | null)
  {
    this.currentViews = count;
  }

  navigate(pagename:string)
  {
    this.router.navigate([ pagename ]);
  }
}
