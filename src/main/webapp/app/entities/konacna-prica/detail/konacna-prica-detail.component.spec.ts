import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KonacnaPricaDetailComponent } from './konacna-prica-detail.component';

describe('KonacnaPrica Management Detail Component', () => {
  let comp: KonacnaPricaDetailComponent;
  let fixture: ComponentFixture<KonacnaPricaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KonacnaPricaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ konacnaPrica: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KonacnaPricaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KonacnaPricaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load konacnaPrica on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.konacnaPrica).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
