import { HttpClientModule } from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import {Router} from "@angular/router";
import {SessionService} from "./services/session.service";
import {Observable} from "rxjs";

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  describe('isLogged', () => {
    it('should call session service', () => {
      const isLoggedObservable: Observable<boolean> = new Observable(observer => {observer.next(true)});

      jest.spyOn(sessionService, '$isLogged').mockReturnValue(isLoggedObservable);

      component.$isLogged();

      expect(sessionService.$isLogged).toHaveBeenCalled();
    });
  });

  describe('logout', () => {
    it('should call session service and redirect', () => {
      jest.spyOn(sessionService, 'logOut').mockImplementation();
      jest.spyOn(router, 'navigate').mockImplementation();

      component.logout();

      expect(sessionService.logOut).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['']);
    })
  })
});
