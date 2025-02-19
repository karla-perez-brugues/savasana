import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import {Teacher} from "../interfaces/teacher.interface";
import {Observable} from "rxjs";

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient: HttpClient;

  let teacher: Teacher = {
    id: 12,
    lastName: 'Villanueva',
    firstName: 'Jane',
    createdAt: new Date('2015-06-01 09:00:00'),
    updatedAt: new Date('2015-06-01 09:00:00'),
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });

    service = TestBed.inject(TeacherService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('all', () => {
    it('should get all teachers', () => {
      let teachersObservable: Observable<Teacher[]> = new Observable(observer => observer.next([teacher]));

      jest.spyOn(httpClient, 'get').mockReturnValue(teachersObservable);

      service.all();

      expect(httpClient.get).toHaveBeenCalledWith('api/teacher');
    });
  });

  describe('detail', () => {
    it('should get all teachers', () => {
      let teacherObservable: Observable<Teacher> = new Observable(observer => observer.next(teacher));

      jest.spyOn(httpClient, 'get').mockReturnValue(teacherObservable);

      service.detail('12');

      expect(httpClient.get).toHaveBeenCalledWith('api/teacher/12');
    });
  });
});
