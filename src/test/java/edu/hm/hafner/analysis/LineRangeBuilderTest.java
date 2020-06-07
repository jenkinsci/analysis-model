package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

class LineRangeBuilderTest {

    private LineRangeBuilder createSut() {
        return new LineRangeBuilder();
    }

    @Test
    public void setLineTest0() {
        LineRangeBuilder sut = createSut().setLine(0);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setLineTest1() {
        LineRangeBuilder sut = createSut().setLine(1);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(1);
        assertThat(result.getEnd()).isEqualTo(1);
    }

    @Test
    public void setLineTest2() {
        LineRangeBuilder sut = createSut().setLine(-1);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setStartEndTest0() {
        LineRangeBuilder sut = createSut().setStart(0).setEnd(0);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setStartEndTest1() {
        LineRangeBuilder sut = createSut().setStart(-1).setEnd(0);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setStartEndTest2() {
        LineRangeBuilder sut = createSut().setStart(0).setEnd(-1);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setStartEndTest3() {
        LineRangeBuilder sut = createSut().setStart(1).setEnd(0);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(1);
    }

    @Test
    public void setStartEndTest4() {
        LineRangeBuilder sut = createSut().setStart(0).setEnd(2);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(0);
        assertThat(result.getEnd()).isEqualTo(0);
    }

    @Test
    public void setStartEndTest5() {
        LineRangeBuilder sut = createSut().setStart(3).setEnd(5);

        LineRange result = sut.build();

        assertThat(result.getStart()).isEqualTo(3);
        assertThat(result.getEnd()).isEqualTo(5);
    }
}