import { IAdresa } from 'app/entities/adresa/adresa.model';
import { IPodaciORoditeljima } from 'app/entities/podaci-o-roditeljima/podaci-o-roditeljima.model';
import { IDete } from 'app/entities/dete/dete.model';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { StatusFormulara } from 'app/entities/enumerations/status-formulara.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';

export interface IFormular {
  id?: number;
  mesecUpisa?: number | null;
  jmbg?: string | null;
  datumRodjenja?: string | null;
  imeDeteta?: string | null;
  mestoRodjenja?: string | null;
  opstinaRodjenja?: string | null;
  drzava?: string | null;
  brDeceUPorodici?: number | null;
  zdravstveniProblemi?: string | null;
  zdravstveniUslovi?: string | null;
  statusFormulara?: StatusFormulara | null;
  tipGrupe?: TipGrupe | null;
  adresa?: IAdresa | null;
  podaciORoditeljimas?: IPodaciORoditeljima[] | null;
  dete?: IDete | null;
  roditelj?: IRoditelj | null;
}

export class DeteZaGrupuDTO {
  constructor(public id?: number, public imeDeteta?: string | null, public dodato?: boolean | null, public tipGrupe?: TipGrupe | null) {}
}

export class Formular implements IFormular {
  constructor(
    public id?: number,
    public mesecUpisa?: number | null,
    public jmbg?: string | null,
    public datumRodjenja?: string | null,
    public imeDeteta?: string | null,
    public mestoRodjenja?: string | null,
    public opstinaRodjenja?: string | null,
    public drzava?: string | null,
    public brDeceUPorodici?: number | null,
    public zdravstveniProblemi?: string | null,
    public zdravstveniUslovi?: string | null,
    public statusFormulara?: StatusFormulara | null,
    public tipGrupe?: TipGrupe | null,
    public adresa?: IAdresa | null,
    public podaciORoditeljimas?: IPodaciORoditeljima[] | null,
    public dete?: IDete | null,
    public roditelj?: IRoditelj | null
  ) {}
}

export function getFormularIdentifier(formular: IFormular): number | undefined {
  return formular.id;
}
