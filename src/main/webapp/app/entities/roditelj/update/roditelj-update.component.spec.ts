import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoditeljService } from '../service/roditelj.service';
import { IRoditelj, Roditelj } from '../roditelj.model';

import { RoditeljUpdateComponent } from './roditelj-update.component';

describe('Roditelj Management Update Component', () => {
  let comp: RoditeljUpdateComponent;
  let fixture: ComponentFixture<RoditeljUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roditeljService: RoditeljService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoditeljUpdateComponent],
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
      .overrideTemplate(RoditeljUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoditeljUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roditeljService = TestBed.inject(RoditeljService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const roditelj: IRoditelj = { id: 456 };

      activatedRoute.data = of({ roditelj });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(roditelj));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Roditelj>>();
      const roditelj = { id: 123 };
      jest.spyOn(roditeljService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roditelj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roditelj }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(roditeljService.update).toHaveBeenCalledWith(roditelj);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Roditelj>>();
      const roditelj = new Roditelj();
      jest.spyOn(roditeljService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roditelj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roditelj }));
      saveSubject.complete();

      // THEN
      expect(roditeljService.create).toHaveBeenCalledWith(roditelj);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Roditelj>>();
      const roditelj = { id: 123 };
      jest.spyOn(roditeljService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roditelj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roditeljService.update).toHaveBeenCalledWith(roditelj);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
