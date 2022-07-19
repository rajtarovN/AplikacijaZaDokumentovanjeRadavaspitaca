import { Component, OnInit } from '@angular/core';
import { ProfilDetetaDTO } from '../../dete.model';
import { ActivatedRoute } from '@angular/router';
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

  constructor(protected activatedRoute: ActivatedRoute, protected deteService: DeteService) {}

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.activatedRoute.data.subscribe(({ dete }) => {
      // eslint-disable-next-line no-console
      console.log('aaa', dete, 'aaa');
      //this.dete = dete;
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
    // eslint-disable-next-line no-console
    console.log('asa', data, 'asa');
    const p1: PodaciORoditeljimaDTO = {
      id: 1,
      telefon: '03232',
      radniStatus: RadniStatus.NOV,
      firma: 'A1',
      ime: 'Vera',
      prezime: 'Veric',
      radnoVreme: 'aaa',
    };
    const li = [p1, p1, p1];
    this.dete!.roditelji = li;
  }

  protected onError(): void {
    alert('greska');
  }
}
