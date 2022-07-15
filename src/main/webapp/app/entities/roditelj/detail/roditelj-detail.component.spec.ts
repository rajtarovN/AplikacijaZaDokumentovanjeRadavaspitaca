import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoditeljDetailComponent } from './roditelj-detail.component';

describe('Roditelj Management Detail Component', () => {
  let comp: RoditeljDetailComponent;
  let fixture: ComponentFixture<RoditeljDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoditeljDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ roditelj: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RoditeljDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RoditeljDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load roditelj on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.roditelj).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
