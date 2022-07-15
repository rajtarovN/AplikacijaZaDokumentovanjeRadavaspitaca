package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dnevnik.
 */
@Entity
@Table(name = "dnevnik")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dnevnik implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "pocetak_vazenja")
    private LocalDate pocetakVazenja;

    @Column(name = "kraj_vazenja")
    private LocalDate krajVazenja;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "detes", "dnevnik" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Grupa grupa;

    @OneToMany(mappedBy = "dnevnik")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "planPrice", "konacnaPrica", "komentarNaPricus", "dnevnik" }, allowSetters = true)
    private Set<Prica> pricas = new HashSet<>();

    @OneToMany(mappedBy = "dnevnik")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dete", "dnevnik" }, allowSetters = true)
    private Set<NeDolasci> neDolascis = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_dnevnik__vaspitac",
        joinColumns = @JoinColumn(name = "dnevnik_id"),
        inverseJoinColumns = @JoinColumn(name = "vaspitac_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "zapazanjeUVeziDeteta", "objekat", "dnevniks" }, allowSetters = true)
    private Set<Vaspitac> vaspitacs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LocalDate getPocetakVazenja() {
        return this.pocetakVazenja;
    }

    public Dnevnik pocetakVazenja(LocalDate pocetakVazenja) {
        this.setPocetakVazenja(pocetakVazenja);
        return this;
    }

    public void setPocetakVazenja(LocalDate pocetakVazenja) {
        this.pocetakVazenja = pocetakVazenja;
    }

    public LocalDate getKrajVazenja() {
        return this.krajVazenja;
    }

    public Dnevnik krajVazenja(LocalDate krajVazenja) {
        this.setKrajVazenja(krajVazenja);
        return this;
    }

    public void setKrajVazenja(LocalDate krajVazenja) {
        this.krajVazenja = krajVazenja;
    }

    public Long getId() {
        return this.id;
    }

    public Dnevnik id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Grupa getGrupa() {
        return this.grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public Dnevnik grupa(Grupa grupa) {
        this.setGrupa(grupa);
        return this;
    }

    public Set<Prica> getPricas() {
        return this.pricas;
    }

    public void setPricas(Set<Prica> pricas) {
        if (this.pricas != null) {
            this.pricas.forEach(i -> i.setDnevnik(null));
        }
        if (pricas != null) {
            pricas.forEach(i -> i.setDnevnik(this));
        }
        this.pricas = pricas;
    }

    public Dnevnik pricas(Set<Prica> pricas) {
        this.setPricas(pricas);
        return this;
    }

    public Dnevnik addPrica(Prica prica) {
        this.pricas.add(prica);
        prica.setDnevnik(this);
        return this;
    }

    public Dnevnik removePrica(Prica prica) {
        this.pricas.remove(prica);
        prica.setDnevnik(null);
        return this;
    }

    public Set<NeDolasci> getNeDolascis() {
        return this.neDolascis;
    }

    public void setNeDolascis(Set<NeDolasci> neDolascis) {
        if (this.neDolascis != null) {
            this.neDolascis.forEach(i -> i.setDnevnik(null));
        }
        if (neDolascis != null) {
            neDolascis.forEach(i -> i.setDnevnik(this));
        }
        this.neDolascis = neDolascis;
    }

    public Dnevnik neDolascis(Set<NeDolasci> neDolascis) {
        this.setNeDolascis(neDolascis);
        return this;
    }

    public Dnevnik addNeDolasci(NeDolasci neDolasci) {
        this.neDolascis.add(neDolasci);
        neDolasci.setDnevnik(this);
        return this;
    }

    public Dnevnik removeNeDolasci(NeDolasci neDolasci) {
        this.neDolascis.remove(neDolasci);
        neDolasci.setDnevnik(null);
        return this;
    }

    public Set<Vaspitac> getVaspitacs() {
        return this.vaspitacs;
    }

    public void setVaspitacs(Set<Vaspitac> vaspitacs) {
        this.vaspitacs = vaspitacs;
    }

    public Dnevnik vaspitacs(Set<Vaspitac> vaspitacs) {
        this.setVaspitacs(vaspitacs);
        return this;
    }

    public Dnevnik addVaspitac(Vaspitac vaspitac) {
        this.vaspitacs.add(vaspitac);
        vaspitac.getDnevniks().add(this);
        return this;
    }

    public Dnevnik removeVaspitac(Vaspitac vaspitac) {
        this.vaspitacs.remove(vaspitac);
        vaspitac.getDnevniks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dnevnik)) {
            return false;
        }
        return id != null && id.equals(((Dnevnik) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dnevnik{" +
            "id=" + getId() +
            ", pocetakVazenja='" + getPocetakVazenja() + "'" +
            ", krajVazenja='" + getKrajVazenja() + "'" +
            "}";
    }
}
