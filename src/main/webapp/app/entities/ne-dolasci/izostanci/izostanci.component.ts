import { Component, OnInit } from '@angular/core';
import { DeteDTO, INeDolasci, NeDolasciDTO } from '../ne-dolasci.model';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { DnevnikService } from '../../dnevnik/service/dnevnik.service';
import { ASC, DESC } from '../../../config/pagination.constants';
import dayjs from 'dayjs/esm';
import { NeDolasciDeleteDialogComponent } from '../delete/ne-dolasci-delete-dialog.component';
import { DodajRazlogComponent } from '../dodaj-razlog/dodaj-razlog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NeDolasciService } from '../service/ne-dolasci.service';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-izostanci',
  templateUrl: './izostanci.component.html',
  styleUrls: ['./izostanci.component.scss'],
})
export class IzostanciComponent implements OnInit {
  izostanci?: NeDolasciDTO[];
  predicate!: string;
  datum: dayjs.Dayjs;
  ascending!: boolean;
  isLoading = false;

  constructor(
    public dnevnikService: DnevnikService,
    protected modalService: NgbModal,
    public nedolasciService: NeDolasciService,
    protected accountService: AccountService
  ) {
    this.datum = dayjs();
  }

  ngOnInit(): void {
    this.datum = dayjs();
    this.loadPage();
  }
  trackId(_index: number, item: NeDolasciDTO): number {
    return item.id!;
  }
  dodajRazlog(nedolazak: NeDolasciDTO): void {
    const modalRef = this.modalService.open(DodajRazlogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.neDolasci = nedolazak;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe((reason: string | null) => {
      if (reason !== '') {
        nedolazak.razlog = reason;
      }
    });
  }
  promeniOdsutnost(id: number): void {
    const izostanak = this.izostanci!.filter(x => x.idDeteta === id)[0];
    izostanak.odsutan = !izostanak.odsutan;
  }
  sacuvaj(): void {
    const listaIzostanaka = this.izostanci!.filter(x => x.odsutan);
    this.nedolasciService.createNeDolasci(listaIzostanaka).subscribe({
      next: (res: HttpResponse<any>) => {
        this.onSuccess(res.body);
      },
      error: () => {
        this.onError();
      },
    });
  }

  loadPage(): void {
    this.isLoading = true;
    let username = '';
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        if (account.authorities[0] === 'ROLE_VASPITAC') {
          username = account.login;
        }
      }
    });
    this.dnevnikService.getDeca(username).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        this.onSuccess(res);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
  }

  protected onSuccess(data: any): void {
    this.izostanci = [];
    let i: any;
    for (i of data) {
      this.izostanci.push(new NeDolasciDTO(this.datum, '', i.formular.imeDeteta, i.id!, 1, false, null));
    }
  }

  protected onError(): void {
    // eslint-disable-next-line no-console
    console.log('aaa'); //todo
  }
}
