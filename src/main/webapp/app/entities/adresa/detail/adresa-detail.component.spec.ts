import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdresaDetailComponent } from './adresa-detail.component';

describe('Adresa Management Detail Component', () => {
  let comp: AdresaDetailComponent;
  let fixture: ComponentFixture<AdresaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdresaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ adresa: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AdresaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AdresaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load adresa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.adresa).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
