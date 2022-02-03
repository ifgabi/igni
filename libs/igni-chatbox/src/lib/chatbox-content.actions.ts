import { createAction, props } from '@ngrx/store';
import { ChatMessage } from './data/ChatMessage';

export const loadChatboxContents = createAction(
  '[ChatboxContent] Load ChatMessages',
  props<{ chatMessages: Array<ChatMessage> }>()
);

export const loadChatline = createAction(
  '[ChatboxContent] Load ChatMessage',
  props<{ chatMessage: ChatMessage }>()
);
