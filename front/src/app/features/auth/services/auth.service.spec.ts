import {AuthService} from "./auth.service";
import {TestBed} from "@angular/core/testing";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {HttpClient} from "@angular/common/http";
import { expect } from '@jest/globals';
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {Observable} from "rxjs";

describe('AuthService', () => {
  let service: AuthService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should call the register api', () => {
      const registerRequest: RegisterRequest = {
        email: 'karla@mail.com',
        firstName: 'Karla',
        lastName: 'Pérez',
        password: 'password',
      }

      jest.spyOn(httpClient, 'post').mockImplementation();

      service.register(registerRequest);

      expect(httpClient.post).toHaveBeenCalledWith('api/auth/register', registerRequest);
    });
  });

  describe('login', () => {
    it('should call the login api', () => {
      const loginRequest: LoginRequest = {
        email: 'karla@mail.com',
        password: 'password',
      };

      const sessionInformation: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'karla@mail.com',
        firstName: 'Karla',
        lastName: 'Pérez',
        admin: true,
      }

      const loginObservable: Observable<SessionInformation> = new Observable(observer => {observer.next(sessionInformation)});

      jest.spyOn(httpClient, 'post').mockReturnValue(loginObservable)

      service.login(loginRequest);

      expect(httpClient.post).toHaveBeenCalledWith('api/auth/login', loginRequest);
    })
  })
})
