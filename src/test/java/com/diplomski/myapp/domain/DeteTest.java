package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dete.class);
        Dete dete1 = new Dete();
        dete1.setId(1L);
        Dete dete2 = new Dete();
        dete2.setId(dete1.getId());
        assertThat(dete1).isEqualTo(dete2);
        dete2.setId(2L);
        assertThat(dete1).isNotEqualTo(dete2);
        dete1.setId(null);
        assertThat(dete1).isNotEqualTo(dete2);
    }
}
