package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.Formular;
import com.diplomski.myapp.domain.enumeration.TipGrupe;

public class DeteZaGrupuDTO {

    public Long id;
    public String imeDeteta;
    public boolean dodato;
    public TipGrupe tipGrupe;

    public DeteZaGrupuDTO(Long id, String imeDeteta, boolean dodato, TipGrupe tipGrupe) {
        this.id = id;
        this.imeDeteta = imeDeteta;
        this.dodato = dodato;
        this.tipGrupe = tipGrupe;
    }

    public DeteZaGrupuDTO() {}

    public DeteZaGrupuDTO(Formular f) {
        this.id = f.getId();
        this.dodato = false;
        this.imeDeteta = f.getImeDeteta();
        this.tipGrupe = f.getTipGrupe();
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

    public TipGrupe getTipGrupe() {
        return tipGrupe;
    }

    public void setTipGrupe(TipGrupe tipGrupe) {
        this.tipGrupe = tipGrupe;
    }
}
