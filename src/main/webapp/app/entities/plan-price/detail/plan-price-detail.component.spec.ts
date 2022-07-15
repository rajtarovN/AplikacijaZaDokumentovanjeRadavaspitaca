import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlanPriceDetailComponent } from './plan-price-detail.component';

describe('PlanPrice Management Detail Component', () => {
  let comp: PlanPriceDetailComponent;
  let fixture: ComponentFixture<PlanPriceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlanPriceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ planPrice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlanPriceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlanPriceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load planPrice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.planPrice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
