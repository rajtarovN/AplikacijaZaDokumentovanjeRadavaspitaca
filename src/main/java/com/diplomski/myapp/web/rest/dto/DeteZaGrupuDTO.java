package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.Formular;

public class DeteZaGrupuDTO {

    public Long id;
    public String imeDeteta;
    public boolean dodato;

    public DeteZaGrupuDTO(Long id, String imeDeteta, boolean dodato) {
        this.id = id;
        this.imeDeteta = imeDeteta;
        this.dodato = dodato;
    }

    public DeteZaGrupuDTO() {}

    public DeteZaGrupuDTO(Formular f) {
        this.id = f.getId();
        this.dodato = false;
        this.imeDeteta = f.getImeDeteta();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeDeteta() {
        return imeDeteta;
    }

    public void setImeDeteta(String imeDeteta) {
        this.imeDeteta = imeDeteta;
    }

    public boolean isDodato() {
        return dodato;
    }

    public void setDodato(boolean dodato) {
        this.dodato = dodato;
    }
}
