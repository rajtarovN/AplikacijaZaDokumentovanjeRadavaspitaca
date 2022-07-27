import dayjs from 'dayjs/esm';
import { IZapazanjeUVeziDeteta } from 'app/entities/zapazanje-u-vezi-deteta/zapazanje-u-vezi-deteta.model';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { IDnevnik } from 'app/entities/dnevnik/dnevnik.model';

export interface IVaspitac {
  datumZaposlenja?: dayjs.Dayjs | null;
  godineIskustva?: number | null;
  opis?: string | null;
  id?: number;
  zapazanjeUVeziDeteta?: IZapazanjeUVeziDeteta | null;
  objekat?: IObjekat | null;
  dnevniks?: IDnevnik[] | null;
}

export class Vaspitac implements IVaspitac {
  constructor(
    public datumZaposlenja?: dayjs.Dayjs | null,
    public godineIskustva?: number | null,
    public opis?: string | null,
    public id?: number,
    public zapazanjeUVeziDeteta?: IZapazanjeUVeziDeteta | null,
    public objekat?: IObjekat | null,
    public dnevniks?: IDnevnik[] | null
  ) {}
}

export class VaspitacZaGrupuDTO {
  constructor(public ime?: string | null, public id?: number) {}
}

export class VaspitacDTO {
  constructor(public datumZaposlenja: dayjs.Dayjs, public godineIskustva: number, public opis: string, public id: number) {}
}
export function getVaspitacIdentifier(vaspitac: IVaspitac): number | undefined {
  return vaspitac.id;
}
