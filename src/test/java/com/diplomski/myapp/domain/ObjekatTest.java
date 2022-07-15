package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObjekatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objekat.class);
        Objekat objekat1 = new Objekat();
        objekat1.setId(1L);
        Objekat objekat2 = new Objekat();
        objekat2.setId(objekat1.getId());
        assertThat(objekat1).isEqualTo(objekat2);
        objekat2.setId(2L);
        assertThat(objekat1).isNotEqualTo(objekat2);
        objekat1.setId(null);
        assertThat(objekat1).isNotEqualTo(objekat2);
    }
}
