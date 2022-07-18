import { IFormular } from 'app/entities/formular/formular.model';

export interface IAdresa {
  mesto?: string | null;
  ulica?: string | null;
  broj?: string | null;
  id?: number;
  formular?: IFormular | null;
}

export class Adresa implements IAdresa {
  constructor(
    public mesto?: string | null,
    public ulica?: string | null,
    public broj?: string | null,
    public id?: number,
    public formular?: IFormular | null
  ) {}
}

export class AdresaDTO {
  constructor(public mesto: string | null, public ulica: string | null, public broj: string | null, public id: number) {}
}

export function getAdresaIdentifier(adresa: IAdresa): number | undefined {
  return adresa.id;
}
