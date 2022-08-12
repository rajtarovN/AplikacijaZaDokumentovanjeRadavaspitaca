package com.diplomski.myapp.web.rest.errors;

import org.zalando.problem.Status;

public class VaspitacAlreadyHaveDnevnikInGivenPeriodException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public VaspitacAlreadyHaveDnevnikInGivenPeriodException() {
        super(ErrorConstants.VASPITAC_ALREADY_HAVE_DNEVNIK, "Vapitac vec ima dnevnik  i grupu za dati period", "raspored-dece", "a");
    }
}
