package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KorisnikTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Korisnik.class);
        Korisnik korisnik1 = new Korisnik();
        korisnik1.setId(1L);
        Korisnik korisnik2 = new Korisnik();
        korisnik2.setId(korisnik1.getId());
        assertThat(korisnik1).isEqualTo(korisnik2);
        korisnik2.setId(2L);
        assertThat(korisnik1).isNotEqualTo(korisnik2);
        korisnik1.setId(null);
        assertThat(korisnik1).isNotEqualTo(korisnik2);
    }
}
