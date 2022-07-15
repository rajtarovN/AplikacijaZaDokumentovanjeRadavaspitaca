package com.diplomski.myapp.domain;

import com.diplomski.myapp.domain.enumeration.TipGrupe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Grupa.
 */
@Entity
@Table(name = "grupa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Grupa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_grupe")
    private TipGrupe tipGrupe;

    @OneToMany(mappedBy = "grupa")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formular", "zapazanjeUVeziDetetas", "neDolasci", "roditelj", "grupa" }, allowSetters = true)
    private Set<Dete> detes = new HashSet<>();

    @JsonIgnoreProperties(value = { "grupa", "pricas", "neDolascis", "vaspitacs" }, allowSetters = true)
    @OneToOne(mappedBy = "grupa")
    private Dnevnik dnevnik;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Grupa id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipGrupe getTipGrupe() {
        return this.tipGrupe;
    }

    public Grupa tipGrupe(TipGrupe tipGrupe) {
        this.setTipGrupe(tipGrupe);
        return this;
    }

    public void setTipGrupe(TipGrupe tipGrupe) {
        this.tipGrupe = tipGrupe;
    }

    public Set<Dete> getDetes() {
        return this.detes;
    }

    public void setDetes(Set<Dete> detes) {
        if (this.detes != null) {
            this.detes.forEach(i -> i.setGrupa(null));
        }
        if (detes != null) {
            detes.forEach(i -> i.setGrupa(this));
        }
        this.detes = detes;
    }

    public Grupa detes(Set<Dete> detes) {
        this.setDetes(detes);
        return this;
    }

    public Grupa addDete(Dete dete) {
        this.detes.add(dete);
        dete.setGrupa(this);
        return this;
    }

    public Grupa removeDete(Dete dete) {
        this.detes.remove(dete);
        dete.setGrupa(null);
        return this;
    }

    public Dnevnik getDnevnik() {
        return this.dnevnik;
    }

    public void setDnevnik(Dnevnik dnevnik) {
        if (this.dnevnik != null) {
            this.dnevnik.setGrupa(null);
        }
        if (dnevnik != null) {
            dnevnik.setGrupa(this);
        }
        this.dnevnik = dnevnik;
    }

    public Grupa dnevnik(Dnevnik dnevnik) {
        this.setDnevnik(dnevnik);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Grupa)) {
            return false;
        }
        return id != null && id.equals(((Grupa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Grupa{" +
            "id=" + getId() +
            ", tipGrupe='" + getTipGrupe() + "'" +
            "}";
    }
}
