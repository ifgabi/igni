import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';

import { concatMap } from 'rxjs/operators';
import { Observable, EMPTY } from 'rxjs';

import * as StreamsContentActions from './streams-content.actions';

@Injectable()
export class StreamsContentEffects {
  loadStreamsContents$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(StreamsContentActions.loadStreamsContents),
      /** An EMPTY observable only emits completion. Replace with your own observable API request */
      concatMap(() => EMPTY as Observable<{ type: string }>)
    );
  });

  constructor(private actions$: Actions) {}
}
