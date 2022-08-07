package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.NeDolasci;
import java.time.LocalDate;

public class NeDolazakViewDTO {

    public LocalDate datum;
    public String razlog;
    public String dete;
    public Long idDeteta;
    public Long dnevnik;
    public Long id;

    public NeDolazakViewDTO(LocalDate datum, String razlog, String dete, Long idDeteta, Long dnevnik, Long id) {
        this.datum = datum;
        this.razlog = razlog;
        this.dete = dete;
        this.idDeteta = idDeteta;
        this.dnevnik = dnevnik;
        this.id = id;
    }

    public NeDolazakViewDTO() {}

    public NeDolazakViewDTO(NeDolasci i, String dete) {
        this.datum = i.getDatum();
        this.razlog = i.getRazlog();
        this.dete = dete;
        this.idDeteta = i.getDete().getId();
        this.dnevnik = i.getDnevnik().getId();
        this.id = i.getId();
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public String getRazlog() {
        return razlog;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }

    public String getDete() {
        return dete;
    }

    public void setDete(String dete) {
        this.dete = dete;
    }

    public Long getIdDeteta() {
        return idDeteta;
    }

    public void setIdDeteta(Long idDeteta) {
        this.idDeteta = idDeteta;
    }

    public Long getDnevnik() {
        return dnevnik;
    }

    public void setDnevnik(Long dnevnik) {
        this.dnevnik = dnevnik;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
