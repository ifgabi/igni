import { createFeatureSelector, createSelector } from '@ngrx/store';
import { StreamsContentState } from './streams-content.reducer';
import * as fromEmbeds from './streams-content.reducer';

export const selectStreamsContentState =
  createFeatureSelector<StreamsContentState>('embeds');

export const selectAllEmbeds = createSelector(
  selectStreamsContentState,
  fromEmbeds.selectAll
);
