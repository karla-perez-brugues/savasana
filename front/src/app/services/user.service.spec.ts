import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import {User} from "../interfaces/user.interface";
import {Observable, of} from "rxjs";

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });

    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getById', () => {
    it('should return the user', () => {
      const user: User = {
        id: 1,
        email: 'karla@mail.com',
        lastName: 'Pérez',
        firstName: 'Karla',
        admin: true,
        password: 'password',
        createdAt: new Date('2025-02-19 16:26:00'),
      };

      const userObservable: Observable<User> = new Observable(observer => {observer.next(user)});

      jest.spyOn(httpClient, 'get').mockReturnValue(userObservable);

      service.getById('12');

      expect(httpClient.get).toHaveBeenCalledWith('api/user/12');
    });
  });

  describe('delete', () => {
    it('should delete the user', () => {
      jest.spyOn(httpClient, 'delete').mockImplementation(() => of({}));

      service.delete('12');

      expect(httpClient.delete).toHaveBeenCalledWith('api/user/12');
    });
  });
});
