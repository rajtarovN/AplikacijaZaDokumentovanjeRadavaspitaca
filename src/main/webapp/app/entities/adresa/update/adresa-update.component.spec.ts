import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AdresaService } from '../service/adresa.service';
import { IAdresa, Adresa } from '../adresa.model';

import { AdresaUpdateComponent } from './adresa-update.component';

describe('Adresa Management Update Component', () => {
  let comp: AdresaUpdateComponent;
  let fixture: ComponentFixture<AdresaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adresaService: AdresaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AdresaUpdateComponent],
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
      .overrideTemplate(AdresaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdresaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adresaService = TestBed.inject(AdresaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const adresa: IAdresa = { id: 456 };

      activatedRoute.data = of({ adresa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(adresa));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Adresa>>();
      const adresa = { id: 123 };
      jest.spyOn(adresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adresa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(adresaService.update).toHaveBeenCalledWith(adresa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Adresa>>();
      const adresa = new Adresa();
      jest.spyOn(adresaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adresa }));
      saveSubject.complete();

      // THEN
      expect(adresaService.create).toHaveBeenCalledWith(adresa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Adresa>>();
      const adresa = { id: 123 };
      jest.spyOn(adresaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adresa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adresaService.update).toHaveBeenCalledWith(adresa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
