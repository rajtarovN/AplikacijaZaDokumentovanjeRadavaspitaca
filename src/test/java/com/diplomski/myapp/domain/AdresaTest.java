package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdresaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adresa.class);
        Adresa adresa1 = new Adresa();
        adresa1.setId(1L);
        Adresa adresa2 = new Adresa();
        adresa2.setId(adresa1.getId());
        assertThat(adresa1).isEqualTo(adresa2);
        adresa2.setId(2L);
        assertThat(adresa1).isNotEqualTo(adresa2);
        adresa1.setId(null);
        assertThat(adresa1).isNotEqualTo(adresa2);
    }
}
