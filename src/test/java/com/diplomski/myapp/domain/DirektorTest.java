package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirektorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direktor.class);
        Direktor direktor1 = new Direktor();
        direktor1.setId(1L);
        Direktor direktor2 = new Direktor();
        direktor2.setId(direktor1.getId());
        assertThat(direktor1).isEqualTo(direktor2);
        direktor2.setId(2L);
        assertThat(direktor1).isNotEqualTo(direktor2);
        direktor1.setId(null);
        assertThat(direktor1).isNotEqualTo(direktor2);
    }
}
