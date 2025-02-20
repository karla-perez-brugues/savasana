import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('submit', () => {
    it('should register the user', () => {
      const registerObservable: Observable<void> = new Observable(observer => {observer.next()});

      jest.spyOn(authService, 'register').mockReturnValue(registerObservable);
      jest.spyOn(router, 'navigate').mockImplementation();

      component.submit();

      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should not register the user', () => {
      const registerObservable: Observable<void> = new Observable(observer => {observer.error(true)});

      jest.spyOn(authService, 'register').mockReturnValue(registerObservable);
      jest.spyOn(router, 'navigate').mockImplementation();

      component.submit();

      expect(component.onError).toEqual(true);
    })
  })
});
