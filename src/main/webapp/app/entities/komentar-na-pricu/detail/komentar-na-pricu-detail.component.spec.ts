import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KomentarNaPricuDetailComponent } from './komentar-na-pricu-detail.component';

describe('KomentarNaPricu Management Detail Component', () => {
  let comp: KomentarNaPricuDetailComponent;
  let fixture: ComponentFixture<KomentarNaPricuDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KomentarNaPricuDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ komentarNaPricu: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(KomentarNaPricuDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(KomentarNaPricuDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load komentarNaPricu on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.komentarNaPricu).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
