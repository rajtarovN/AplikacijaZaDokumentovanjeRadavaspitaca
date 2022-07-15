package com.diplomski.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prica.
 */
@Entity
@Table(name = "prica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private PlanPrice planPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private KonacnaPrica konacnaPrica;

    @OneToMany(mappedBy = "prica")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prica" }, allowSetters = true)
    private Set<KomentarNaPricu> komentarNaPricus = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "grupa", "pricas", "neDolascis", "vaspitacs" }, allowSetters = true)
    private Dnevnik dnevnik;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanPrice getPlanPrice() {
        return this.planPrice;
    }

    public void setPlanPrice(PlanPrice planPrice) {
        this.planPrice = planPrice;
    }

    public Prica planPrice(PlanPrice planPrice) {
        this.setPlanPrice(planPrice);
        return this;
    }

    public KonacnaPrica getKonacnaPrica() {
        return this.konacnaPrica;
    }

    public void setKonacnaPrica(KonacnaPrica konacnaPrica) {
        this.konacnaPrica = konacnaPrica;
    }

    public Prica konacnaPrica(KonacnaPrica konacnaPrica) {
        this.setKonacnaPrica(konacnaPrica);
        return this;
    }

    public Set<KomentarNaPricu> getKomentarNaPricus() {
        return this.komentarNaPricus;
    }

    public void setKomentarNaPricus(Set<KomentarNaPricu> komentarNaPricus) {
        if (this.komentarNaPricus != null) {
            this.komentarNaPricus.forEach(i -> i.setPrica(null));
        }
        if (komentarNaPricus != null) {
            komentarNaPricus.forEach(i -> i.setPrica(this));
        }
        this.komentarNaPricus = komentarNaPricus;
    }

    public Prica komentarNaPricus(Set<KomentarNaPricu> komentarNaPricus) {
        this.setKomentarNaPricus(komentarNaPricus);
        return this;
    }

    public Prica addKomentarNaPricu(KomentarNaPricu komentarNaPricu) {
        this.komentarNaPricus.add(komentarNaPricu);
        komentarNaPricu.setPrica(this);
        return this;
    }

    public Prica removeKomentarNaPricu(KomentarNaPricu komentarNaPricu) {
        this.komentarNaPricus.remove(komentarNaPricu);
        komentarNaPricu.setPrica(null);
        return this;
    }

    public Dnevnik getDnevnik() {
        return this.dnevnik;
    }

    public void setDnevnik(Dnevnik dnevnik) {
        this.dnevnik = dnevnik;
    }

    public Prica dnevnik(Dnevnik dnevnik) {
        this.setDnevnik(dnevnik);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prica)) {
            return false;
        }
        return id != null && id.equals(((Prica) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prica{" +
            "id=" + getId() +
            "}";
    }
}
