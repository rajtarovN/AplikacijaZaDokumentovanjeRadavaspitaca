export interface IKorisnik {
  korisnickoIme?: string | null;
  sifra?: string | null;
  ime?: string | null;
  prezime?: string | null;
  id?: number;
}

export class Korisnik implements IKorisnik {
  constructor(
    public korisnickoIme?: string | null,
    public sifra?: string | null,
    public ime?: string | null,
    public prezime?: string | null,
    public id?: number
  ) {}
}

export function getKorisnikIdentifier(korisnik: IKorisnik): number | undefined {
  return korisnik.id;
}
