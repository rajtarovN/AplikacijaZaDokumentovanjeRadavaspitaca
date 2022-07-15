package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Adresa.
 */
@Entity
@Table(name = "adresa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Adresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "mesto")
    private String mesto;

    @Column(name = "ulica")
    private String ulica;

    @Column(name = "broj")
    private String broj;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "adresa", "podaciORoditeljimas", "dete", "roditelj" }, allowSetters = true)
    @OneToOne(mappedBy = "adresa")
    private Formular formular;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getMesto() {
        return this.mesto;
    }

    public Adresa mesto(String mesto) {
        this.setMesto(mesto);
        return this;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getUlica() {
        return this.ulica;
    }

    public Adresa ulica(String ulica) {
        this.setUlica(ulica);
        return this;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getBroj() {
        return this.broj;
    }

    public Adresa broj(String broj) {
        this.setBroj(broj);
        return this;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public Long getId() {
        return this.id;
    }

    public Adresa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Formular getFormular() {
        return this.formular;
    }

    public void setFormular(Formular formular) {
        if (this.formular != null) {
            this.formular.setAdresa(null);
        }
        if (formular != null) {
            formular.setAdresa(this);
        }
        this.formular = formular;
    }

    public Adresa formular(Formular formular) {
        this.setFormular(formular);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adresa)) {
            return false;
        }
        return id != null && id.equals(((Adresa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Adresa{" +
            "id=" + getId() +
            ", mesto='" + getMesto() + "'" +
            ", ulica='" + getUlica() + "'" +
            ", broj='" + getBroj() + "'" +
            "}";
    }
}
