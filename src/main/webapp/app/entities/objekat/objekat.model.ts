import { IAdresa } from 'app/entities/adresa/adresa.model';
import { IPotrebanMaterijal } from 'app/entities/potreban-materijal/potreban-materijal.model';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';

export interface IObjekat {
  opis?: string | null;
  id?: number;
  adresa?: IAdresa | null;
  potrebanMaterijals?: IPotrebanMaterijal[] | null;
  vaspitacs?: IVaspitac[] | null;
}

export class Objekat implements IObjekat {
  constructor(
    public opis?: string | null,
    public id?: number,
    public adresa?: IAdresa | null,
    public potrebanMaterijals?: IPotrebanMaterijal[] | null,
    public vaspitacs?: IVaspitac[] | null
  ) {}
}

export function getObjekatIdentifier(objekat: IObjekat): number | undefined {
  return objekat.id;
}
