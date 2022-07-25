package com.diplomski.myapp.web.rest.dto;

import java.time.LocalDate;

public class NeDolasciDTO {

    public LocalDate datum;
    public String razlog;
    public String dete;
    public Long idDeteta;
    public Long idDnevnika;
    public boolean odsutan; //true znaci da nije tu
    public Long id;

    public NeDolasciDTO(LocalDate datum, String razlog, String dete, Long idDeteta, Long idDnevnika, boolean odsutan, Long id) {
        this.datum = datum;
        this.razlog = razlog;
        this.dete = dete;
        this.idDeteta = idDeteta;
        this.idDnevnika = idDnevnika;
        this.odsutan = odsutan;
        this.id = id;
    }

    public NeDolasciDTO() {}

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

    public Long getIdDnevnika() {
        return idDnevnika;
    }

    public void setIdDnevnika(Long idDnevnika) {
        this.idDnevnika = idDnevnika;
    }

    public boolean isOdsutan() {
        return odsutan;
    }

    public void setOdsutan(boolean odsutan) {
        this.odsutan = odsutan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
