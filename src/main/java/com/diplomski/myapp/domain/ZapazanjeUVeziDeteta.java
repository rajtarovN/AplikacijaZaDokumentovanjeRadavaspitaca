package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ZapazanjeUVeziDeteta.
 */
@Entity
@Table(name = "zapazanje_u_vezi_deteta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ZapazanjeUVeziDeteta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "interesovanja")
    private String interesovanja;

    @Column(name = "prednosti")
    private String prednosti;

    @Column(name = "mane")
    private String mane;

    @Column(name = "predlozi")
    private String predlozi;

    @Column(name = "datum")
    private LocalDate datum;

    @JsonIgnoreProperties(value = { "zapazanjeUVeziDeteta", "objekat", "dnevniks" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Vaspitac vaspitac;

    @OneToOne
    @JoinColumn(unique = false)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formular", "zapazanjeUVeziDetetas", "neDolasci", "roditelj", "grupa" }, allowSetters = true)
    private Dete dete;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ZapazanjeUVeziDeteta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInteresovanja() {
        return this.interesovanja;
    }

    public ZapazanjeUVeziDeteta interesovanja(String interesovanja) {
        this.setInteresovanja(interesovanja);
        return this;
    }

    public void setInteresovanja(String interesovanja) {
        this.interesovanja = interesovanja;
    }

    public String getPrednosti() {
        return this.prednosti;
    }

    public ZapazanjeUVeziDeteta prednosti(String prednosti) {
        this.setPrednosti(prednosti);
        return this;
    }

    public void setPrednosti(String prednosti) {
        this.prednosti = prednosti;
    }

    public String getMane() {
        return this.mane;
    }

    public ZapazanjeUVeziDeteta mane(String mane) {
        this.setMane(mane);
        return this;
    }

    public void setMane(String mane) {
        this.mane = mane;
    }

    public String getPredlozi() {
        return this.predlozi;
    }

    public ZapazanjeUVeziDeteta predlozi(String predlozi) {
        this.setPredlozi(predlozi);
        return this;
    }

    public void setPredlozi(String predlozi) {
        this.predlozi = predlozi;
    }

    public LocalDate getDatum() {
        return this.datum;
    }

    public ZapazanjeUVeziDeteta datum(LocalDate datum) {
        this.setDatum(datum);
        return this;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Vaspitac getVaspitac() {
        return this.vaspitac;
    }

    public void setVaspitac(Vaspitac vaspitac) {
        this.vaspitac = vaspitac;
    }

    public ZapazanjeUVeziDeteta vaspitac(Vaspitac vaspitac) {
        this.setVaspitac(vaspitac);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dete getDete() {
        return this.dete;
    }

    public void setDete(Dete dete) {
        this.dete = dete;
    }

    public ZapazanjeUVeziDeteta dete(Dete dete) {
        this.setDete(dete);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZapazanjeUVeziDeteta)) {
            return false;
        }
        return id != null && id.equals(((ZapazanjeUVeziDeteta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZapazanjeUVeziDeteta{" +
            "id=" + getId() +
            ", interesovanja='" + getInteresovanja() + "'" +
            ", prednosti='" + getPrednosti() + "'" +
            ", mane='" + getMane() + "'" +
            ", predlozi='" + getPredlozi() + "'" +
            ", datum='" + getDatum() + "'" +
            "}";
    }
}
