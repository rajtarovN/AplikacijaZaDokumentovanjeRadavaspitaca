export interface IPedagog {
  id?: number;
}

export class Pedagog implements IPedagog {
  constructor(public id?: number) {}
}

export function getPedagogIdentifier(pedagog: IPedagog): number | undefined {
  return pedagog.id;
}
