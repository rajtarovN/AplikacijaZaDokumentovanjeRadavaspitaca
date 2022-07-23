import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-pisanje-plana',
  templateUrl: './pisanje-plana.component.html',
  styleUrls: ['./pisanje-plana.component.scss'],
})
export class PisanjePlanaComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planPrice }) => {
      // eslint-disable-next-line no-console
      console.log('aaa');
    });
  }
}
