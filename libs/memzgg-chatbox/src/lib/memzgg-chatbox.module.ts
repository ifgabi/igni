import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule} from '@angular/common/http'
import { ChatboxComponent } from './chatbox.component';
import { StoreModule } from '@ngrx/store';
import * as fromChatboxContent from './chatbox-content.reducer';
import { EffectsModule, EffectsRootModule } from '@ngrx/effects';
import { ChatboxContentEffects } from './chatbox-content.effects';
import { ChatlineElementComponent } from './chatline-element/chatline-element.component';
import { ChatlinecollectionComponent } from './chatlinecollection/chatlinecollection.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule,
    StoreModule.forRoot({}),
    StoreModule.forFeature(fromChatboxContent.chatboxContentFeatureKey, fromChatboxContent.reducer),
    EffectsModule.forRoot([]),
    EffectsModule.forFeature([ChatboxContentEffects])
  ],
  declarations: [ChatboxComponent, ChatlineElementComponent, ChatlinecollectionComponent],
  exports: [ChatboxComponent],
})
export class MemzggChatboxModule {}
