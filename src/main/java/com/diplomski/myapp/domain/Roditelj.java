package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Roditelj.
 */
@Entity
@Table(name = "roditelj")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Roditelj implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "roditelj")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formular", "zapazanjeUVeziDetetas", "neDolasci", "roditelj", "grupa" }, allowSetters = true)
    private Set<Dete> detes = new HashSet<>();

    @OneToMany(mappedBy = "roditelj")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "adresa", "podaciORoditeljimas", "dete", "roditelj" }, allowSetters = true)
    private Set<Formular> formulars = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Roditelj id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Dete> getDetes() {
        return this.detes;
    }

    public void setDetes(Set<Dete> detes) {
        if (this.detes != null) {
            this.detes.forEach(i -> i.setRoditelj(null));
        }
        if (detes != null) {
            detes.forEach(i -> i.setRoditelj(this));
        }
        this.detes = detes;
    }

    public Roditelj detes(Set<Dete> detes) {
        this.setDetes(detes);
        return this;
    }

    public Roditelj addDete(Dete dete) {
        this.detes.add(dete);
        dete.setRoditelj(this);
        return this;
    }

    public Roditelj removeDete(Dete dete) {
        this.detes.remove(dete);
        dete.setRoditelj(null);
        return this;
    }

    public Set<Formular> getFormulars() {
        return this.formulars;
    }

    public void setFormulars(Set<Formular> formulars) {
        if (this.formulars != null) {
            this.formulars.forEach(i -> i.setRoditelj(null));
        }
        if (formulars != null) {
            formulars.forEach(i -> i.setRoditelj(this));
        }
        this.formulars = formulars;
    }

    public Roditelj formulars(Set<Formular> formulars) {
        this.setFormulars(formulars);
        return this;
    }

    public Roditelj addFormular(Formular formular) {
        this.formulars.add(formular);
        formular.setRoditelj(this);
        return this;
    }

    public Roditelj removeFormular(Formular formular) {
        this.formulars.remove(formular);
        formular.setRoditelj(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roditelj)) {
            return false;
        }
        return id != null && id.equals(((Roditelj) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roditelj{" +
            "id=" + getId() +
            "}";
    }
}
