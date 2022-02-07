import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginpromptComponent } from './loginprompt.component';

describe('LoginpromptComponent', () => {
  let component: LoginpromptComponent;
  let fixture: ComponentFixture<LoginpromptComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginpromptComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginpromptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
