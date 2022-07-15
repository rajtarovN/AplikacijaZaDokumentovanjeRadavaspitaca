import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ZapazanjeUVeziDetetaDetailComponent } from './zapazanje-u-vezi-deteta-detail.component';

describe('ZapazanjeUVeziDeteta Management Detail Component', () => {
  let comp: ZapazanjeUVeziDetetaDetailComponent;
  let fixture: ComponentFixture<ZapazanjeUVeziDetetaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ZapazanjeUVeziDetetaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ zapazanjeUVeziDeteta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ZapazanjeUVeziDetetaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ZapazanjeUVeziDetetaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load zapazanjeUVeziDeteta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.zapazanjeUVeziDeteta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
