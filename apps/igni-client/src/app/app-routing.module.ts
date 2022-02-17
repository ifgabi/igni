import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { StreamsPageComponent } from './streams-page/streams-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { SignupPageComponent } from './signup-page/signup-page.component';
import { StreamPageComponent } from './stream-page/stream-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/streams-page', pathMatch: 'full' },
  { path: 'streams-page', component: StreamsPageComponent },
  { path: 'streams-page/:pageId', component: StreamsPageComponent },
  { path: 'login-page', component: LoginPageComponent },
  { path: 'signup-page', component: SignupPageComponent },
  { path: 'stream-page/:embedId', component: StreamPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
