package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlanPriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanPrice.class);
        PlanPrice planPrice1 = new PlanPrice();
        planPrice1.setId(1L);
        PlanPrice planPrice2 = new PlanPrice();
        planPrice2.setId(planPrice1.getId());
        assertThat(planPrice1).isEqualTo(planPrice2);
        planPrice2.setId(2L);
        assertThat(planPrice1).isNotEqualTo(planPrice2);
        planPrice1.setId(null);
        assertThat(planPrice1).isNotEqualTo(planPrice2);
    }
}
