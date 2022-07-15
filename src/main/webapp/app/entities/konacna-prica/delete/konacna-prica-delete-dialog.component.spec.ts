jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { KonacnaPricaService } from '../service/konacna-prica.service';

import { KonacnaPricaDeleteDialogComponent } from './konacna-prica-delete-dialog.component';

describe('KonacnaPrica Management Delete Component', () => {
  let comp: KonacnaPricaDeleteDialogComponent;
  let fixture: ComponentFixture<KonacnaPricaDeleteDialogComponent>;
  let service: KonacnaPricaService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [KonacnaPricaDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(KonacnaPricaDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KonacnaPricaDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(KonacnaPricaService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
