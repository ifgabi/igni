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
import { AccountService } from './account/accountservice.service';

import { StoreModule } from '@ngrx/store';
import * as fromStreamsContent from './streams-page/streams-content.reducer';
import { EffectsModule, EffectsRootModule } from '@ngrx/effects';
import { StreamsContentEffects } from './streams-page/streams-content.effects';
import { StreamscollectionComponent } from './streams-page/streamscollection/streamscollection.component';
import { StreamEmbedComponent } from './streams-page/stream-embed/stream-embed.component';
import { CommonModule } from '@angular/common';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    StreamsPageComponent,
    SignupPageComponent,
    AccountMenuComponent,
    StreamscollectionComponent,
    StreamEmbedComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    CommonModule,
    IgniChatboxModule,
    AppRoutingModule,
    StoreModule.forRoot({}),
    StoreModule.forFeature(
      fromStreamsContent.streamsContentFeatureKey,
      fromStreamsContent.reducer
    ),
    EffectsModule.forRoot([]),
    EffectsModule.forFeature([StreamsContentEffects])
  ],
  providers: [AccountService],
  bootstrap: [AppComponent],
})
export class AppModule {}
