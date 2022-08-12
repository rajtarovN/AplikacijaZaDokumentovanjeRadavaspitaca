package com.diplomski.myapp.domain;

import com.diplomski.myapp.domain.enumeration.Status;
import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vaspitac.
 */
@Entity
@Table(name = "vaspitac")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vaspitac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "datum_zaposlenja")
    private LocalDate datumZaposlenja;

    @Column(name = "godine_iskustva")
    private Integer godineIskustva;

    @Column(name = "opis")
    private String opis;

    @Column(name = "slika")
    private String slika;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "vaspitac", "dete" }, allowSetters = true)
    @OneToOne(mappedBy = "vaspitac")
    private ZapazanjeUVeziDeteta zapazanjeUVeziDeteta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "adresa", "potrebanMaterijals", "vaspitacs" }, allowSetters = true)
    private Objekat objekat;

    @ManyToMany(mappedBy = "vaspitacs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "grupa", "pricas", "neDolascis", "vaspitacs" }, allowSetters = true)
    private Set<Dnevnik> dnevniks = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LocalDate getDatumZaposlenja() {
        return this.datumZaposlenja;
    }

    public Vaspitac datumZaposlenja(LocalDate datumZaposlenja) {
        this.setDatumZaposlenja(datumZaposlenja);
        return this;
    }

    public void setDatumZaposlenja(LocalDate datumZaposlenja) {
        this.datumZaposlenja = datumZaposlenja;
    }

    public Integer getGodineIskustva() {
        return this.godineIskustva;
    }

    public Vaspitac godineIskustva(Integer godineIskustva) {
        this.setGodineIskustva(godineIskustva);
        return this;
    }

    public void setGodineIskustva(Integer godineIskustva) {
        this.godineIskustva = godineIskustva;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOpis() {
        return this.opis;
    }

    public Vaspitac opis(String opis) {
        this.setOpis(opis);
        return this;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getId() {
        return this.id;
    }

    public Vaspitac id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZapazanjeUVeziDeteta getZapazanjeUVeziDeteta() {
        return this.zapazanjeUVeziDeteta;
    }

    public void setZapazanjeUVeziDeteta(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        if (this.zapazanjeUVeziDeteta != null) {
            //this.zapazanjeUVeziDeteta.setVaspitac(null);
        }
        if (zapazanjeUVeziDeteta != null) {
            // zapazanjeUVeziDeteta.setVaspitac(this);
        }
        this.zapazanjeUVeziDeteta = zapazanjeUVeziDeteta;
    }

    public Vaspitac zapazanjeUVeziDeteta(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        this.setZapazanjeUVeziDeteta(zapazanjeUVeziDeteta);
        return this;
    }

    public Objekat getObjekat() {
        return this.objekat;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public Vaspitac objekat(Objekat objekat) {
        this.setObjekat(objekat);
        return this;
    }

    public Set<Dnevnik> getDnevniks() {
        return this.dnevniks;
    }

    public void setDnevniks(Set<Dnevnik> dnevniks) {
        if (this.dnevniks != null) {
            this.dnevniks.forEach(i -> i.removeVaspitac(this));
        }
        if (dnevniks != null) {
            dnevniks.forEach(i -> i.addVaspitac(this));
        }
        this.dnevniks = dnevniks;
    }

    public Vaspitac dnevniks(Set<Dnevnik> dnevniks) {
        this.setDnevniks(dnevniks);
        return this;
    }

    public Vaspitac addDnevnik(Dnevnik dnevnik) {
        this.dnevniks.add(dnevnik);
        dnevnik.getVaspitacs().add(this);
        return this;
    }

    public Vaspitac removeDnevnik(Dnevnik dnevnik) {
        this.dnevniks.remove(dnevnik);
        dnevnik.getVaspitacs().remove(this);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaspitac)) {
            return false;
        }
        return id != null && id.equals(((Vaspitac) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaspitac{" +
            "id=" + getId() +
            ", datumZaposlenja='" + getDatumZaposlenja() + "'" +
            ", godineIskustva=" + getGodineIskustva() +
            ", opis='" + getOpis() + "'" +
            "}";
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }
}
