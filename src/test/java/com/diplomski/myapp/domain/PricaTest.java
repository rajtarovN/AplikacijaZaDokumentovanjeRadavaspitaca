package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prica.class);
        Prica prica1 = new Prica();
        prica1.setId(1L);
        Prica prica2 = new Prica();
        prica2.setId(prica1.getId());
        assertThat(prica1).isEqualTo(prica2);
        prica2.setId(2L);
        assertThat(prica1).isNotEqualTo(prica2);
        prica1.setId(null);
        assertThat(prica1).isNotEqualTo(prica2);
    }
}
