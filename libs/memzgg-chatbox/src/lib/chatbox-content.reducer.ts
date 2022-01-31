import { createReducer, on } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity'
import * as ChatboxContentActions from './chatbox-content.actions';
import { Chatline } from './chatbox-content.models';

export const chatboxContentFeatureKey = 'chatlines';

export function selectChatlineId(a: Chatline): number {
  return a.id;
}

export function sortById(a: Chatline, b: Chatline): number {
  return a.id > b.id ? 1: -1;
}

export const adapter: EntityAdapter<Chatline> = createEntityAdapter<Chatline>({
  selectId: selectChatlineId,
  sortComparer: sortById
});

// eslint-disable-next-line @typescript-eslint/no-empty-interface
export interface ChatboxContentState extends EntityState<Chatline>{
  selectedChatlineId: number | null;
};

export const initialState: ChatboxContentState = adapter.getInitialState({
  selectedChatlineId: null
});

export const reducer = createReducer(
  initialState,

  on(ChatboxContentActions.loadChatboxContents, (state, { chatLines } ) => adapter.addMany( chatLines , state) ),

  on(ChatboxContentActions.loadChatline, (state, { chatLine } ) => adapter.addOne(chatLine, state) )

);

export const { selectAll } = adapter.getSelectors();
