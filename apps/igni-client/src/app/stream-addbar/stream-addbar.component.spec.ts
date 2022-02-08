import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StreamAddbarComponent } from './stream-addbar.component';

describe('StreamAddbarComponent', () => {
  let component: StreamAddbarComponent;
  let fixture: ComponentFixture<StreamAddbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StreamAddbarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StreamAddbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
