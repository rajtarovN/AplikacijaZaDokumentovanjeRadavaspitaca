import { Component, OnInit } from '@angular/core';
import { IZapazanjeUVeziDeteta } from '../../zapazanje-u-vezi-deteta.model';
import { ITEMS_PER_PAGE } from '../../../../config/pagination.constants';
import { ObjekatService } from '../../../objekat/service/objekat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ObjekatDTO } from '../../../objekat/objekat.model';
import { ZapazanjeUVeziDetetaService } from '../../service/zapazanje-u-vezi-deteta.service';

@Component({
  selector: 'jhi-pregled-zapazanja',
  templateUrl: './pregled-zapazanja.component.html',
  styleUrls: ['./pregled-zapazanja.component.scss'],
})
export class PregledZapazanjaComponent implements OnInit {
  zapazanjeUVeziDeteta: IZapazanjeUVeziDeteta | null = null;
  observable?: IZapazanjeUVeziDeteta[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected zapazanjeUVeziDetetaService: ZapazanjeUVeziDetetaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  previousState(): void {
    window.history.back();
  }
  changeZapazanje(zapazanje: any): void {
    this.zapazanjeUVeziDeteta = zapazanje;
  }
  loadPage(): void {
    this.isLoading = true;

    this.zapazanjeUVeziDetetaService.query().subscribe({
      next: (res: HttpResponse<any>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
  }

  ngOnInit(): void {
    this.loadPage();
  }

  protected onSuccess(data: ObjekatDTO[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.observable = data ?? [];
    // eslint-disable-next-line no-console
    console.log('aaa', this.observable, 'aaa');
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
