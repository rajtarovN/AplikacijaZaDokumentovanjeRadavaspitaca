import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KonacnaPricaService } from '../service/konacna-prica.service';
import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';

import { KonacnaPricaUpdateComponent } from './konacna-prica-update.component';

describe('KonacnaPrica Management Update Component', () => {
  let comp: KonacnaPricaUpdateComponent;
  let fixture: ComponentFixture<KonacnaPricaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let konacnaPricaService: KonacnaPricaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KonacnaPricaUpdateComponent],
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
      .overrideTemplate(KonacnaPricaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KonacnaPricaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    konacnaPricaService = TestBed.inject(KonacnaPricaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const konacnaPrica: IKonacnaPrica = { id: 456 };

      activatedRoute.data = of({ konacnaPrica });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(konacnaPrica));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KonacnaPrica>>();
      const konacnaPrica = { id: 123 };
      jest.spyOn(konacnaPricaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ konacnaPrica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: konacnaPrica }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(konacnaPricaService.update).toHaveBeenCalledWith(konacnaPrica);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KonacnaPrica>>();
      const konacnaPrica = new KonacnaPrica();
      jest.spyOn(konacnaPricaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ konacnaPrica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: konacnaPrica }));
      saveSubject.complete();

      // THEN
      expect(konacnaPricaService.create).toHaveBeenCalledWith(konacnaPrica);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<KonacnaPrica>>();
      const konacnaPrica = { id: 123 };
      jest.spyOn(konacnaPricaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ konacnaPrica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(konacnaPricaService.update).toHaveBeenCalledWith(konacnaPrica);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
