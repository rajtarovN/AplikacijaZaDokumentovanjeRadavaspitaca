package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DnevnikTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dnevnik.class);
        Dnevnik dnevnik1 = new Dnevnik();
        dnevnik1.setId(1L);
        Dnevnik dnevnik2 = new Dnevnik();
        dnevnik2.setId(dnevnik1.getId());
        assertThat(dnevnik1).isEqualTo(dnevnik2);
        dnevnik2.setId(2L);
        assertThat(dnevnik1).isNotEqualTo(dnevnik2);
        dnevnik1.setId(null);
        assertThat(dnevnik1).isNotEqualTo(dnevnik2);
    }
}
