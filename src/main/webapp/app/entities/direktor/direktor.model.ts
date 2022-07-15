export interface IDirektor {
  id?: number;
}

export class Direktor implements IDirektor {
  constructor(public id?: number) {}
}

export function getDirektorIdentifier(direktor: IDirektor): number | undefined {
  return direktor.id;
}
