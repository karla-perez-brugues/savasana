import {HttpClient, HttpClientModule} from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";
import {Observable} from "rxjs";
import {LoginRequest} from "../../interfaces/loginRequest.interface";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  let httpClient: HttpClient;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    }).compileComponents();

    httpClient = TestBed.inject(HttpClient);
    authService = new AuthService(httpClient);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('submit', () => {
    const sessionInformation: SessionInformation = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'karla@mail.com',
      firstName: 'Karla',
      lastName: 'Pérez',
      admin: true,
    }

    const loginRequest: LoginRequest = {
      email: "karla@mail.com",
      password: "password",
    };

    it('should log the user in', () => {
      component.form.setValue(loginRequest);

      const loginObservable: Observable<SessionInformation> = new Observable(observer => {observer.next(sessionInformation)});

      jest.spyOn(authService, 'login').mockReturnValue(loginObservable);
      jest.spyOn(httpClient, 'post').mockReturnValue(loginObservable);
      jest.spyOn(sessionService, 'logIn').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.submit();

      expect(httpClient.post).toHaveBeenCalledWith('api/auth/login', loginRequest);
      expect(sessionService.logIn).toHaveBeenCalledWith(sessionInformation);
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should show error', () => {
      component.form.setValue(loginRequest);

      const loginObservable: Observable<SessionInformation> = new Observable(observer => {observer.error(true)});

      jest.spyOn(authService, 'login').mockReturnValue(loginObservable);
      jest.spyOn(httpClient, 'post').mockReturnValue(loginObservable);
      jest.spyOn(sessionService, 'logIn').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.submit();

      expect(httpClient.post).toHaveBeenCalledWith('api/auth/login', loginRequest);
      expect(component.onError).toBe(true);
    });
  });
});
