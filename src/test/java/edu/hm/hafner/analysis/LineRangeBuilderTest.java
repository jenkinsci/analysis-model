package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class LineRangeBuilderTest {

    private static final int START = 1;
    private static final int END = 2;
    private static final int LINE = 3;
    private static final int ZERO = 0;
    private static final int NEGATIVE = -1;

    @Test
    void shouldHaveTheSameFieldsWithLineRange() {
        LineRange sut1 = new LineRange(START, END);
        LineRange sut2 = new LineRangeBuilder()
                .setLineRange(START, END)
                .build();

        assertThat(sut1.equals(sut2)).isTrue();
    }

    @Test
    void shouldHaveTheSameFieldsWithLine() {
        LineRange sut1 = new LineRange(LINE, LINE);
        LineRange sut2 = new LineRangeBuilder()
                .setLine(LINE)
                .build();

        assertThat(sut1.equals(sut2)).isTrue();
    }

    @Test
    void shouldHaveSameValueWhenLine() {
        LineRange sut = new LineRangeBuilder()
                .setLine(LINE)
                .build();

        assertThat(sut.getStart()).isEqualTo(LINE);
        assertThat(sut.getEnd()).isEqualTo(LINE);
    }

    @Test
    void shouldHaveSameValueWhenLineRange() {
        LineRange sut = new LineRangeBuilder()
                .setLineRange(START, END)
                .build();

        assertThat(sut.getStart()).isEqualTo(START);
        assertThat(sut.getEnd()).isEqualTo(END);
    }

    @Test
    void shouldHaveZeroWhenStartNegative() {
        LineRange sut = new LineRangeBuilder()
                .setLineRange(NEGATIVE, END)
                .build();

        assertThat(sut.getStart()).isZero();
        assertThat(sut.getEnd()).isZero();
    }

    @Test
    void shouldHaveZeroWhenStartZero() {
        LineRange sut = new LineRangeBuilder()
                .setLineRange(ZERO, END)
                .build();

        assertThat(sut.getStart()).isZero();
        assertThat(sut.getEnd()).isZero();
    }

    @Test
    void shouldHaveZeroWhenLineNegative() {
        LineRange sut = new LineRangeBuilder()
                .setLine(NEGATIVE)
                .build();

        assertThat(sut.getStart()).isZero();
        assertThat(sut.getEnd()).isZero();
    }

    @Test
    void shouldHaveZeroWhenLineZero() {
        LineRange sut = new LineRangeBuilder()
                .setLine(ZERO)
                .build();

        assertThat(sut.getStart()).isZero();
        assertThat(sut.getEnd()).isZero();
    }

    @Test
    void shouldChangeStartAndEndWhenStartGreaterEnd() {
        LineRange sut = new LineRangeBuilder()
                .setLineRange(END, START)
                .build();

        assertThat(sut.getStart()).isEqualTo(START);
        assertThat(sut.getEnd()).isEqualTo(END);
    }
}
