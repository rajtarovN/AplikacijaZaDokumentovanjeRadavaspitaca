package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoditeljTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roditelj.class);
        Roditelj roditelj1 = new Roditelj();
        roditelj1.setId(1L);
        Roditelj roditelj2 = new Roditelj();
        roditelj2.setId(roditelj1.getId());
        assertThat(roditelj1).isEqualTo(roditelj2);
        roditelj2.setId(2L);
        assertThat(roditelj1).isNotEqualTo(roditelj2);
        roditelj1.setId(null);
        assertThat(roditelj1).isNotEqualTo(roditelj2);
    }
}
