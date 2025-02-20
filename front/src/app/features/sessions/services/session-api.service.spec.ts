import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {Session} from "../interfaces/session.interface";
import {Observable, of} from "rxjs";

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient: HttpClient;

  let session: Session = {
    id: 1,
    name: 'Reformer pilates',
    description: 'Pilates session for beginners.',
    date: new Date('2025-02-19 16:00:00'),
    teacher_id: 12,
    users: [1,2],
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });

    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should get all sessions', () => {
      const sessionsObservable = new Observable<Session[]>(observer => {observer.next([session])});
      jest.spyOn(httpClient, 'get').mockReturnValue(sessionsObservable);

      service.all();

      expect(httpClient.get).toHaveBeenCalledWith('api/session');
    });
  });

  describe('detail', () => {
    it('should get one session', () => {
      const sessionObservable = new Observable<Session>(observer => {observer.next(session)});
      jest.spyOn(httpClient, 'get').mockReturnValue(sessionObservable);

      service.detail('1');

      expect(httpClient.get).toHaveBeenCalledWith('api/session/1');
    });
  });

  describe('delete', () => {
    it('should delete one session', () => {
      jest.spyOn(httpClient, 'delete').mockImplementation(() => of({}))

      service.delete('1');

      expect(httpClient.delete).toHaveBeenCalledWith('api/session/1');
    });
  });

  describe('create', () => {
    it('should create one session', () => {
      const sessionObservable = new Observable<Session>(observer => {observer.next(session)});
      jest.spyOn(httpClient, 'post').mockReturnValue(sessionObservable);

      service.create(session);

      expect(httpClient.post).toHaveBeenCalledWith('api/session', session);
    });
  });

  describe('update', () => {
    it('should update one session', () => {
      const sessionObservable = new Observable<Session>(observer => {observer.next(session)});
      jest.spyOn(httpClient, 'put').mockReturnValue(sessionObservable);

      service.update('1', session);

      expect(httpClient.put).toHaveBeenCalledWith('api/session/1', session);
    });
  });

  describe('participate', () => {
    it('should add a participation to a session', () => {
      jest.spyOn(httpClient, 'post').mockImplementation(() => of(void 0));

      service.participate('1', '1');

      expect(httpClient.post).toHaveBeenCalledWith('api/session/1/participate/1', null);
    });
  });

  describe('unParticipate', () => {
    it('should remove participation from session', () => {
      jest.spyOn(httpClient, 'delete').mockImplementation(() => of(void 0));

      service.unParticipate('1', '1');

      expect(httpClient.delete).toHaveBeenCalledWith('api/session/1/participate/1');
    });
  });
});
