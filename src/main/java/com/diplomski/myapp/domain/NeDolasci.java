package com.diplomski.myapp.domain;

import com.diplomski.myapp.web.rest.dto.NeDolasciDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NeDolasci.
 */
@Entity
@Table(name = "ne_dolasci")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NeDolasci implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "datum")
    private LocalDate datum;

    @Column(name = "razlog")
    private String razlog;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "formular", "zapazanjeUVeziDetetas", "neDolasci", "roditelj", "grupa" }, allowSetters = true)
    @ManyToOne
    @JoinColumn(unique = false)
    private Dete dete;

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupa", "pricas", "neDolascis", "vaspitacs" }, allowSetters = true)
    private Dnevnik dnevnik;

    public NeDolasci(NeDolasciDTO dto, Dete dete, Dnevnik dnevnik) {
        this.dete = dete;
        this.datum = dto.getDatum();
        this.dnevnik = dnevnik;
        this.razlog = dto.getRazlog();
    }

    public NeDolasci() {}

    public NeDolasci(LocalDate datum, String razlog, Long id, Dete dete, Dnevnik dnevnik) {
        this.datum = datum;
        this.razlog = razlog;
        this.id = id;
        this.dete = dete;
        this.dnevnik = dnevnik;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LocalDate getDatum() {
        return this.datum;
    }

    public NeDolasci datum(LocalDate datum) {
        this.setDatum(datum);
        return this;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getRazlog() {
        return this.razlog;
    }

    public NeDolasci razlog(String razlog) {
        this.setRazlog(razlog);
        return this;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }

    public Long getId() {
        return this.id;
    }

    public NeDolasci id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dete getDete() {
        return this.dete;
    }

    public void setDete(Dete dete) {
        this.dete = dete;
    }

    public NeDolasci dete(Dete dete) {
        this.setDete(dete);
        return this;
    }

    public Dnevnik getDnevnik() {
        return this.dnevnik;
    }

    public void setDnevnik(Dnevnik dnevnik) {
        this.dnevnik = dnevnik;
    }

    public NeDolasci dnevnik(Dnevnik dnevnik) {
        this.setDnevnik(dnevnik);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NeDolasci)) {
            return false;
        }
        return id != null && id.equals(((NeDolasci) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NeDolasci{" +
            "id=" + getId() +
            ", datum='" + getDatum() + "'" +
            ", razlog='" + getRazlog() + "'" +
            "}";
    }
}
