import { Component, OnInit } from '@angular/core';
import { ObjekatDTO } from '../entities/objekat/objekat.model';
import { ITEMS_PER_PAGE } from '../config/pagination.constants';
import { ObjekatService } from '../entities/objekat/service/objekat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { VaspitacDTO } from '../entities/vaspitac/vaspitac.model';
import { VaspitacService } from '../entities/vaspitac/service/vaspitac.service';

@Component({
  selector: 'jhi-pregled-vaspitaca',
  templateUrl: './pregled-vaspitaca.component.html',
  styleUrls: ['./pregled-vaspitaca.component.scss'],
})
export class PregledVaspitacaComponent implements OnInit {
  observable?: VaspitacDTO[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected vaspitacService: VaspitacService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.isLoading = true;

    this.vaspitacService.getByObjekat().subscribe({
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

  protected onSuccess(data: VaspitacDTO[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.observable = data ?? [];
    // eslint-disable-next-line no-console
    console.log('aaa', data, 'aaa');
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
