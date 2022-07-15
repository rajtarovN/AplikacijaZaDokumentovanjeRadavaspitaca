package com.diplomski.myapp.domain;

import com.diplomski.myapp.domain.enumeration.StatusMaterijala;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PotrebanMaterijal.
 */
@Entity
@Table(name = "potreban_materijal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PotrebanMaterijal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "kolicina")
    private Integer kolicina;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_materijala")
    private StatusMaterijala statusMaterijala;

    @ManyToOne
    @JsonIgnoreProperties(value = { "adresa", "potrebanMaterijals", "vaspitacs" }, allowSetters = true)
    private Objekat objekat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getNaziv() {
        return this.naziv;
    }

    public PotrebanMaterijal naziv(String naziv) {
        this.setNaziv(naziv);
        return this;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getKolicina() {
        return this.kolicina;
    }

    public PotrebanMaterijal kolicina(Integer kolicina) {
        this.setKolicina(kolicina);
        return this;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public Long getId() {
        return this.id;
    }

    public PotrebanMaterijal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusMaterijala getStatusMaterijala() {
        return this.statusMaterijala;
    }

    public PotrebanMaterijal statusMaterijala(StatusMaterijala statusMaterijala) {
        this.setStatusMaterijala(statusMaterijala);
        return this;
    }

    public void setStatusMaterijala(StatusMaterijala statusMaterijala) {
        this.statusMaterijala = statusMaterijala;
    }

    public Objekat getObjekat() {
        return this.objekat;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public PotrebanMaterijal objekat(Objekat objekat) {
        this.setObjekat(objekat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PotrebanMaterijal)) {
            return false;
        }
        return id != null && id.equals(((PotrebanMaterijal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotrebanMaterijal{" +
            "id=" + getId() +
            ", naziv='" + getNaziv() + "'" +
            ", kolicina=" + getKolicina() +
            ", statusMaterijala='" + getStatusMaterijala() + "'" +
            "}";
    }
}
