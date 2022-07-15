package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KomentarNaPricuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KomentarNaPricu.class);
        KomentarNaPricu komentarNaPricu1 = new KomentarNaPricu();
        komentarNaPricu1.setId(1L);
        KomentarNaPricu komentarNaPricu2 = new KomentarNaPricu();
        komentarNaPricu2.setId(komentarNaPricu1.getId());
        assertThat(komentarNaPricu1).isEqualTo(komentarNaPricu2);
        komentarNaPricu2.setId(2L);
        assertThat(komentarNaPricu1).isNotEqualTo(komentarNaPricu2);
        komentarNaPricu1.setId(null);
        assertThat(komentarNaPricu1).isNotEqualTo(komentarNaPricu2);
    }
}
