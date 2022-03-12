import { createReducer, on } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';
import * as StreamsContentActions from './streams-content.actions';
import { Embed } from '../stream/data/Embed';
// import { Chatline } from './chatbox-content.models';

export const streamsContentFeatureKey = 'embeds';

export function selectEmbedId(a: Embed): number {
  return a?.id ?? -1;
}

export function sortById(a: Embed, b: Embed): number {
  return a.id > b.id ? 1 : -1;
}

export function sortByViewCountAndBumpDate(a: Embed, b: Embed) {
  if(a.viewCount == b.viewCount)
    return a.timeoutBumpDate < b.timeoutBumpDate ? 1 : -1;
  return a.viewCount < b.viewCount ? 1 : -1;
}

export const adapter: EntityAdapter<Embed> = createEntityAdapter<Embed>({
  selectId: selectEmbedId,
  sortComparer: sortByViewCountAndBumpDate,
});

// eslint-disable-next-line @typescript-eslint/no-empty-interface
export interface StreamsContentState extends EntityState<Embed> {
  selectedChatMessageId: number;
}

export const initialState: StreamsContentState = adapter.getInitialState({
  selectedChatMessageId: -1,
});

export const reducer = createReducer(
  initialState,

  on(StreamsContentActions.loadStreamsContents, (state, { embeds }) => {
      // let stateNew : StreamsContentState = adapter.removeAll(state);
      if(embeds !== null)
      {
        let stateNew : StreamsContentState = adapter.removeAll(state);
        stateNew = adapter.addMany(embeds, stateNew);
        return stateNew;
      }
      return state;
    }
  )
);

export const { selectAll } = adapter.getSelectors();
