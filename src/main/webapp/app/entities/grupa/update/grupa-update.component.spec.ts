import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GrupaService } from '../service/grupa.service';
import { IGrupa, Grupa } from '../grupa.model';

import { GrupaUpdateComponent } from './grupa-update.component';

describe('Grupa Management Update Component', () => {
  let comp: GrupaUpdateComponent;
  let fixture: ComponentFixture<GrupaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let grupaService: GrupaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GrupaUpdateComponent],
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
      .overrideTemplate(GrupaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GrupaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    grupaService = TestBed.inject(GrupaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const grupa: IGrupa = { id: 456 };

      activatedRoute.data = of({ grupa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(grupa));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Grupa>>();
      const grupa = { id: 123 };
      jest.spyOn(grupaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grupa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(grupaService.update).toHaveBeenCalledWith(grupa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Grupa>>();
      const grupa = new Grupa();
      jest.spyOn(grupaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: grupa }));
      saveSubject.complete();

      // THEN
      expect(grupaService.create).toHaveBeenCalledWith(grupa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Grupa>>();
      const grupa = { id: 123 };
      jest.spyOn(grupaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ grupa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(grupaService.update).toHaveBeenCalledWith(grupa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
