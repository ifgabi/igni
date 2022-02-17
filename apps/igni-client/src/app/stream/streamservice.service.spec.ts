import { TestBed } from '@angular/core/testing';

import { StreamserviceService } from './streamservice.service';

describe('StreamserviceService', () => {
  let service: StreamserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StreamserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
