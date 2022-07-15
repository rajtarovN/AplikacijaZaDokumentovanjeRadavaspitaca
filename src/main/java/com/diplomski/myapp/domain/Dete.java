package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dete.
 */
@Entity
@Table(name = "dete")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "visina")
    private Integer visina;

    @Column(name = "tezina")
    private Integer tezina;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "adresa", "podaciORoditeljimas", "dete", "roditelj" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Formular formular;

    @OneToMany(mappedBy = "dete")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vaspitac", "dete" }, allowSetters = true)
    private Set<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetas = new HashSet<>();

    @JsonIgnoreProperties(value = { "dete", "dnevnik" }, allowSetters = true)
    @OneToOne(mappedBy = "dete")
    private NeDolasci neDolasci;

    @ManyToOne
    @JsonIgnoreProperties(value = { "detes", "formulars" }, allowSetters = true)
    private Roditelj roditelj;

    @ManyToOne
    @JsonIgnoreProperties(value = { "detes", "dnevnik" }, allowSetters = true)
    private Grupa grupa;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getVisina() {
        return this.visina;
    }

    public Dete visina(Integer visina) {
        this.setVisina(visina);
        return this;
    }

    public void setVisina(Integer visina) {
        this.visina = visina;
    }

    public Integer getTezina() {
        return this.tezina;
    }

    public Dete tezina(Integer tezina) {
        this.setTezina(tezina);
        return this;
    }

    public void setTezina(Integer tezina) {
        this.tezina = tezina;
    }

    public Long getId() {
        return this.id;
    }

    public Dete id(Long id) {
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
        this.formular = formular;
    }

    public Dete formular(Formular formular) {
        this.setFormular(formular);
        return this;
    }

    public Set<ZapazanjeUVeziDeteta> getZapazanjeUVeziDetetas() {
        return this.zapazanjeUVeziDetetas;
    }

    public void setZapazanjeUVeziDetetas(Set<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetas) {
        if (this.zapazanjeUVeziDetetas != null) {
            this.zapazanjeUVeziDetetas.forEach(i -> i.setDete(null));
        }
        if (zapazanjeUVeziDetetas != null) {
            zapazanjeUVeziDetetas.forEach(i -> i.setDete(this));
        }
        this.zapazanjeUVeziDetetas = zapazanjeUVeziDetetas;
    }

    public Dete zapazanjeUVeziDetetas(Set<ZapazanjeUVeziDeteta> zapazanjeUVeziDetetas) {
        this.setZapazanjeUVeziDetetas(zapazanjeUVeziDetetas);
        return this;
    }

    public Dete addZapazanjeUVeziDeteta(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        this.zapazanjeUVeziDetetas.add(zapazanjeUVeziDeteta);
        zapazanjeUVeziDeteta.setDete(this);
        return this;
    }

    public Dete removeZapazanjeUVeziDeteta(ZapazanjeUVeziDeteta zapazanjeUVeziDeteta) {
        this.zapazanjeUVeziDetetas.remove(zapazanjeUVeziDeteta);
        zapazanjeUVeziDeteta.setDete(null);
        return this;
    }

    public NeDolasci getNeDolasci() {
        return this.neDolasci;
    }

    public void setNeDolasci(NeDolasci neDolasci) {
        if (this.neDolasci != null) {
            this.neDolasci.setDete(null);
        }
        if (neDolasci != null) {
            neDolasci.setDete(this);
        }
        this.neDolasci = neDolasci;
    }

    public Dete neDolasci(NeDolasci neDolasci) {
        this.setNeDolasci(neDolasci);
        return this;
    }

    public Roditelj getRoditelj() {
        return this.roditelj;
    }

    public void setRoditelj(Roditelj roditelj) {
        this.roditelj = roditelj;
    }

    public Dete roditelj(Roditelj roditelj) {
        this.setRoditelj(roditelj);
        return this;
    }

    public Grupa getGrupa() {
        return this.grupa;
    }

    public void setGrupa(Grupa grupa) {
        this.grupa = grupa;
    }

    public Dete grupa(Grupa grupa) {
        this.setGrupa(grupa);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dete)) {
            return false;
        }
        return id != null && id.equals(((Dete) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dete{" +
            "id=" + getId() +
            ", visina=" + getVisina() +
            ", tezina=" + getTezina() +
            "}";
    }
}
