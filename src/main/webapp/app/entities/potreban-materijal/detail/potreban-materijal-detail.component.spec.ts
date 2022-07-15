import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PotrebanMaterijalDetailComponent } from './potreban-materijal-detail.component';

describe('PotrebanMaterijal Management Detail Component', () => {
  let comp: PotrebanMaterijalDetailComponent;
  let fixture: ComponentFixture<PotrebanMaterijalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PotrebanMaterijalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ potrebanMaterijal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PotrebanMaterijalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PotrebanMaterijalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load potrebanMaterijal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.potrebanMaterijal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
