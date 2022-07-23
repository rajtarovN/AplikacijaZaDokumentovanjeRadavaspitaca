package com.diplomski.myapp.web.rest.dto;

import com.diplomski.myapp.domain.Dete;
import com.diplomski.myapp.domain.PodaciORoditeljima;
import com.diplomski.myapp.domain.Roditelj;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProfilDetetaDTO {

    public String ime;
    public String mestoRodjenja;
    public String opstina;
    public String drzava;
    public String zdravstveniProblemi;
    public String zdravstveniUslovi;
    public String grupa;
    public String vaspitac;
    public int brojIzostanaka;
    public String datumRodjenja;
    public int visina;
    public int tezina;
    public Long id;

    public List<PodaciORoditeljimaDTO> roditelji;

    public ProfilDetetaDTO(Dete dete) {
        if (dete.getFormular() != null) {
            this.ime = dete.getFormular().getImeDeteta();
            this.mestoRodjenja = dete.getFormular().getMestoRodjenja();
            this.opstina = dete.getFormular().getOpstinaRodjenja();
            this.drzava = dete.getFormular().getDrzava();
            this.zdravstveniProblemi = dete.getFormular().getZdravstveniProblemi();
            this.zdravstveniUslovi = dete.getFormular().getZdravstveniUslovi();
            this.datumRodjenja = dete.getFormular().getDatumRodjenja();
            if (dete.getFormular().getPodaciORoditeljimas() != null) {
                for (PodaciORoditeljima r : dete.getFormular().getPodaciORoditeljimas()) {
                    roditelji.add(new PodaciORoditeljimaDTO(r));
                }
            }
        }
        if (dete.getGrupa() != null) {
            this.grupa = dete.getGrupa().getTipGrupe() + "";
        }
        this.vaspitac = "";
        this.brojIzostanaka = 0;
        this.visina = dete.getVisina();
        this.tezina = dete.getTezina();
        this.roditelji = new ArrayList<>();
        this.id = dete.getId();
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMestoRodjenja() {
        return mestoRodjenja;
    }

    public void setMestoRodjenja(String mestoRodjenja) {
        this.mestoRodjenja = mestoRodjenja;
    }

    public String getOpstina() {
        return opstina;
    }

    public void setOpstina(String opstina) {
        this.opstina = opstina;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getZdravstveniProblemi() {
        return zdravstveniProblemi;
    }

    public void setZdravstveniProblemi(String zdravstveniProblemi) {
        this.zdravstveniProblemi = zdravstveniProblemi;
    }

    public String getZdravstveniUslovi() {
        return zdravstveniUslovi;
    }

    public void setZdravstveniUslovi(String zdravstveniUslovi) {
        this.zdravstveniUslovi = zdravstveniUslovi;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getVaspitac() {
        return vaspitac;
    }

    public void setVaspitac(String vaspitac) {
        this.vaspitac = vaspitac;
    }

    public int getBrojIzostanaka() {
        return brojIzostanaka;
    }

    public void setBrojIzostanaka(int brojIzostanaka) {
        this.brojIzostanaka = brojIzostanaka;
    }

    public String getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(String datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public int getTezina() {
        return tezina;
    }

    public void setTezina(int tezina) {
        this.tezina = tezina;
    }

    public List<PodaciORoditeljimaDTO> getRoditelji() {
        return roditelji;
    }

    public void setRoditelji(List<PodaciORoditeljimaDTO> roditelji) {
        this.roditelji = roditelji;
    }

    public ProfilDetetaDTO(
        Long id,
        String ime,
        String mestoRodjenja,
        String opstina,
        String drzava,
        String zdravstveniProblemi,
        String zdravstveniUslovi,
        String grupa,
        String vaspitac,
        int brojIzostanaka,
        String datumRodjenja,
        int visina,
        int tezina,
        List<PodaciORoditeljimaDTO> roditelji
    ) {
        this.ime = ime;
        this.mestoRodjenja = mestoRodjenja;
        this.opstina = opstina;
        this.drzava = drzava;
        this.zdravstveniProblemi = zdravstveniProblemi;
        this.zdravstveniUslovi = zdravstveniUslovi;
        this.grupa = grupa;
        this.vaspitac = vaspitac;
        this.brojIzostanaka = brojIzostanaka;
        this.datumRodjenja = datumRodjenja;
        this.visina = visina;
        this.tezina = tezina;
        this.roditelji = roditelji;
        this.id = id;
    }

    public ProfilDetetaDTO() {}
}
