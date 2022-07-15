package com.diplomski.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.diplomski.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PodaciORoditeljimaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PodaciORoditeljima.class);
        PodaciORoditeljima podaciORoditeljima1 = new PodaciORoditeljima();
        podaciORoditeljima1.setId(1L);
        PodaciORoditeljima podaciORoditeljima2 = new PodaciORoditeljima();
        podaciORoditeljima2.setId(podaciORoditeljima1.getId());
        assertThat(podaciORoditeljima1).isEqualTo(podaciORoditeljima2);
        podaciORoditeljima2.setId(2L);
        assertThat(podaciORoditeljima1).isNotEqualTo(podaciORoditeljima2);
        podaciORoditeljima1.setId(null);
        assertThat(podaciORoditeljima1).isNotEqualTo(podaciORoditeljima2);
    }
}
