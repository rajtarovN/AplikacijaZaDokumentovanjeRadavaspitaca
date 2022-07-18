import { AdresaDTO, IAdresa } from 'app/entities/adresa/adresa.model';
import { IPotrebanMaterijal } from 'app/entities/potreban-materijal/potreban-materijal.model';
import { IVaspitac } from 'app/entities/vaspitac/vaspitac.model';

export interface IObjekat {
  opis?: string | null;
  naziv?: string | null;
  id?: number;
  adresa?: IAdresa | null;
  potrebanMaterijals?: IPotrebanMaterijal[] | null;
  vaspitacs?: IVaspitac[] | null;
}

export class Objekat implements IObjekat {
  constructor(
    public opis?: string | null,
    public naziv?: string | null,
    public id?: number,
    public adresa?: IAdresa | null,
    public potrebanMaterijals?: IPotrebanMaterijal[] | null,
    public vaspitacs?: IVaspitac[] | null
  ) {}
}

export class ObjekatDTO {
  constructor(public opis: string | null, public naziv: string | null, public id: number, public adresa: AdresaDTO) {}
}

export function getObjekatIdentifier(objekat: IObjekat): number | undefined {
  return objekat.id;
}
