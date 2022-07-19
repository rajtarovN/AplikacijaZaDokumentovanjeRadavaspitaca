package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.PodaciORoditeljima;

public class PodaciORoditeljimaDTO {

    public String ime;
    public String prezime;
    public String telefon;
    public String firma;
    public String radnoVreme;
    public String radniStatus;
    public Long id;

    public PodaciORoditeljimaDTO(PodaciORoditeljima r) {
        this.ime = r.getIme();
        this.prezime = r.getPrezime();
        this.telefon = r.getTelefon();
        this.firma = r.getFirma();
        this.radnoVreme = r.getRadnoVreme();
        this.radniStatus = r.getRadniStatus() + "";
        this.id = r.getId();
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getRadnoVreme() {
        return radnoVreme;
    }

    public void setRadnoVreme(String radnoVreme) {
        this.radnoVreme = radnoVreme;
    }

    public String getRadniStatus() {
        return radniStatus;
    }

    public void setRadniStatus(String radniStatus) {
        this.radniStatus = radniStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PodaciORoditeljimaDTO(Long id, String ime, String prezime, String telefon, String firma, String radnoVreme, String radniStatus) {
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.firma = firma;
        this.radnoVreme = radnoVreme;
        this.radniStatus = radniStatus;
        this.id = id;
    }

    public PodaciORoditeljimaDTO() {}
}
