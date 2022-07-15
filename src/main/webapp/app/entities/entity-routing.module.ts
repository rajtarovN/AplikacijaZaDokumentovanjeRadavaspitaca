import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'korisnik',
        data: { pageTitle: 'diplomskiApp.korisnik.home.title' },
        loadChildren: () => import('./korisnik/korisnik.module').then(m => m.KorisnikModule),
      },
      {
        path: 'vaspitac',
        data: { pageTitle: 'diplomskiApp.vaspitac.home.title' },
        loadChildren: () => import('./vaspitac/vaspitac.module').then(m => m.VaspitacModule),
      },
      {
        path: 'direktor',
        data: { pageTitle: 'diplomskiApp.direktor.home.title' },
        loadChildren: () => import('./direktor/direktor.module').then(m => m.DirektorModule),
      },
      {
        path: 'pedagog',
        data: { pageTitle: 'diplomskiApp.pedagog.home.title' },
        loadChildren: () => import('./pedagog/pedagog.module').then(m => m.PedagogModule),
      },
      {
        path: 'objekat',
        data: { pageTitle: 'diplomskiApp.objekat.home.title' },
        loadChildren: () => import('./objekat/objekat.module').then(m => m.ObjekatModule),
      },
      {
        path: 'adresa',
        data: { pageTitle: 'diplomskiApp.adresa.home.title' },
        loadChildren: () => import('./adresa/adresa.module').then(m => m.AdresaModule),
      },
      {
        path: 'potreban-materijal',
        data: { pageTitle: 'diplomskiApp.potrebanMaterijal.home.title' },
        loadChildren: () => import('./potreban-materijal/potreban-materijal.module').then(m => m.PotrebanMaterijalModule),
      },
      {
        path: 'dnevnik',
        data: { pageTitle: 'diplomskiApp.dnevnik.home.title' },
        loadChildren: () => import('./dnevnik/dnevnik.module').then(m => m.DnevnikModule),
      },
      {
        path: 'grupa',
        data: { pageTitle: 'diplomskiApp.grupa.home.title' },
        loadChildren: () => import('./grupa/grupa.module').then(m => m.GrupaModule),
      },
      {
        path: 'roditelj',
        data: { pageTitle: 'diplomskiApp.roditelj.home.title' },
        loadChildren: () => import('./roditelj/roditelj.module').then(m => m.RoditeljModule),
      },
      {
        path: 'prica',
        data: { pageTitle: 'diplomskiApp.prica.home.title' },
        loadChildren: () => import('./prica/prica.module').then(m => m.PricaModule),
      },
      {
        path: 'plan-price',
        data: { pageTitle: 'diplomskiApp.planPrice.home.title' },
        loadChildren: () => import('./plan-price/plan-price.module').then(m => m.PlanPriceModule),
      },
      {
        path: 'konacna-prica',
        data: { pageTitle: 'diplomskiApp.konacnaPrica.home.title' },
        loadChildren: () => import('./konacna-prica/konacna-prica.module').then(m => m.KonacnaPricaModule),
      },
      {
        path: 'komentar-na-pricu',
        data: { pageTitle: 'diplomskiApp.komentarNaPricu.home.title' },
        loadChildren: () => import('./komentar-na-pricu/komentar-na-pricu.module').then(m => m.KomentarNaPricuModule),
      },
      {
        path: 'formular',
        data: { pageTitle: 'diplomskiApp.formular.home.title' },
        loadChildren: () => import('./formular/formular.module').then(m => m.FormularModule),
      },
      {
        path: 'podaci-o-roditeljima',
        data: { pageTitle: 'diplomskiApp.podaciORoditeljima.home.title' },
        loadChildren: () => import('./podaci-o-roditeljima/podaci-o-roditeljima.module').then(m => m.PodaciORoditeljimaModule),
      },
      {
        path: 'zapazanje-u-vezi-deteta',
        data: { pageTitle: 'diplomskiApp.zapazanjeUVeziDeteta.home.title' },
        loadChildren: () => import('./zapazanje-u-vezi-deteta/zapazanje-u-vezi-deteta.module').then(m => m.ZapazanjeUVeziDetetaModule),
      },
      {
        path: 'dete',
        data: { pageTitle: 'diplomskiApp.dete.home.title' },
        loadChildren: () => import('./dete/dete.module').then(m => m.DeteModule),
      },
      {
        path: 'ne-dolasci',
        data: { pageTitle: 'diplomskiApp.neDolasci.home.title' },
        loadChildren: () => import('./ne-dolasci/ne-dolasci.module').then(m => m.NeDolasciModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
