package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KomentarNaPricu.
 */
@Entity
@Table(name = "komentar_na_pricu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KomentarNaPricu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tekst")
    private String tekst;

    @Column(name = "datum")
    private LocalDate datum;

    @ManyToOne
    @JsonIgnoreProperties(value = { "planPrice", "konacnaPrica", "komentarNaPricus", "dnevnik" }, allowSetters = true)
    private Prica prica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KomentarNaPricu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTekst() {
        return this.tekst;
    }

    public KomentarNaPricu tekst(String tekst) {
        this.setTekst(tekst);
        return this;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public LocalDate getDatum() {
        return this.datum;
    }

    public KomentarNaPricu datum(LocalDate datum) {
        this.setDatum(datum);
        return this;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Prica getPrica() {
        return this.prica;
    }

    public void setPrica(Prica prica) {
        this.prica = prica;
    }

    public KomentarNaPricu prica(Prica prica) {
        this.setPrica(prica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KomentarNaPricu)) {
            return false;
        }
        return id != null && id.equals(((KomentarNaPricu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KomentarNaPricu{" +
            "id=" + getId() +
            ", tekst='" + getTekst() + "'" +
            ", datum='" + getDatum() + "'" +
            "}";
    }
}
