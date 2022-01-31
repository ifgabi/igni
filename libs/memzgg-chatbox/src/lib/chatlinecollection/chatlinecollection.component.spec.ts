import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatlinecollectionComponent } from './chatlinecollection.component';

describe('ChatlinecollectionComponent', () => {
  let component: ChatlinecollectionComponent;
  let fixture: ComponentFixture<ChatlinecollectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChatlinecollectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatlinecollectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
