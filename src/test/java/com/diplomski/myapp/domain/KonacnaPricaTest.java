package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KonacnaPricaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KonacnaPrica.class);
        KonacnaPrica konacnaPrica1 = new KonacnaPrica();
        konacnaPrica1.setId(1L);
        KonacnaPrica konacnaPrica2 = new KonacnaPrica();
        konacnaPrica2.setId(konacnaPrica1.getId());
        assertThat(konacnaPrica1).isEqualTo(konacnaPrica2);
        konacnaPrica2.setId(2L);
        assertThat(konacnaPrica1).isNotEqualTo(konacnaPrica2);
        konacnaPrica1.setId(null);
        assertThat(konacnaPrica1).isNotEqualTo(konacnaPrica2);
    }
}
