import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account/accountservice.service';

@Component({
  selector: 'igni-account-menu',
  templateUrl: './account-menu.component.html',
  styleUrls: ['./account-menu.component.css']
})
export class AccountMenuComponent implements OnInit {

  // user: User | null;

  constructor(private accountService: AccountService) {
    // this.user = null;
  }

  ngOnInit(): void {
    return;
  }

  logoutClicked()
  {
    this.accountService.unauthenticate();
  }

}
