package com.diplomski.myapp.domain;

import com.diplomski.myapp.domain.enumeration.RadniStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PodaciORoditeljima.
 */
@Entity
@Table(name = "podaci_o_roditeljima")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PodaciORoditeljima implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "jmbg")
    private String jmbg;

    @Column(name = "ime")
    private String ime;

    @Column(name = "prezime")
    private String prezime;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "firma")
    private String firma;

    @Column(name = "radno_vreme")
    private String radnoVreme;

    @Enumerated(EnumType.STRING)
    @Column(name = "radni_status")
    private RadniStatus radniStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "adresa", "podaciORoditeljimas", "dete", "roditelj" }, allowSetters = true)
    private Formular formular;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PodaciORoditeljima id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJmbg() {
        return this.jmbg;
    }

    public PodaciORoditeljima jmbg(String jmbg) {
        this.setJmbg(jmbg);
        return this;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getIme() {
        return this.ime;
    }

    public PodaciORoditeljima ime(String ime) {
        this.setIme(ime);
        return this;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return this.prezime;
    }

    public PodaciORoditeljima prezime(String prezime) {
        this.setPrezime(prezime);
        return this;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public PodaciORoditeljima telefon(String telefon) {
        this.setTelefon(telefon);
        return this;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFirma() {
        return this.firma;
    }

    public PodaciORoditeljima firma(String firma) {
        this.setFirma(firma);
        return this;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getRadnoVreme() {
        return this.radnoVreme;
    }

    public PodaciORoditeljima radnoVreme(String radnoVreme) {
        this.setRadnoVreme(radnoVreme);
        return this;
    }

    public void setRadnoVreme(String radnoVreme) {
        this.radnoVreme = radnoVreme;
    }

    public RadniStatus getRadniStatus() {
        return this.radniStatus;
    }

    public PodaciORoditeljima radniStatus(RadniStatus radniStatus) {
        this.setRadniStatus(radniStatus);
        return this;
    }

    public void setRadniStatus(RadniStatus radniStatus) {
        this.radniStatus = radniStatus;
    }

    public Formular getFormular() {
        return this.formular;
    }

    public void setFormular(Formular formular) {
        this.formular = formular;
    }

    public PodaciORoditeljima formular(Formular formular) {
        this.setFormular(formular);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PodaciORoditeljima)) {
            return false;
        }
        return id != null && id.equals(((PodaciORoditeljima) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PodaciORoditeljima{" +
            "id=" + getId() +
            ", jmbg='" + getJmbg() + "'" +
            ", ime='" + getIme() + "'" +
            ", prezime='" + getPrezime() + "'" +
            ", telefon='" + getTelefon() + "'" +
            ", firma='" + getFirma() + "'" +
            ", radnoVreme='" + getRadnoVreme() + "'" +
            ", radniStatus='" + getRadniStatus() + "'" +
            "}";
    }
}
