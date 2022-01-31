import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { NxWelcomeComponent } from './nx-welcome.component';

import { MemzggChatboxModule } from '@memzgg/memzgg-chatbox'

@NgModule({
  declarations: [AppComponent, NxWelcomeComponent],
  imports: [
    BrowserModule,
    MemzggChatboxModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
