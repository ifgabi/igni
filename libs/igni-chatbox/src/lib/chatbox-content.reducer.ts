import { createReducer, on } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';
import * as ChatboxContentActions from './chatbox-content.actions';
import { ChatMessage } from './data/ChatMessage';
// import { Chatline } from './chatbox-content.models';

export const chatboxContentFeatureKey = 'chatMessages';

export function selectChatMessageId(a: ChatMessage): number {
  console.log("A???:" + JSON.stringify(a));
  return a?.id ?? -1;
}

export function sortById(a: ChatMessage, b: ChatMessage): number {
  return a.id > b.id ? 1 : -1;
}

export const adapter: EntityAdapter<ChatMessage> = createEntityAdapter<ChatMessage>({
  selectId: selectChatMessageId,
  sortComparer: sortById,
});

// eslint-disable-next-line @typescript-eslint/no-empty-interface
export interface ChatboxContentState extends EntityState<ChatMessage> {
  selectedChatMessageId: number;
}

export const initialState: ChatboxContentState = adapter.getInitialState({
  selectedChatMessageId: -1,
});

export const reducer = createReducer(
  initialState,

  on(ChatboxContentActions.loadChatboxContents, (state, { chatMessages }) =>
    adapter.addMany(chatMessages, state)
  ),

  on(ChatboxContentActions.loadChatline, (state, { chatMessage }) =>
    adapter.addOne(chatMessage, state)
  ),

  on(ChatboxContentActions.removeChatboxContents, (state) => {
    const newState: ChatboxContentState = adapter.removeAll(state);
    return newState;
  })
);

export const { selectAll } = adapter.getSelectors();
