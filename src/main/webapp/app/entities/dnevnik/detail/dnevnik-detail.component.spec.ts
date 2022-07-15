import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DnevnikDetailComponent } from './dnevnik-detail.component';

describe('Dnevnik Management Detail Component', () => {
  let comp: DnevnikDetailComponent;
  let fixture: ComponentFixture<DnevnikDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DnevnikDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dnevnik: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DnevnikDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DnevnikDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dnevnik on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dnevnik).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
