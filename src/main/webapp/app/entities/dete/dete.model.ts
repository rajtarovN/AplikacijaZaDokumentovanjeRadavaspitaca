import { IFormular } from 'app/entities/formular/formular.model';
import { IZapazanjeUVeziDeteta } from 'app/entities/zapazanje-u-vezi-deteta/zapazanje-u-vezi-deteta.model';
import { INeDolasci } from 'app/entities/ne-dolasci/ne-dolasci.model';
import { IRoditelj } from 'app/entities/roditelj/roditelj.model';
import { IGrupa } from 'app/entities/grupa/grupa.model';

export interface IDete {
  visina?: number | null;
  tezina?: number | null;
  id?: number;
  formular?: IFormular | null;
  zapazanjeUVeziDetetas?: IZapazanjeUVeziDeteta[] | null;
  neDolasci?: INeDolasci | null;
  roditelj?: IRoditelj | null;
  grupa?: IGrupa | null;
}

export class Dete implements IDete {
  constructor(
    public visina?: number | null,
    public tezina?: number | null,
    public id?: number,
    public formular?: IFormular | null,
    public zapazanjeUVeziDetetas?: IZapazanjeUVeziDeteta[] | null,
    public neDolasci?: INeDolasci | null,
    public roditelj?: IRoditelj | null,
    public grupa?: IGrupa | null
  ) {}
}

export function getDeteIdentifier(dete: IDete): number | undefined {
  return dete.id;
}
