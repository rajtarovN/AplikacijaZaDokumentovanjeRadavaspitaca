package com.diplomski.myapp.domain;

import com.diplomski.myapp.domain.enumeration.StatusFormulara;
import com.diplomski.myapp.domain.enumeration.TipGrupe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Formular.
 */
@Entity
@Table(name = "formular")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Formular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "mesec_upisa")
    private Integer mesecUpisa;

    @Column(name = "jmbg")
    private String jmbg;

    @Column(name = "datum_rodjenja")
    private String datumRodjenja;

    @Column(name = "ime_deteta")
    private String imeDeteta;

    @Column(name = "mesto_rodjenja")
    private String mestoRodjenja;

    @Column(name = "opstina_rodjenja")
    private String opstinaRodjenja;

    @Column(name = "drzava")
    private String drzava;

    @Column(name = "br_dece_u_porodici")
    private Integer brDeceUPorodici;

    @Column(name = "zdravstveni_problemi")
    private String zdravstveniProblemi;

    @Column(name = "zdravstveni_uslovi")
    private String zdravstveniUslovi;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_formulara")
    private StatusFormulara statusFormulara;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_grupe")
    private TipGrupe tipGrupe;

    @JsonIgnoreProperties(value = { "formular" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Adresa adresa;

    @OneToMany(mappedBy = "formular")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "formular" }, allowSetters = true)
    private Set<PodaciORoditeljima> podaciORoditeljimas = new HashSet<>();

    @JsonIgnoreProperties(value = { "formular", "zapazanjeUVeziDetetas", "neDolasci", "roditelj", "grupa" }, allowSetters = true)
    @OneToOne(mappedBy = "formular")
    private Dete dete;

    @ManyToOne
    @JsonIgnoreProperties(value = { "detes", "formulars" }, allowSetters = true)
    private Roditelj roditelj;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Formular id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMesecUpisa() {
        return this.mesecUpisa;
    }

    public Formular mesecUpisa(Integer mesecUpisa) {
        this.setMesecUpisa(mesecUpisa);
        return this;
    }

    public void setMesecUpisa(Integer mesecUpisa) {
        this.mesecUpisa = mesecUpisa;
    }

    public String getJmbg() {
        return this.jmbg;
    }

    public Formular jmbg(String jmbg) {
        this.setJmbg(jmbg);
        return this;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getDatumRodjenja() {
        return this.datumRodjenja;
    }

    public Formular datumRodjenja(String datumRodjenja) {
        this.setDatumRodjenja(datumRodjenja);
        return this;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getImeDeteta() {
        return this.imeDeteta;
    }

    public Formular imeDeteta(String imeDeteta) {
        this.setImeDeteta(imeDeteta);
        return this;
    }

    public void setImeDeteta(String imeDeteta) {
        this.imeDeteta = imeDeteta;
    }

    public String getMestoRodjenja() {
        return this.mestoRodjenja;
    }

    public Formular mestoRodjenja(String mestoRodjenja) {
        this.setMestoRodjenja(mestoRodjenja);
        return this;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public String getOpstinaRodjenja() {
        return this.opstinaRodjenja;
    }

    public Formular opstinaRodjenja(String opstinaRodjenja) {
        this.setOpstinaRodjenja(opstinaRodjenja);
        return this;
    }

    public void setOpstinaRodjenja(String opstinaRodjenja) {
        this.opstinaRodjenja = opstinaRodjenja;
    }

    public String getDrzava() {
        return this.drzava;
    }

    public Formular drzava(String drzava) {
        this.setDrzava(drzava);
        return this;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public Integer getBrDeceUPorodici() {
        return this.brDeceUPorodici;
    }

    public Formular brDeceUPorodici(Integer brDeceUPorodici) {
        this.setBrDeceUPorodici(brDeceUPorodici);
        return this;
    }

    public void setBrDeceUPorodici(Integer brDeceUPorodici) {
        this.brDeceUPorodici = brDeceUPorodici;
    }

    public String getZdravstveniProblemi() {
        return this.zdravstveniProblemi;
    }

    public Formular zdravstveniProblemi(String zdravstveniProblemi) {
        this.setZdravstveniProblemi(zdravstveniProblemi);
        return this;
    }

    public void setZdravstveniProblemi(String zdravstveniProblemi) {
        this.zdravstveniProblemi = zdravstveniProblemi;
    }

    public String getZdravstveniUslovi() {
        return this.zdravstveniUslovi;
    }

    public Formular zdravstveniUslovi(String zdravstveniUslovi) {
        this.setZdravstveniUslovi(zdravstveniUslovi);
        return this;
    }

    public void setZdravstveniUslovi(String zdravstveniUslovi) {
        this.zdravstveniUslovi = zdravstveniUslovi;
    }

    public StatusFormulara getStatusFormulara() {
        return this.statusFormulara;
    }

    public Formular statusFormulara(StatusFormulara statusFormulara) {
        this.setStatusFormulara(statusFormulara);
        return this;
    }

    public void setStatusFormulara(StatusFormulara statusFormulara) {
        this.statusFormulara = statusFormulara;
    }

    public TipGrupe getTipGrupe() {
        return this.tipGrupe;
    }

    public Formular tipGrupe(TipGrupe tipGrupe) {
        this.setTipGrupe(tipGrupe);
        return this;
    }

    public void setTipGrupe(TipGrupe tipGrupe) {
        this.tipGrupe = tipGrupe;
    }

    public Adresa getAdresa() {
        return this.adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    public Formular adresa(Adresa adresa) {
        this.setAdresa(adresa);
        return this;
    }

    public Set<PodaciORoditeljima> getPodaciORoditeljimas() {
        return this.podaciORoditeljimas;
    }

    public void setPodaciORoditeljimas(Set<PodaciORoditeljima> podaciORoditeljimas) {
        if (this.podaciORoditeljimas != null) {
            this.podaciORoditeljimas.forEach(i -> i.setFormular(null));
        }
        if (podaciORoditeljimas != null) {
            podaciORoditeljimas.forEach(i -> i.setFormular(this));
        }
        this.podaciORoditeljimas = podaciORoditeljimas;
    }

    public Formular podaciORoditeljimas(Set<PodaciORoditeljima> podaciORoditeljimas) {
        this.setPodaciORoditeljimas(podaciORoditeljimas);
        return this;
    }

    public Formular addPodaciORoditeljima(PodaciORoditeljima podaciORoditeljima) {
        this.podaciORoditeljimas.add(podaciORoditeljima);
        podaciORoditeljima.setFormular(this);
        return this;
    }

    public Formular removePodaciORoditeljima(PodaciORoditeljima podaciORoditeljima) {
        this.podaciORoditeljimas.remove(podaciORoditeljima);
        podaciORoditeljima.setFormular(null);
        return this;
    }

    public Dete getDete() {
        return this.dete;
    }

    public void setDete(Dete dete) {
        if (this.dete != null) {
            this.dete.setFormular(null);
        }
        if (dete != null) {
            dete.setFormular(this);
        }
        this.dete = dete;
    }

    public Formular dete(Dete dete) {
        this.setDete(dete);
        return this;
    }

    public Roditelj getRoditelj() {
        return this.roditelj;
    }

    public void setRoditelj(Roditelj roditelj) {
        this.roditelj = roditelj;
    }

    public Formular roditelj(Roditelj roditelj) {
        this.setRoditelj(roditelj);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Formular)) {
            return false;
        }
        return id != null && id.equals(((Formular) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Formular{" +
            "id=" + getId() +
            ", mesecUpisa=" + getMesecUpisa() +
            ", jmbg='" + getJmbg() + "'" +
            ", datumRodjenja='" + getDatumRodjenja() + "'" +
            ", imeDeteta='" + getImeDeteta() + "'" +
            ", mestoRodjenja='" + getMestoRodjenja() + "'" +
            ", opstinaRodjenja='" + getOpstinaRodjenja() + "'" +
            ", drzava='" + getDrzava() + "'" +
            ", brDeceUPorodici=" + getBrDeceUPorodici() +
            ", zdravstveniProblemi='" + getZdravstveniProblemi() + "'" +
            ", zdravstveniUslovi='" + getZdravstveniUslovi() + "'" +
            ", statusFormulara='" + getStatusFormulara() + "'" +
            ", tipGrupe='" + getTipGrupe() + "'" +
            "}";
    }
}
