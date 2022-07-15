export interface IKonacnaPrica {
  id?: number;
  tekst?: string | null;
}

export class KonacnaPrica implements IKonacnaPrica {
  constructor(public id?: number, public tekst?: string | null) {}
}

export function getKonacnaPricaIdentifier(konacnaPrica: IKonacnaPrica): number | undefined {
  return konacnaPrica.id;
}
