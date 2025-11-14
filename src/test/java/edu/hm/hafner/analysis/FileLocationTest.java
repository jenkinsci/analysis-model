package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FileLocation}.
 *
 * @author Akash Manna
 */
class FileLocationTest {
    private static final String FILE_NAME = "test.cpp";
    private static final int LINE_START = 10;
    private static final int LINE_END = 20;
    private static final int COLUMN_START = 5;
    private static final int COLUMN_END = 15;
    private static final String MESSAGE = "Referenced from here";

    @Test
    void shouldCreateFileLocationWithAllParameters() {
        var location = new FileLocation(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_END);
        assertThat(location.getColumnStart()).isEqualTo(COLUMN_START);
        assertThat(location.getColumnEnd()).isEqualTo(COLUMN_END);
        assertThat(location.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldCreateFileLocationWithLineRange() {
        var location = new FileLocation(FILE_NAME, LINE_START, LINE_END);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_END);
        assertThat(location.getColumnStart()).isZero();
        assertThat(location.getColumnEnd()).isZero();
        assertThat(location.getMessage()).isNull();
    }

    @Test
    void shouldCreateFileLocationWithSingleLine() {
        var location = new FileLocation(FILE_NAME, LINE_START);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_START);
        assertThat(location.getColumnStart()).isZero();
        assertThat(location.getColumnEnd()).isZero();
        assertThat(location.getMessage()).isNull();
    }

    @Test
    void shouldCreateFileLocationWithMessage() {
        var location = new FileLocation(FILE_NAME, LINE_START, MESSAGE);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_START);
        assertThat(location.getColumnStart()).isZero();
        assertThat(location.getColumnEnd()).isZero();
        assertThat(location.getMessage()).isEqualTo(MESSAGE);
    }

    @Test
    void shouldGenerateCorrectToString() {
        var location1 = new FileLocation(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);
        assertThat(location1.toString()).isEqualTo("test.cpp:10-20:5-15 - Referenced from here");

        var location2 = new FileLocation(FILE_NAME, LINE_START, LINE_END);
        assertThat(location2.toString()).isEqualTo("test.cpp:10-20");

        var location3 = new FileLocation(FILE_NAME, LINE_START);
        assertThat(location3.toString()).isEqualTo("test.cpp:10");

        var location4 = new FileLocation(FILE_NAME, 0);
        assertThat(location4.toString()).isEqualTo("test.cpp");
    }

    @Test
    void shouldBeEqual() {
        var location1 = new FileLocation(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);
        var location2 = new FileLocation(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);

        assertThat(location1).isEqualTo(location2);
        assertThat(location1.hashCode()).isEqualTo(location2.hashCode());
    }

    @Test
    void shouldNotBeEqual() {
        var location1 = new FileLocation(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);
        var location2 = new FileLocation("other.cpp", LINE_START, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);
        var location3 = new FileLocation(FILE_NAME, 5, LINE_END, COLUMN_START, COLUMN_END, MESSAGE);

        assertThat(location1).isNotEqualTo(location2);
        assertThat(location1).isNotEqualTo(location3);
    }
}