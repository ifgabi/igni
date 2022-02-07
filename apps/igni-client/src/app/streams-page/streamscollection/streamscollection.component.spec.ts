import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StreamscollectionComponent } from './streamscollection.component';

describe('StreamscollectionComponent', () => {
  let component: StreamscollectionComponent;
  let fixture: ComponentFixture<StreamscollectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StreamscollectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StreamscollectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
