import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GrupaDetailComponent } from './grupa-detail.component';

describe('Grupa Management Detail Component', () => {
  let comp: GrupaDetailComponent;
  let fixture: ComponentFixture<GrupaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GrupaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ grupa: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GrupaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GrupaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load grupa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.grupa).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
