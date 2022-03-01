import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AccountService, UserDto } from '../account/accountservice.service';

@Component({
  selector: 'igni-account-menu',
  templateUrl: './account-menu.component.html',
  styleUrls: ['./account-menu.component.css']
})
export class AccountMenuComponent implements OnInit {

  currentUser$: Observable<UserDto | null>;

  constructor(private accountService: AccountService,
    public router: Router) {
    // this.user = null;
    this.currentUser$ = this.accountService.authenticatedUpdateEvent.asObservable();
  }

  ngOnInit(): void {
    ;
  }

  logoutClicked()
  {
    this.accountService.unauthenticate();
  }

}
