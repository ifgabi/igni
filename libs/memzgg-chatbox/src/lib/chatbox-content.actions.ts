import { createAction, props } from '@ngrx/store';
import { Chatline } from './chatbox-content.models';

export const loadChatboxContents = createAction(
  '[ChatboxContent] Load ChatboxContents',
  props< { chatLines: Array<Chatline> } >()
);

export const loadChatline = createAction(
  '[ChatboxContent] Load Chatline',
  props< { chatLine: Chatline } >()
);
