import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ObjekatDetailComponent } from './objekat-detail.component';

describe('Objekat Management Detail Component', () => {
  let comp: ObjekatDetailComponent;
  let fixture: ComponentFixture<ObjekatDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ObjekatDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ objekat: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ObjekatDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ObjekatDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load objekat on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.objekat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
