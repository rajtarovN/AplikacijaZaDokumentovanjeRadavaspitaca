import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { KorisnikService } from '../service/korisnik.service';
import { IKorisnik, Korisnik } from '../korisnik.model';

import { KorisnikUpdateComponent } from './korisnik-update.component';

describe('Korisnik Management Update Component', () => {
  let comp: KorisnikUpdateComponent;
  let fixture: ComponentFixture<KorisnikUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let korisnikService: KorisnikService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [KorisnikUpdateComponent],
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
      .overrideTemplate(KorisnikUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(KorisnikUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    korisnikService = TestBed.inject(KorisnikService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const korisnik: IKorisnik = { id: 456 };

      activatedRoute.data = of({ korisnik });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(korisnik));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Korisnik>>();
      const korisnik = { id: 123 };
      jest.spyOn(korisnikService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ korisnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: korisnik }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(korisnikService.update).toHaveBeenCalledWith(korisnik);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Korisnik>>();
      const korisnik = new Korisnik();
      jest.spyOn(korisnikService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ korisnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: korisnik }));
      saveSubject.complete();

      // THEN
      expect(korisnikService.create).toHaveBeenCalledWith(korisnik);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Korisnik>>();
      const korisnik = { id: 123 };
      jest.spyOn(korisnikService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ korisnik });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(korisnikService.update).toHaveBeenCalledWith(korisnik);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
