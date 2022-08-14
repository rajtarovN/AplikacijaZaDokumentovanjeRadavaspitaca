import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ObjekatDTO } from '../../app/entities/objekat/objekat.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { ObjekatService } from '../../app/entities/objekat/service/objekat.service';
import { ObjekatDeleteDialogComponent } from '../entities/objekat/delete/objekat-delete-dialog.component';

@Component({
  selector: 'jhi-pregled-objekata',
  templateUrl: './pregled-objekata.component.html',
  styleUrls: ['./pregled-objekata.component.scss'],
})
export class PregledObjekataComponent implements OnInit {
  observable?: ObjekatDTO[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected objekatService: ObjekatService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(): void {
    this.isLoading = true;

    this.objekatService.getAll().subscribe({
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

  setObjekat(id: number): void {
    localStorage.setItem('objekat', String(id));
    this.router.navigateByUrl('/pregled-vaspitaca');
  }

  protected onSuccess(data: ObjekatDTO[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.observable = data ?? [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
