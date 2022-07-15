package com.diplomski.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Korisnik.
 */
@Entity
@Table(name = "korisnik")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Korisnik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "korisnicko_ime")
    private String korisnickoIme;

    @Column(name = "sifra")
    private String sifra;

    @Column(name = "ime")
    private String ime;

    @Column(name = "prezime")
    private String prezime;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getKorisnickoIme() {
        return this.korisnickoIme;
    }

    public Korisnik korisnickoIme(String korisnickoIme) {
        this.setKorisnickoIme(korisnickoIme);
        return this;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getSifra() {
        return this.sifra;
    }

    public Korisnik sifra(String sifra) {
        this.setSifra(sifra);
        return this;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getIme() {
        return this.ime;
    }

    public Korisnik ime(String ime) {
        this.setIme(ime);
        return this;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return this.prezime;
    }

    public Korisnik prezime(String prezime) {
        this.setPrezime(prezime);
        return this;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Long getId() {
        return this.id;
    }

    public Korisnik id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Korisnik)) {
            return false;
        }
        return id != null && id.equals(((Korisnik) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Korisnik{" +
            "id=" + getId() +
            ", korisnickoIme='" + getKorisnickoIme() + "'" +
            ", sifra='" + getSifra() + "'" +
            ", ime='" + getIme() + "'" +
            ", prezime='" + getPrezime() + "'" +
            "}";
    }
}
