import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IVaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';

@Component({
  selector: 'jhi-vaspitac-detail',
  templateUrl: './vaspitac-detail.component.html',
})
export class VaspitacDetailComponent implements OnInit {
  vaspitac: IVaspitac | null = null;

  constructor(protected activatedRoute: ActivatedRoute, private router: Router, protected vaspitacService: VaspitacService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vaspitac }) => {
      this.vaspitac = vaspitac;
    });
  }

  previousState(): void {
    window.history.back();
  }
  pregledVazecegDnevnika(): void {
    localStorage.setItem('username', this.vaspitac!.user!.login!);
    this.router.navigate(['/prica']);
  }

  changeStatus(status: any): void {
    this.vaspitac!.status = status;
    this.vaspitacService.changeStatus(this.vaspitac?.id, status).subscribe(res => {
      // eslint-disable-next-line no-console
      console.log('uspeh', res); //todo
    });
  }
}
