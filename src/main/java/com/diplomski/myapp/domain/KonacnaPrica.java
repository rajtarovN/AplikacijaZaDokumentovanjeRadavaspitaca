package com.diplomski.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KonacnaPrica.
 */
@Entity
@Table(name = "konacna_prica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KonacnaPrica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tekst")
    private String tekst;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KonacnaPrica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTekst() {
        return this.tekst;
    }

    public KonacnaPrica tekst(String tekst) {
        this.setTekst(tekst);
        return this;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KonacnaPrica)) {
            return false;
        }
        return id != null && id.equals(((KonacnaPrica) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KonacnaPrica{" +
            "id=" + getId() +
            ", tekst='" + getTekst() + "'" +
            "}";
    }
}
