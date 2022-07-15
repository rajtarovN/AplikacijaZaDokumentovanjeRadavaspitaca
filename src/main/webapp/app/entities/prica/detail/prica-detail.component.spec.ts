import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PricaDetailComponent } from './prica-detail.component';

describe('Prica Management Detail Component', () => {
  let comp: PricaDetailComponent;
  let fixture: ComponentFixture<PricaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PricaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prica: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PricaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PricaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prica on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prica).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
