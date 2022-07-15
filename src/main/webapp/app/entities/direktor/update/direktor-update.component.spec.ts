import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DirektorService } from '../service/direktor.service';
import { IDirektor, Direktor } from '../direktor.model';

import { DirektorUpdateComponent } from './direktor-update.component';

describe('Direktor Management Update Component', () => {
  let comp: DirektorUpdateComponent;
  let fixture: ComponentFixture<DirektorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let direktorService: DirektorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DirektorUpdateComponent],
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
      .overrideTemplate(DirektorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DirektorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    direktorService = TestBed.inject(DirektorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const direktor: IDirektor = { id: 456 };

      activatedRoute.data = of({ direktor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(direktor));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direktor>>();
      const direktor = { id: 123 };
      jest.spyOn(direktorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direktor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direktor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(direktorService.update).toHaveBeenCalledWith(direktor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direktor>>();
      const direktor = new Direktor();
      jest.spyOn(direktorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direktor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direktor }));
      saveSubject.complete();

      // THEN
      expect(direktorService.create).toHaveBeenCalledWith(direktor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Direktor>>();
      const direktor = { id: 123 };
      jest.spyOn(direktorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direktor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(direktorService.update).toHaveBeenCalledWith(direktor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
