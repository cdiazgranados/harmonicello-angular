import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PresetsFormService } from './presets-form.service';
import { PresetsService } from '../service/presets.service';
import { IPresets } from '../presets.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PresetsUpdateComponent } from './presets-update.component';

describe('Presets Management Update Component', () => {
  let comp: PresetsUpdateComponent;
  let fixture: ComponentFixture<PresetsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let presetsFormService: PresetsFormService;
  let presetsService: PresetsService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PresetsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PresetsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PresetsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    presetsFormService = TestBed.inject(PresetsFormService);
    presetsService = TestBed.inject(PresetsService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const presets: IPresets = { id: 456 };
      const user: IUser = { id: 22398 };
      presets.user = user;

      const userCollection: IUser[] = [{ id: 21965 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ presets });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const presets: IPresets = { id: 456 };
      const user: IUser = { id: 26582 };
      presets.user = user;

      activatedRoute.data = of({ presets });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.presets).toEqual(presets);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresets>>();
      const presets = { id: 123 };
      jest.spyOn(presetsFormService, 'getPresets').mockReturnValue(presets);
      jest.spyOn(presetsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presets }));
      saveSubject.complete();

      // THEN
      expect(presetsFormService.getPresets).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(presetsService.update).toHaveBeenCalledWith(expect.objectContaining(presets));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresets>>();
      const presets = { id: 123 };
      jest.spyOn(presetsFormService, 'getPresets').mockReturnValue({ id: null });
      jest.spyOn(presetsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presets: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: presets }));
      saveSubject.complete();

      // THEN
      expect(presetsFormService.getPresets).toHaveBeenCalled();
      expect(presetsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPresets>>();
      const presets = { id: 123 };
      jest.spyOn(presetsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ presets });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(presetsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
