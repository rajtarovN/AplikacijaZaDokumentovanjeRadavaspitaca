import { Component, OnInit } from '@angular/core';
import { DeteDTO, NeDolasciDTO } from '../../entities/ne-dolasci/ne-dolasci.model';
import dayjs from 'dayjs/esm';
import { TipGrupe } from '../../entities/enumerations/tip-grupe.model';
import { DnevnikService } from '../../entities/dnevnik/service/dnevnik.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NeDolasciService } from '../../entities/ne-dolasci/service/ne-dolasci.service';
import { DodajRazlogComponent } from '../../entities/ne-dolasci/dodaj-razlog/dodaj-razlog.component';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { VaspitacService } from '../../entities/vaspitac/service/vaspitac.service';
import { DeteService } from '../../entities/dete/service/dete.service';
import { FormularService } from '../../entities/formular/service/formular.service';
import { DeteZaGrupuDTO } from '../../entities/formular/formular.model';
import { VaspitacZaGrupuDTO } from '../../entities/vaspitac/vaspitac.model';

@Component({
  selector: 'jhi-raspored-dece',
  templateUrl: './raspored-dece.component.html',
  styleUrls: ['./raspored-dece.component.scss'],
})
export class RasporedDeceComponent implements OnInit {
  deca: DeteZaGrupuDTO[];
  dodataDeca: DeteZaGrupuDTO[];
  predicate!: string;
  datumPocetka: dayjs.Dayjs | undefined;
  datumKraja: dayjs.Dayjs | undefined;
  tipGrupe?: TipGrupe | null;
  vaspitacDto: VaspitacZaGrupuDTO | undefined;
  listaVasapitaca: VaspitacZaGrupuDTO[] | undefined;
  ascending!: boolean;
  isLoading = false;

  panelOpenState = false;

  constructor(public vaspitacService: VaspitacService, protected modalService: NgbModal, public formularService: FormularService) {
    this.datumPocetka = dayjs();
    this.datumKraja = dayjs();
    this.dodataDeca = [];
    this.deca = [];
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

  sacuvaj(): void {
    // const listaIzostanaka = this.izostanci!.filter(x => x.odsutan);
    // this.nedolasciService.createNeDolasci(listaIzostanaka).subscribe({
    //   next: (res: HttpResponse<any>) => {
    //     this.onSuccess(res.body, res.headers);
    //   },
    //   error: () => {
    //     this.onError();
    //   },
    // });
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
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
    // eslint-disable-next-line no-console
    console.log(this.deca);

    this.vaspitacService.getImena().subscribe({
      next: (res: HttpResponse<VaspitacZaGrupuDTO[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
  }

  protected onSuccess(data: VaspitacZaGrupuDTO[] | null, headers: HttpHeaders): void {
    this.listaVasapitaca = [];
    // eslint-disable-next-line no-console
    console.log(this.deca);

    let i: VaspitacZaGrupuDTO;
    for (i of data!) {
      //todo id dnevnika
      this.listaVasapitaca.push(i);
    }
  }
  protected onGetDecaSuccess(data: DeteZaGrupuDTO[] | null, headers: HttpHeaders): void {
    this.deca = [];
    // eslint-disable-next-line no-console
    console.log(this.deca);

    let i: DeteZaGrupuDTO;
    for (i of data!) {
      //todo id dnevnika
      this.deca.push(i);
    }
  }

  protected onError(): void {
    // eslint-disable-next-line no-console
    console.log('aaa'); //todo
  }
}
