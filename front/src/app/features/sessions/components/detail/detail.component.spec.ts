import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { ActivatedRoute, Router } from "@angular/router";
import { SessionApiService } from "../../services/session-api.service";
import { TeacherService } from "../../../../services/teacher.service";
import { Session } from "../../interfaces/session.interface";
import { Observable, of } from "rxjs";
import { Teacher } from "../../../../interfaces/teacher.interface";

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'karla@mail.com',
      firstName: 'Karla',
      lastName: 'Pérez',
      admin: true,
    }
  }

  const session: Session = {
    id: 1,
    name: 'name',
    description: 'description',
    date: new Date(),
    teacher_id: 12,
    users: [1,2],
  };
  const teacher: Teacher = {
    id: 12,
    lastName: 'Villanueva',
    firstName: 'Jane',
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  const sessionObservable: Observable<Session> = new Observable(observer => {observer.next(session)});
  const teacherObservable: Observable<Teacher> = new Observable(observer => {observer.next(teacher)});

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    }).compileComponents();

    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch session', () => {
      jest.spyOn(sessionApiService, 'detail').mockReturnValue(sessionObservable);
      jest.spyOn(teacherService, 'detail').mockReturnValue(teacherObservable);

      component.ngOnInit();

      expect(component.session).toEqual(session);
      expect(component.teacher).toEqual(teacher);
    });
  })

  describe('delete', () => {
    it('should delete session', () => {
      component.sessionId = 'session-id';

      jest.spyOn(sessionApiService, 'delete').mockImplementation(() => of({}));
      jest.spyOn(matSnackBar, 'open').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.delete();

      expect(sessionApiService.delete).toHaveBeenCalledWith('session-id');
      expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('participate', () => {
    it('should call the api service', () => {
      component.sessionId = 'session-id';

      jest.spyOn(sessionApiService, 'participate').mockImplementation(() => of(void 0));
      jest.spyOn(sessionApiService, 'detail').mockReturnValue(sessionObservable);
      jest.spyOn(teacherService, 'detail').mockReturnValue(teacherObservable);

      component.participate();

      expect(component.isParticipate).toBeTruthy();
    });
  });

  describe('unParticipate', () => {
    it('should call the api service', () => {
      component.sessionId = 'session-id';
      session.users = [2];

      jest.spyOn(sessionApiService, 'unParticipate').mockImplementation(() => of(void 0));
      jest.spyOn(sessionApiService, 'detail').mockReturnValue(sessionObservable);
      jest.spyOn(teacherService, 'detail').mockReturnValue(teacherObservable);

      component.unParticipate();

      expect(component.isParticipate).toBeFalsy();
    });
  })
});

