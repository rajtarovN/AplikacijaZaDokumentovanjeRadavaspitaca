import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrica } from '../prica.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { PricaService } from '../service/prica.service';
import { PricaDeleteDialogComponent } from '../delete/prica-delete-dialog.component';
import { Dayjs } from 'dayjs';
import { KonacnaPricaService } from '../../konacna-prica/service/konacna-prica.service';

@Component({
  selector: 'jhi-prica',
  templateUrl: './prica.component.html',
})
export class PricaComponent implements OnInit {
  pricas?: IPrica[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  today: Date;

  constructor(
    protected pricaService: PricaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected konacnaPricaService: KonacnaPricaService
  ) {
    this.today = new Date();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    this.today = new Date();
    const pageToLoad: number = page ?? this.page ?? 1;
    const username = localStorage.getItem('username');
    const dnevnik = localStorage.getItem('dnevnik');
    if (dnevnik) {
      this.pricaService
        .queryOldDnevnik(
          {
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          },
          dnevnik
        )
        .subscribe({
          next: (res: HttpResponse<IPrica[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onError();
          },
        });
    } else if (username) {
      this.pricaService
        .queryCurrentDnevnik(
          {
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          },
          username
        )
        .subscribe({
          next: (res: HttpResponse<IPrica[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onError();
          },
        });
    } else {
      this.pricaService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe({
          next: (res: HttpResponse<IPrica[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onError();
          },
        });
    }
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(_index: number, item: IPrica): number {
    return item.id!;
  }

  delete(prica: IPrica): void {
    const modalRef = this.modalService.open(PricaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prica = prica;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }
  pisiPricu(prica: IPrica): void {
    localStorage.setItem('idPrice', String(prica.id));

    if (prica.konacnaPrica !== null) {
      localStorage.setItem('prica', prica.konacnaPrica!.tekst!);
    } else {
      const id = localStorage.getItem('idPrice');
      this.konacnaPricaService.getPocetnaPrica(id!).subscribe({
        next: (res: any) => localStorage.setItem('prica', res.body.tekst!),
        error: () => this.onError(),
      });
    }

    this.router.navigate(['/konacna-prica/editor']);
  }

  viewKonacnaPrica(id: number, idKonacna: number): void {
    localStorage.setItem('idPrice', String(id));
    this.router.navigate(['/konacna-prica/' + String(idKonacna) + '/view']);
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPrica[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/prica'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.pricas = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
