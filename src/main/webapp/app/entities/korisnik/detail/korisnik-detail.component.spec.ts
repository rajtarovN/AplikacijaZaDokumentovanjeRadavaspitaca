import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KorisnikDetailComponent } from './korisnik-detail.component';

describe('Korisnik Management Detail Component', () => {
  let comp: KorisnikDetailComponent;
  let fixture: ComponentFixture<KorisnikDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KorisnikDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ korisnik: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KorisnikDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KorisnikDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load korisnik on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.korisnik).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
