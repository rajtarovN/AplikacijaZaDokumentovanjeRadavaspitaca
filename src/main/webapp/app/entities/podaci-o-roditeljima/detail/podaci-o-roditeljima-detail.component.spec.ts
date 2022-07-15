import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PodaciORoditeljimaDetailComponent } from './podaci-o-roditeljima-detail.component';

describe('PodaciORoditeljima Management Detail Component', () => {
  let comp: PodaciORoditeljimaDetailComponent;
  let fixture: ComponentFixture<PodaciORoditeljimaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PodaciORoditeljimaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ podaciORoditeljima: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PodaciORoditeljimaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PodaciORoditeljimaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load podaciORoditeljima on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.podaciORoditeljima).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
