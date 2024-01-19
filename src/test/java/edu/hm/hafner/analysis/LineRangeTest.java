package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

import static org.assertj.core.api.Assertions.*;

class LineRangeTest {
    @Test
    void shouldFindLinesInsideAndOutsideOfLineRange() {
        assertThat(new LineRange(1, 2).contains(0)).isFalse();
        assertThat(new LineRange(1, 2).contains(1)).isTrue();
        assertThat(new LineRange(1, 2).contains(2)).isTrue();
        assertThat(new LineRange(1, 2).contains(3)).isFalse();

        assertThat(new LineRange(2, 1).contains(0)).isFalse();
        assertThat(new LineRange(2, 1).contains(1)).isTrue();
        assertThat(new LineRange(2, 1).contains(2)).isTrue();
        assertThat(new LineRange(2, 1).contains(3)).isFalse();

        assertThat(new LineRange(2).contains(1)).isFalse();
        assertThat(new LineRange(2).contains(2)).isTrue();
        assertThat(new LineRange(2).contains(3)).isFalse();
    }

    @Test
    void shouldAdhereToEquals() {
        EqualsVerifier.forClass(LineRange.class).verify();
    }
}
