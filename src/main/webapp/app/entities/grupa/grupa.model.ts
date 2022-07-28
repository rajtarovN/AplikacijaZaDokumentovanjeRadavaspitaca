import { IDete } from 'app/entities/dete/dete.model';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';
import { TipGrupe } from 'app/entities/enumerations/tip-grupe.model';
import { DeteZaGrupuDTO } from '../formular/formular.model';
import { VaspitacDTO, VaspitacZaGrupuDTO } from '../vaspitac/vaspitac.model';
import dayjs from 'dayjs/esm';

export interface IGrupa {
  id?: number;
  tipGrupe?: TipGrupe | null;
  detes?: IDete[] | null;
  dnevnik?: IDnevnik | null;
}

export class Grupa implements IGrupa {
  constructor(public id?: number, public tipGrupe?: TipGrupe | null, public detes?: IDete[] | null, public dnevnik?: IDnevnik | null) {}
}

export class GrupaDTO {
  constructor(
    public tipGrupe: TipGrupe,
    public deca: DeteZaGrupuDTO[],
    public pocetakVazenja: dayjs.Dayjs,
    public krajVazenja: dayjs.Dayjs,
    public vaspitac: VaspitacZaGrupuDTO
  ) {}
}

export function getGrupaIdentifier(grupa: IGrupa): number | undefined {
  return grupa.id;
}
