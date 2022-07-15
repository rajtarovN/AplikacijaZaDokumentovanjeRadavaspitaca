package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PotrebanMaterijalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotrebanMaterijal.class);
        PotrebanMaterijal potrebanMaterijal1 = new PotrebanMaterijal();
        potrebanMaterijal1.setId(1L);
        PotrebanMaterijal potrebanMaterijal2 = new PotrebanMaterijal();
        potrebanMaterijal2.setId(potrebanMaterijal1.getId());
        assertThat(potrebanMaterijal1).isEqualTo(potrebanMaterijal2);
        potrebanMaterijal2.setId(2L);
        assertThat(potrebanMaterijal1).isNotEqualTo(potrebanMaterijal2);
        potrebanMaterijal1.setId(null);
        assertThat(potrebanMaterijal1).isNotEqualTo(potrebanMaterijal2);
    }
}
