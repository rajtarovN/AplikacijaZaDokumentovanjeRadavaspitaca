import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DeteDetailComponent } from './dete-detail.component';

describe('Dete Management Detail Component', () => {
  let comp: DeteDetailComponent;
  let fixture: ComponentFixture<DeteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dete: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DeteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dete on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dete).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
