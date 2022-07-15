package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PedagogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pedagog.class);
        Pedagog pedagog1 = new Pedagog();
        pedagog1.setId(1L);
        Pedagog pedagog2 = new Pedagog();
        pedagog2.setId(pedagog1.getId());
        assertThat(pedagog1).isEqualTo(pedagog2);
        pedagog2.setId(2L);
        assertThat(pedagog1).isNotEqualTo(pedagog2);
        pedagog1.setId(null);
        assertThat(pedagog1).isNotEqualTo(pedagog2);
    }
}
