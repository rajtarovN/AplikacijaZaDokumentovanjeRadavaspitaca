import { Component, OnInit } from '@angular/core';
import { ObjekatDTO } from '../entities/objekat/objekat.model';
import { ITEMS_PER_PAGE } from '../config/pagination.constants';
import { ObjekatService } from '../entities/objekat/service/objekat.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { VaspitacDTO } from '../entities/vaspitac/vaspitac.model';
import { VaspitacService } from '../entities/vaspitac/service/vaspitac.service';
import { User } from '../admin/user-management/user-management.model';

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
    const id = localStorage.getItem('objekat');
    this.vaspitacService.getByObjekat(id!).subscribe({
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

    let vaspitac1: VaspitacDTO;
    for (vaspitac1 of data!) {
      if (vaspitac1.user === undefined) {
        vaspitac1.user = new User();
        vaspitac1.user.firstName = 'Pera';
        vaspitac1.user.lastName = 'Peric';
      }
    }
    this.observable = data ?? [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
