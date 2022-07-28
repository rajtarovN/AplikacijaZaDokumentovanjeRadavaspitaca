package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.enumeration.TipGrupe;
import java.time.LocalDate;
import java.util.List;

public class GrupaDTO {

    public TipGrupe tipGrupe;
    public List<DeteZaGrupuDTO> deca;
    public LocalDate pocetakVazenja;
    public LocalDate krajVazenja;
    public VaspitacDTO vaspitac;

    public GrupaDTO(TipGrupe tipGrupe, List<DeteZaGrupuDTO> deca, LocalDate pocetakVazenja, LocalDate krajVazenja, VaspitacDTO vaspitac) {
        this.tipGrupe = tipGrupe;
        this.deca = deca;
        this.pocetakVazenja = pocetakVazenja;
        this.krajVazenja = krajVazenja;
        this.vaspitac = vaspitac;
    }

    public GrupaDTO() {}

    public TipGrupe getTipGrupe() {
        return tipGrupe;
    }

    public void setTipGrupe(TipGrupe tipGrupe) {
        this.tipGrupe = tipGrupe;
    }

    public List<DeteZaGrupuDTO> getDeca() {
        return deca;
    }

    public void setDeca(List<DeteZaGrupuDTO> deca) {
        this.deca = deca;
    }

    public LocalDate getPocetakVazenja() {
        return pocetakVazenja;
    }

    public void setPocetakVazenja(LocalDate pocetakVazenja) {
        this.pocetakVazenja = pocetakVazenja;
    }

    public LocalDate getKrajVazenja() {
        return krajVazenja;
    }

    public void setKrajVazenja(LocalDate krajVazenja) {
        this.krajVazenja = krajVazenja;
    }

    public VaspitacDTO getVaspitac() {
        return vaspitac;
    }

    public void setVaspitac(VaspitacDTO vaspitac) {
        this.vaspitac = vaspitac;
    }
}
