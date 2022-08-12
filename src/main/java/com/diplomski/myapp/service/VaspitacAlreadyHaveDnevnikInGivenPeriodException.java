package com.diplomski.myapp.service;

public class VaspitacAlreadyHaveDnevnikInGivenPeriodException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VaspitacAlreadyHaveDnevnikInGivenPeriodException() {
        super("Vaspitac vec ima dnevnik u datom periodu!");
    }
}
