package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.Vaspitac;

public class VaspitacDTO {

    public String ime;
    public Long id;

    public VaspitacDTO() {}

    public VaspitacDTO(String ime, Long id) {
        this.ime = ime;
        this.id = id;
    }

    public VaspitacDTO(Vaspitac f) {
        this.id = f.getId();
        this.ime = "a"; // todo f.getIme();
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
