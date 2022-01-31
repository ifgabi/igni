import { createFeatureSelector, createSelector } from '@ngrx/store';
import { Chatline } from './chatbox-content.models';
import { ChatboxContentState } from './chatbox-content.reducer';
import * as fromChatlines from './chatbox-content.reducer';

export const selectChatboxContentState = createFeatureSelector<ChatboxContentState>('chatlines');

export const selectAllChatlines = createSelector(
  selectChatboxContentState,
  fromChatlines.selectAll
);
