import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';

import { IgniChatboxModule } from '@igni/igni-chatbox';
import { LoginPageComponent } from './login-page/login-page.component';
import { StreamsPageComponent } from './streams-page/streams-page.component';
import { SignupPageComponent } from './signup-page/signup-page.component';
import { AccountMenuComponent } from './account-menu/account-menu.component';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [AppComponent, LoginPageComponent, StreamsPageComponent, SignupPageComponent, AccountMenuComponent],
  imports: [BrowserModule, FormsModule, IgniChatboxModule, AppRoutingModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
