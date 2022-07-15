import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NeDolasciDetailComponent } from './ne-dolasci-detail.component';

describe('NeDolasci Management Detail Component', () => {
  let comp: NeDolasciDetailComponent;
  let fixture: ComponentFixture<NeDolasciDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeDolasciDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ neDolasci: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NeDolasciDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NeDolasciDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load neDolasci on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.neDolasci).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
