import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let service: SessionService;

  let sessionInformation: SessionInformation = {
    token: 'token',
    type: 'token',
    id: 1,
    username: 'karla@mail.com',
    firstName: 'Karla',
    lastName: 'Pérez',
    admin: true,
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('logIn', () => {
    it('should set user logged in', () => {
      service.logIn(sessionInformation);

      expect(service.sessionInformation).toEqual(sessionInformation);
      expect(service.isLogged).toBeTruthy();
    });
  });

  describe('logOut', () => {
    it('should set user logged out', () => {
      service.logOut();

      expect(service.sessionInformation).toEqual(undefined);
      expect(service.isLogged).toBeFalsy();
    })
  })
});
