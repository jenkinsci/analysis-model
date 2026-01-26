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
    private static final TreeString FILE_NAME_TREE = BUILDER.intern("test.cpp");
    private static final int LINE_START = 10;
    private static final int LINE_END = 20;
    private static final int COLUMN_START = 5;
    private static final int COLUMN_END = 15;

    @Test
    void shouldCreateLocationWithAllParameters() {
        assertThatLocationHasAllPropertiesSet(
                new Location(FILE_NAME_TREE, LINE_START, LINE_END, COLUMN_START, COLUMN_END));
        assertThatLocationHasAllPropertiesSet(
                new Location(FILE_NAME_TREE, LINE_END, LINE_START, COLUMN_START, COLUMN_END));
        assertThatLocationHasAllPropertiesSet(
                new Location(FILE_NAME_TREE, LINE_END, LINE_START, COLUMN_END, COLUMN_START));
    }

    private void assertThatLocationHasAllPropertiesSet(final Location location) {
        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(COLUMN_START)
                .hasColumnEnd(COLUMN_END);
    }

    @Test
    void shouldCreateLocationWithLines() {
        var location = new Location(FILE_NAME_TREE, LINE_START, LINE_END);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldCreateLocationWithSingleLine() {
        var location = new Location(FILE_NAME_TREE, LINE_START);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_START)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldGenerateCorrectToString() {
        assertThat(new Location(FILE_NAME_TREE, LINE_START, LINE_END, COLUMN_START, COLUMN_END))
                .hasToString("test.cpp:10-20:5-15");
        assertThat(new Location(FILE_NAME_TREE, LINE_START, LINE_END))
                .hasToString("test.cpp:10-20");
        assertThat(new Location(FILE_NAME_TREE, LINE_START))
                .hasToString("test.cpp:10");
        assertThat(new Location(FILE_NAME_TREE, 0))
                .hasToString("test.cpp");
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
