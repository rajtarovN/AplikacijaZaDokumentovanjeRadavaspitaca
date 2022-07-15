package com.diplomski.myapp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlanPrice.
 */
@Entity
@Table(name = "plan_price")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlanPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "izvori_saznanja")
    private String izvoriSaznanja;

    @Column(name = "naziv_teme")
    private String nazivTeme;

    @Column(name = "pocetna_ideja")
    private String pocetnaIdeja;

    @Column(name = "datum_pocetka")
    private LocalDate datumPocetka;

    @Column(name = "nacin_ucesca_vaspitaca")
    private String nacinUcescaVaspitaca;

    @Column(name = "materijali")
    private String materijali;

    @Column(name = "ucesce_porodice")
    private String ucescePorodice;

    @Column(name = "mesta")
    private String mesta;

    @Column(name = "datum_zavrsetka")
    private LocalDate datumZavrsetka;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlanPrice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIzvoriSaznanja() {
        return this.izvoriSaznanja;
    }

    public PlanPrice izvoriSaznanja(String izvoriSaznanja) {
        this.setIzvoriSaznanja(izvoriSaznanja);
        return this;
    }

    public void setIzvoriSaznanja(String izvoriSaznanja) {
        this.izvoriSaznanja = izvoriSaznanja;
    }

    public String getNazivTeme() {
        return this.nazivTeme;
    }

    public PlanPrice nazivTeme(String nazivTeme) {
        this.setNazivTeme(nazivTeme);
        return this;
    }

    public void setNazivTeme(String nazivTeme) {
        this.nazivTeme = nazivTeme;
    }

    public String getPocetnaIdeja() {
        return this.pocetnaIdeja;
    }

    public PlanPrice pocetnaIdeja(String pocetnaIdeja) {
        this.setPocetnaIdeja(pocetnaIdeja);
        return this;
    }

    public void setPocetnaIdeja(String pocetnaIdeja) {
        this.pocetnaIdeja = pocetnaIdeja;
    }

    public LocalDate getDatumPocetka() {
        return this.datumPocetka;
    }

    public PlanPrice datumPocetka(LocalDate datumPocetka) {
        this.setDatumPocetka(datumPocetka);
        return this;
    }

    public void setDatumPocetka(LocalDate datumPocetka) {
        this.datumPocetka = datumPocetka;
    }

    public String getNacinUcescaVaspitaca() {
        return this.nacinUcescaVaspitaca;
    }

    public PlanPrice nacinUcescaVaspitaca(String nacinUcescaVaspitaca) {
        this.setNacinUcescaVaspitaca(nacinUcescaVaspitaca);
        return this;
    }

    public void setNacinUcescaVaspitaca(String nacinUcescaVaspitaca) {
        this.nacinUcescaVaspitaca = nacinUcescaVaspitaca;
    }

    public String getMaterijali() {
        return this.materijali;
    }

    public PlanPrice materijali(String materijali) {
        this.setMaterijali(materijali);
        return this;
    }

    public void setMaterijali(String materijali) {
        this.materijali = materijali;
    }

    public String getUcescePorodice() {
        return this.ucescePorodice;
    }

    public PlanPrice ucescePorodice(String ucescePorodice) {
        this.setUcescePorodice(ucescePorodice);
        return this;
    }

    public void setUcescePorodice(String ucescePorodice) {
        this.ucescePorodice = ucescePorodice;
    }

    public String getMesta() {
        return this.mesta;
    }

    public PlanPrice mesta(String mesta) {
        this.setMesta(mesta);
        return this;
    }

    public void setMesta(String mesta) {
        this.mesta = mesta;
    }

    public LocalDate getDatumZavrsetka() {
        return this.datumZavrsetka;
    }

    public PlanPrice datumZavrsetka(LocalDate datumZavrsetka) {
        this.setDatumZavrsetka(datumZavrsetka);
        return this;
    }

    public void setDatumZavrsetka(LocalDate datumZavrsetka) {
        this.datumZavrsetka = datumZavrsetka;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlanPrice)) {
            return false;
        }
        return id != null && id.equals(((PlanPrice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanPrice{" +
            "id=" + getId() +
            ", izvoriSaznanja='" + getIzvoriSaznanja() + "'" +
            ", nazivTeme='" + getNazivTeme() + "'" +
            ", pocetnaIdeja='" + getPocetnaIdeja() + "'" +
            ", datumPocetka='" + getDatumPocetka() + "'" +
            ", nacinUcescaVaspitaca='" + getNacinUcescaVaspitaca() + "'" +
            ", materijali='" + getMaterijali() + "'" +
            ", ucescePorodice='" + getUcescePorodice() + "'" +
            ", mesta='" + getMesta() + "'" +
            ", datumZavrsetka='" + getDatumZavrsetka() + "'" +
            "}";
    }
}
