import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDete } from '../dete.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { DeteService } from '../service/dete.service';
import { DeteDeleteDialogComponent } from '../delete/dete-delete-dialog.component';
import { AccountService } from '../../../core/auth/account.service';
import { UserService } from '../../user/user.service';

@Component({
  selector: 'jhi-dete',
  templateUrl: './dete.component.html',
})
export class DeteComponent implements OnInit {
  detes?: IDete[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  username?: string;

  constructor(
    protected deteService: DeteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected userService: UserService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const grupa = localStorage.getItem('grupa');
    const vaspitac = localStorage.getItem('vaspitac');

    if (vaspitac) {
      // eslint-disable-next-line no-console
      console.log('a');
      this.deteService
        .queryDecaOfVaspitac(
          {
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
            username: this.username,
          },
          vaspitac
        )
        .subscribe({
          next: (res: HttpResponse<IDete[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onError();
          },
        });
    } else if (this.username) {
      // eslint-disable-next-line no-console
      console.log('b');
      this.deteService
        .queryDeteOfRoditelj(
          {
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          },
          this.username
        )
        .subscribe({
          next: (res: HttpResponse<IDete[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          error: () => {
            this.isLoading = false;
            this.onError();
          },
        });
    } else {
      // eslint-disable-next-line no-console
      console.log('c');
      this.deteService
        .queryByGrupa(
          {
            page: pageToLoad - 1,
            size: this.itemsPerPage,
            sort: this.sort(),
          },
          grupa!
        )
        .subscribe({
          next: (res: HttpResponse<IDete[]>) => {
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
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        // eslint-disable-next-line no-console
        console.log(account);
        if (account.authorities[0] === 'ROLE_RODITELJ') {
          this.username = account.login; //ovo bi trebalo da radi, //todo prilikom menjanja statusa formularu potrebno je da se napravi dete
        }
      }
      this.handleNavigation();
    });
  }

  trackId(_index: number, item: IDete): number {
    return item.id!;
  }

  delete(dete: IDete): void {
    const modalRef = this.modalService.open(DeteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dete = dete;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
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

  protected onSuccess(data: IDete[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/dete'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.detes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
