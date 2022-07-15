package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormularTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formular.class);
        Formular formular1 = new Formular();
        formular1.setId(1L);
        Formular formular2 = new Formular();
        formular2.setId(formular1.getId());
        assertThat(formular1).isEqualTo(formular2);
        formular2.setId(2L);
        assertThat(formular1).isNotEqualTo(formular2);
        formular1.setId(null);
        assertThat(formular1).isNotEqualTo(formular2);
    }
}
