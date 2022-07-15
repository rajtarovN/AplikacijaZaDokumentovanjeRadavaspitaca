package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VaspitacTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vaspitac.class);
        Vaspitac vaspitac1 = new Vaspitac();
        vaspitac1.setId(1L);
        Vaspitac vaspitac2 = new Vaspitac();
        vaspitac2.setId(vaspitac1.getId());
        assertThat(vaspitac1).isEqualTo(vaspitac2);
        vaspitac2.setId(2L);
        assertThat(vaspitac1).isNotEqualTo(vaspitac2);
        vaspitac1.setId(null);
        assertThat(vaspitac1).isNotEqualTo(vaspitac2);
    }
}
