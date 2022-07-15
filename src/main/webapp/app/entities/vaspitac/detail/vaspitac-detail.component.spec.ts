import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VaspitacDetailComponent } from './vaspitac-detail.component';

describe('Vaspitac Management Detail Component', () => {
  let comp: VaspitacDetailComponent;
  let fixture: ComponentFixture<VaspitacDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VaspitacDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vaspitac: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VaspitacDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VaspitacDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vaspitac on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vaspitac).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
