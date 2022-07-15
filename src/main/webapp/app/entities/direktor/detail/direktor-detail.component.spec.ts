import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DirektorDetailComponent } from './direktor-detail.component';

describe('Direktor Management Detail Component', () => {
  let comp: DirektorDetailComponent;
  let fixture: ComponentFixture<DirektorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DirektorDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ direktor: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DirektorDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DirektorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load direktor on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.direktor).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
