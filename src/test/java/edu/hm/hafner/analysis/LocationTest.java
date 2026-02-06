package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;

import nl.jqno.equalsverifier.EqualsVerifier;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link Location}.
 *
 * @author Akash Manna
 */
class LocationTest {
    private static final TreeStringBuilder BUILDER = new TreeStringBuilder();
    private static final String FILE_NAME = "test.cpp";
    private static final TreeString INTERNAL_FILE_NAME = BUILDER.intern(FILE_NAME);
    private static final int LINE_START = 10;
    private static final int LINE_END = 20;
    private static final int COLUMN_START = 5;
    private static final int COLUMN_END = 15;

    @Test
    void shouldCreateLocationWithAllParameters() {
        var location = new Location(INTERNAL_FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(COLUMN_START)
                .hasColumnEnd(COLUMN_END);
    }

    @Test
    void shouldCreateLocationWithLineRange() {
        var location = new Location(INTERNAL_FILE_NAME, LINE_START, LINE_END);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldCreateLocationWithSingleLine() {
        var location = new Location(INTERNAL_FILE_NAME, LINE_START);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_START)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldGenerateCorrectToString() {
        assertThat(new Location(INTERNAL_FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END))
                .hasToString("test.cpp:10-20:5-15");
        assertThat(new Location(INTERNAL_FILE_NAME, LINE_START, LINE_END))
                .hasToString("test.cpp:10-20");
        assertThat(new Location(INTERNAL_FILE_NAME, LINE_START))
                .hasToString("test.cpp:10");
        assertThat(new Location(INTERNAL_FILE_NAME, 0))
                .hasToString(FILE_NAME);
    }

    @Test
    void shouldFindLinesInsideAndOutsideOfLineRange() {
        var location = new Location(INTERNAL_FILE_NAME, 1, 2);

        assertThat(location.contains(0)).isFalse();
        assertThat(location.contains(1)).isTrue();
        assertThat(location.contains(2)).isTrue();
        assertThat(location.contains(3)).isFalse();
        assertThat(location).hasLineStart(1).hasLineEnd(2)
                .hasLines(1, 2).isNotSingleLine().hasToString("test.cpp:1-2");

        var wrongOrder = new Location(INTERNAL_FILE_NAME, 2, 1);
        assertThat(wrongOrder.contains(0)).isFalse();
        assertThat(wrongOrder.contains(1)).isTrue();
        assertThat(wrongOrder.contains(2)).isTrue();
        assertThat(wrongOrder.contains(3)).isFalse();
        assertThat(wrongOrder).hasLineStart(1).hasLineEnd(2)
                .hasLines(1, 2).isNotSingleLine().hasToString("test.cpp:1-2");

        var point = new Location(INTERNAL_FILE_NAME, 2);
        assertThat(point.contains(1)).isFalse();
        assertThat(point.contains(2)).isTrue();
        assertThat(point.contains(3)).isFalse();
        assertThat(point).hasLineStart(2).hasLineEnd(2)
                .hasLines(2).isSingleLine().hasToString("test.cpp:2");
    }

    @Test
    void shouldObeyEqualsContract() {
        EqualsVerifier.simple()
                .withPrefabValues(TreeString.class,
                        BUILDER.intern("file1.txt"),
                        BUILDER.intern("file2.txt"))
                .forClass(Location.class)
                .verify();
    }
}
