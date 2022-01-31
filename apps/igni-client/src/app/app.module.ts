import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NxWelcomeComponent } from './nx-welcome.component';

import { IgniChatboxModule } from '@memzgg/igni-chatbox';

@NgModule({
  declarations: [AppComponent, NxWelcomeComponent],
  imports: [BrowserModule, IgniChatboxModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
