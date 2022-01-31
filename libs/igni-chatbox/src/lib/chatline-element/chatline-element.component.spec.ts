import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatlineElementComponent } from './chatline-element.component';

describe('ChatlineElementComponent', () => {
  let component: ChatlineElementComponent;
  let fixture: ComponentFixture<ChatlineElementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ChatlineElementComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatlineElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
