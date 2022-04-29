import { NgModule } from '@angular/core';
import { Routes, RouterModule, ExtraOptions } from '@angular/router';

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

const options: ExtraOptions = {
  onSameUrlNavigation: 'reload',
}

@NgModule({
  imports: [
    RouterModule.forRoot(routes, options)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
