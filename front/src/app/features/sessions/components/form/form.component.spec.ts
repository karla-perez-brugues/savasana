import {HttpClient, HttpClientModule} from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import {ActivatedRoute, Router} from "@angular/router";
import {Session} from "../../interfaces/session.interface";
import {Observable} from "rxjs";

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let activatedRoute: ActivatedRoute;
  let matSnackBar: MatSnackBar;
  let sessionApiService: SessionApiService;
  let router: Router;
  let httpClient: HttpClient;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const session: Session = {
    id: 1,
    name: 'name',
    description: 'description',
    date: new Date('2025-02-19 14:42:42'),
    teacher_id: 12,
    users: [1,2],
  };

  const sessionObservable: Observable<Session> = new Observable(observer => {observer.next(session)});

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    }).compileComponents();

    activatedRoute = TestBed.inject(ActivatedRoute);
    matSnackBar = TestBed.inject(MatSnackBar);
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    httpClient = TestBed.inject(HttpClient);

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should redirect to /sessions', () => {
      mockSessionService.sessionInformation.admin = false;

      jest.spyOn(router, 'navigate').mockImplementation();

      component.ngOnInit();

      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should init form from session', () => {
      // @ts-ignore: force this private property value for testing.
      router.currentUrlTree = router.parseUrl('/update/1');

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(sessionObservable);

      component.ngOnInit();

      expect(component.onUpdate).toBeTruthy();
    });
  });

  describe('submit', () => {
    it('should call create api', () => {
      component.sessionForm?.controls['name'].setValue('name');
      component.sessionForm?.controls['description'].setValue('description');
      component.sessionForm?.controls['teacher_id'].setValue(12);
      component.sessionForm?.controls['date'].setValue(new Date('2025-02-19 14:42:42'));

      jest.spyOn(httpClient, 'post').mockReturnValue(sessionObservable);
      jest.spyOn(matSnackBar, 'open').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.submit();

      expect(httpClient.post).toHaveBeenCalledWith('api/session', component.sessionForm?.value);
      expect(matSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should call update api', () => {
      // @ts-ignore: force this private property value for testing.
      router.currentUrlTree = router.parseUrl('/update/1');

      component.sessionForm?.controls['name'].setValue('name');
      component.sessionForm?.controls['description'].setValue('description');
      component.sessionForm?.controls['teacher_id'].setValue(12);
      component.sessionForm?.controls['date'].setValue(new Date('2025-02-19 14:42:42'));

      component.onUpdate = true;

      jest.spyOn(activatedRoute.snapshot.paramMap, 'get').mockReturnValue('1');
      jest.spyOn(httpClient, 'put').mockReturnValue(sessionObservable);
      jest.spyOn(matSnackBar, 'open').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.ngOnInit();
      component.submit();

      expect(httpClient.put).toHaveBeenCalledWith('api/session/1', component.sessionForm?.value);
      expect(matSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    })
  })
});
