package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NeDolasciTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NeDolasci.class);
        NeDolasci neDolasci1 = new NeDolasci();
        neDolasci1.setId(1L);
        NeDolasci neDolasci2 = new NeDolasci();
        neDolasci2.setId(neDolasci1.getId());
        assertThat(neDolasci1).isEqualTo(neDolasci2);
        neDolasci2.setId(2L);
        assertThat(neDolasci1).isNotEqualTo(neDolasci2);
        neDolasci1.setId(null);
        assertThat(neDolasci1).isNotEqualTo(neDolasci2);
    }
}
