import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user.interface";
import {Observable, of} from "rxjs";
import {SessionInformation} from "../../interfaces/sessionInformation.interface";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let router: Router;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let userService: UserService;

  const user: User = {
    id: 1,
    email: 'karla@mail.com',
    lastName: 'Pérez',
    firstName: 'Karla',
    admin: false,
    password: 'password',
    createdAt: new Date(),
  }

  const sessionInformation: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'karla@mail.com',
    firstName: 'Karla',
    lastName: 'Pérez',
    admin: true,
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [SessionService],
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    userService = TestBed.inject(UserService);

    sessionService.sessionInformation = sessionInformation;

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should get user by id', () => {
      const userObservable: Observable<User> = new Observable(observer =>  observer.next(user));

      jest.spyOn(userService, 'getById').mockReturnValue(userObservable);

      component.ngOnInit();

      expect(component.user).toEqual(user);
    });
  });

  describe('delete', () => {
    it('should delete the user', () => {
      jest.spyOn(userService, 'delete').mockImplementation(() => of({}));
      jest.spyOn(matSnackBar, 'open').mockImplementation();
      jest.spyOn(sessionService, 'logOut').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.delete();

      expect(matSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
      expect(sessionService.logOut).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/']);
    });
  });
});
