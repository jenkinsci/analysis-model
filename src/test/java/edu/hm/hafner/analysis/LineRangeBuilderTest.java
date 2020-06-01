package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LineRangeBuilderTest {

    @Test
    void withLine() {
        LineRangeBuilder sut = new LineRangeBuilder();

        LineRange result = sut.withLine(5).build();
        LineRange expected = new LineRange(5);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void withLineThrowsIllegalArgumentNegativeLine() {
        LineRangeBuilder sut = new LineRangeBuilder();

        assertThatThrownBy(() -> sut.withLine(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withLineStartAndEnd() {
        LineRangeBuilder sut = new LineRangeBuilder();

        LineRange result = sut.withLineRange(3, 7).build();
        LineRange expected = new LineRange(3, 7);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void withLineStartAndEndThrowsIllegalArgumentStartNegativeNumber() {
        LineRangeBuilder sut = new LineRangeBuilder();

        assertThatThrownBy(() -> sut.withLineRange(-1, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withLineStartAndEndThrowsIllegalArgumentEndNegativeNumber() {
        LineRangeBuilder sut = new LineRangeBuilder();

        assertThatThrownBy(() -> sut.withLineRange(5, -5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withLineStartAndEndThrowsIllegalArgumentEndSmallerStart() {
        LineRangeBuilder sut = new LineRangeBuilder();

        assertThatThrownBy(() -> sut.withLineRange(5, 3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
