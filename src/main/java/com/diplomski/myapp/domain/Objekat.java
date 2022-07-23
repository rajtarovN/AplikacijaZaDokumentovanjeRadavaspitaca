package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Objekat.
 */
@Entity
@Table(name = "objekat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Objekat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "opis")
    private String opis;

    @Column(name = "naziv")
    private String naziv;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "formular" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Adresa adresa;

    @OneToMany(mappedBy = "objekat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "objekat" }, allowSetters = true)
    private Set<PotrebanMaterijal> potrebanMaterijals = new HashSet<>();

    @OneToMany(mappedBy = "objekat")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "zapazanjeUVeziDeteta", "objekat", "dnevniks" }, allowSetters = true)
    private Set<Vaspitac> vaspitacs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getOpis() {
        return this.opis;
    }

    public Objekat opis(String opis) {
        this.setOpis(opis);
        return this;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getId() {
        return this.id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Objekat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Objekat adresa(Adresa adresa) {
        this.setAdresa(adresa);
        return this;
    }

    public Set<PotrebanMaterijal> getPotrebanMaterijals() {
        return this.potrebanMaterijals;
    }

    public void setPotrebanMaterijals(Set<PotrebanMaterijal> potrebanMaterijals) {
        if (this.potrebanMaterijals != null) {
            this.potrebanMaterijals.forEach(i -> i.setObjekat(null));
        }
        if (potrebanMaterijals != null) {
            potrebanMaterijals.forEach(i -> i.setObjekat(this));
        }
        this.potrebanMaterijals = potrebanMaterijals;
    }

    public Objekat potrebanMaterijals(Set<PotrebanMaterijal> potrebanMaterijals) {
        this.setPotrebanMaterijals(potrebanMaterijals);
        return this;
    }

    public Objekat addPotrebanMaterijal(PotrebanMaterijal potrebanMaterijal) {
        this.potrebanMaterijals.add(potrebanMaterijal);
        potrebanMaterijal.setObjekat(this);
        return this;
    }

    public Objekat removePotrebanMaterijal(PotrebanMaterijal potrebanMaterijal) {
        this.potrebanMaterijals.remove(potrebanMaterijal);
        potrebanMaterijal.setObjekat(null);
        return this;
    }

    public Set<Vaspitac> getVaspitacs() {
        return this.vaspitacs;
    }

    public void setVaspitacs(Set<Vaspitac> vaspitacs) {
        if (this.vaspitacs != null) {
            this.vaspitacs.forEach(i -> i.setObjekat(null));
        }
        if (vaspitacs != null) {
            vaspitacs.forEach(i -> i.setObjekat(this));
        }
        this.vaspitacs = vaspitacs;
    }

    public Objekat vaspitacs(Set<Vaspitac> vaspitacs) {
        this.setVaspitacs(vaspitacs);
        return this;
    }

    public Objekat addVaspitac(Vaspitac vaspitac) {
        this.vaspitacs.add(vaspitac);
        vaspitac.setObjekat(this);
        return this;
    }

    public Objekat removeVaspitac(Vaspitac vaspitac) {
        this.vaspitacs.remove(vaspitac);
        vaspitac.setObjekat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objekat)) {
            return false;
        }
        return id != null && id.equals(((Objekat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Objekat{" +
            "id=" + getId() +
            ", opis='" + getOpis() + "'" +
            ", naziv='" + getNaziv() + "'" +
            "}";
    }
}
