import { Component, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { TipGrupe } from '../../entities/enumerations/tip-grupe.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { VaspitacService } from '../../entities/vaspitac/service/vaspitac.service';
import { FormularService } from '../../entities/formular/service/formular.service';
import { DeteZaGrupuDTO } from '../../entities/formular/formular.model';
import { IVaspitac, VaspitacZaGrupuDTO } from '../../entities/vaspitac/vaspitac.model';
import { GrupaDTO } from '../../entities/grupa/grupa.model';
import { GrupaService } from '../../entities/grupa/service/grupa.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-raspored-dece',
  templateUrl: './raspored-dece.component.html',
  styleUrls: ['./raspored-dece.component.scss'],
})
export class RasporedDeceComponent implements OnInit {
  errorVaspitacHasDnevnik = false;
  success = false;
  deca: DeteZaGrupuDTO[];
  svaDeca: DeteZaGrupuDTO[];
  dodataDeca: DeteZaGrupuDTO[];
  predicate!: string;
  datumPocetka: dayjs.Dayjs;
  datumKraja: dayjs.Dayjs;
  tipGrupe: TipGrupe;
  vaspitacDto: VaspitacZaGrupuDTO | undefined;
  listaVasapitaca: VaspitacZaGrupuDTO[] | undefined;
  ascending!: boolean;
  isLoading = false;
  tipGrupeValues = Object.keys(TipGrupe);

  editForm = this.fb.group({
    pocetakVazenja: [],
    krajVazenja: [],
    vaspitacs: [],
    tipGrupe: [],
  });
  panelOpenState = false;

  constructor(
    public vaspitacService: VaspitacService,
    protected modalService: NgbModal,
    public formularService: FormularService,
    public grupaService: GrupaService,
    protected fb: FormBuilder
  ) {
    this.datumPocetka = dayjs();
    this.datumKraja = dayjs();
    this.dodataDeca = [];
    this.svaDeca = [];
    this.deca = [];
    this.tipGrupe = TipGrupe.POLUDNEVNA;
  }

  ngOnInit(): void {
    this.datumPocetka = dayjs();
    this.datumKraja = dayjs();
    this.loadPage();
    this.dodataDeca = [];
    this.deca = [];
  }
  trackId(_index: number, item: DeteZaGrupuDTO): number {
    return item.id!;
  }
  onChange(): void {
    // eslint-disable-next-line no-console
    console.log(this.editForm.get('tipGrupe')?.value);
    const odabranaGrupa = this.editForm.get('tipGrupe')?.value;
    this.tipGrupe = odabranaGrupa;
    this.deca = [];
    this.dodataDeca = [];
    //ovde menjam todo i pazi na null
    if (odabranaGrupa === null) {
      this.deca = this.svaDeca;
    } else {
      this.svaDeca.forEach((element, index) => {
        if (element.tipGrupe === odabranaGrupa) {
          this.deca.push(element);
        }
      });
    }
  }

  save(): void {
    const newGrupa = new GrupaDTO(
      this.tipGrupe,
      this.dodataDeca,
      this.datumPocetka,
      this.datumKraja,
      this.editForm.get(['vaspitacs'])!.value[0]
    );
    this.success = false;
    this.errorVaspitacHasDnevnik = false;
    this.grupaService.createGrupa(newGrupa).subscribe({
      next: (res: HttpResponse<any>) => {
        this.onCreateSuccess(res.body, res.headers);
      },
      error: response => {
        this.onError(response);
      },
    });
  }
  addDeteToGroup(dete1: DeteZaGrupuDTO): void {
    const dete: DeteZaGrupuDTO = this.deca.filter(elem => elem.id === dete1.id)[0];
    this.dodataDeca.push(dete);
    this.deca.forEach((element, index) => {
      if (element.id === dete1.id) {
        this.deca.splice(index, 1);
      }
      //delete this.deca[index];}
    });
  }
  removeFromGrupa(id: number | undefined): void {
    const dete: DeteZaGrupuDTO = this.dodataDeca.filter(elem => elem.id === id)[0];
    this.deca.push(dete);
    this.dodataDeca.forEach((element, index) => {
      if (element.id === id) {
        this.dodataDeca.splice(index, 1);
      }
    });
  }

  loadPage(): void {
    this.isLoading = true;
    this.formularService.getDecaZaRaspored().subscribe({
      next: (res: HttpResponse<DeteZaGrupuDTO[]>) => {
        this.isLoading = false;
        this.onGetDecaSuccess(res.body, res.headers);
      },
      error: response => {
        this.isLoading = false;
        this.onError(response);
      },
    });

    this.vaspitacService.getImena().subscribe({
      next: (res: HttpResponse<VaspitacZaGrupuDTO[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers);
      },
      error: response => {
        this.isLoading = false;
        this.onError(response);
      },
    });
  }

  getSelectedVaspitac(option: VaspitacZaGrupuDTO, selectedVals?: VaspitacZaGrupuDTO[]): VaspitacZaGrupuDTO {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }
  protected onSuccess(data: VaspitacZaGrupuDTO[] | null, headers: HttpHeaders): void {
    this.listaVasapitaca = [];

    let i: VaspitacZaGrupuDTO;
    for (i of data!) {
      //todo id dnevnika
      this.listaVasapitaca.push(i);
    }
  }
  protected onGetDecaSuccess(data: DeteZaGrupuDTO[] | null, headers: HttpHeaders): void {
    this.deca = [];
    let i: DeteZaGrupuDTO;
    for (i of data!) {
      //todo id dnevnika
      this.deca.push(i);
      this.svaDeca.push(i);
    }
  }

  private onError(response: HttpErrorResponse): void {
    if (response.status === 400) {
      this.errorVaspitacHasDnevnik = true;
    }
  }

  private onCreateSuccess(body: any | null, headers: HttpHeaders): void {
    this.success = true;
  }
}
