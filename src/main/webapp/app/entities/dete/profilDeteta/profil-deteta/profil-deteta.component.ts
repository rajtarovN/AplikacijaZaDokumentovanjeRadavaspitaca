import { Component, OnInit } from '@angular/core';
import { ProfilDetetaDTO } from '../../dete.model';
import { ActivatedRoute, Router } from '@angular/router';
import { DeteService } from '../../service/dete.service';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PodaciORoditeljimaDTO } from '../../../podaci-o-roditeljima/podaci-o-roditeljima.model';
import { RadniStatus } from '../../../enumerations/radni-status.model';

@Component({
  selector: 'jhi-profil-deteta',
  templateUrl: './profil-deteta.component.html',
  styleUrls: ['./profil-deteta.component.scss'],
})
export class ProfilDetetaComponent implements OnInit {
  isLoading = false;
  zvati = '';
  dete: ProfilDetetaDTO | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected deteService: DeteService, private router: Router) {}

  previousState(): void {
    window.history.back();
  }
  viewZapazanja(id: number): void {
    localStorage.setItem('dete', String(id));
    this.router.navigate(['/zapazanje-u-vezi-deteta', id, 'pregled']);
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.activatedRoute.data.subscribe(({ dete }) => {
      this.deteService.getprofile(dete.id).subscribe({
        next: (res: HttpResponse<any>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
    });
  }

  protected onSuccess(data: ProfilDetetaDTO | null, headers: HttpHeaders): void {
    this.dete = data ?? null;
  }

  protected onError(): void {
    alert('greska');
  }
}
