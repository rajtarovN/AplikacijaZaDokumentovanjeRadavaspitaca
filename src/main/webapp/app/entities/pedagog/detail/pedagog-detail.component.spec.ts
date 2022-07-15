import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PedagogDetailComponent } from './pedagog-detail.component';

describe('Pedagog Management Detail Component', () => {
  let comp: PedagogDetailComponent;
  let fixture: ComponentFixture<PedagogDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PedagogDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pedagog: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PedagogDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PedagogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pedagog on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pedagog).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
