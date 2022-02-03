import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ChatboxContentState } from './chatbox-content.reducer';
import * as fromChatMessages from './chatbox-content.reducer';

export const selectChatboxContentState =
  createFeatureSelector<ChatboxContentState>('chatMessages');

export const selectAllChatMessages = createSelector(
  selectChatboxContentState,
  fromChatMessages.selectAll
);
