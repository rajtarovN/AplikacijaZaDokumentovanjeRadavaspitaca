import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PedagogService } from '../service/pedagog.service';
import { IPedagog, Pedagog } from '../pedagog.model';

import { PedagogUpdateComponent } from './pedagog-update.component';

describe('Pedagog Management Update Component', () => {
  let comp: PedagogUpdateComponent;
  let fixture: ComponentFixture<PedagogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pedagogService: PedagogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PedagogUpdateComponent],
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
      .overrideTemplate(PedagogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PedagogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pedagogService = TestBed.inject(PedagogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pedagog: IPedagog = { id: 456 };

      activatedRoute.data = of({ pedagog });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(pedagog));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedagog>>();
      const pedagog = { id: 123 };
      jest.spyOn(pedagogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedagog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedagog }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pedagogService.update).toHaveBeenCalledWith(pedagog);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedagog>>();
      const pedagog = new Pedagog();
      jest.spyOn(pedagogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedagog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pedagog }));
      saveSubject.complete();

      // THEN
      expect(pedagogService.create).toHaveBeenCalledWith(pedagog);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Pedagog>>();
      const pedagog = { id: 123 };
      jest.spyOn(pedagogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pedagog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pedagogService.update).toHaveBeenCalledWith(pedagog);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
