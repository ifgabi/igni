import { createAction, props } from '@ngrx/store';
import { Embed } from './data/Embed';

export const loadStreamsContents = createAction(
  '[ChatboxContent] Load Embeds',
  props<{ embeds: Array<Embed> }>()
);
