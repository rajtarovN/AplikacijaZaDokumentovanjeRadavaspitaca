package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grupa.class);
        Grupa grupa1 = new Grupa();
        grupa1.setId(1L);
        Grupa grupa2 = new Grupa();
        grupa2.setId(grupa1.getId());
        assertThat(grupa1).isEqualTo(grupa2);
        grupa2.setId(2L);
        assertThat(grupa1).isNotEqualTo(grupa2);
        grupa1.setId(null);
        assertThat(grupa1).isNotEqualTo(grupa2);
    }
}
