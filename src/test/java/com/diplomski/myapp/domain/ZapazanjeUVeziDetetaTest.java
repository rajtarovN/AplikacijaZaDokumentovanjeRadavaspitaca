package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ZapazanjeUVeziDetetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZapazanjeUVeziDeteta.class);
        ZapazanjeUVeziDeteta zapazanjeUVeziDeteta1 = new ZapazanjeUVeziDeteta();
        zapazanjeUVeziDeteta1.setId(1L);
        ZapazanjeUVeziDeteta zapazanjeUVeziDeteta2 = new ZapazanjeUVeziDeteta();
        zapazanjeUVeziDeteta2.setId(zapazanjeUVeziDeteta1.getId());
        assertThat(zapazanjeUVeziDeteta1).isEqualTo(zapazanjeUVeziDeteta2);
        zapazanjeUVeziDeteta2.setId(2L);
        assertThat(zapazanjeUVeziDeteta1).isNotEqualTo(zapazanjeUVeziDeteta2);
        zapazanjeUVeziDeteta1.setId(null);
        assertThat(zapazanjeUVeziDeteta1).isNotEqualTo(zapazanjeUVeziDeteta2);
    }
}
