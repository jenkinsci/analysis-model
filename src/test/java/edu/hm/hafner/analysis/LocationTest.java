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
    private static final TreeStringBuilder TREE_STRING_BUILDER = new TreeStringBuilder();
    private static final TreeString FILE_NAME = TREE_STRING_BUILDER.intern("test.cpp");
    private static final int LINE_START = 10;
    private static final int LINE_END = 20;
    private static final int COLUMN_START = 5;
    private static final int COLUMN_END = 15;

    @Test
    void shouldCreateLocationWithAllParameters() {
        var location = new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(COLUMN_START)
                .hasColumnEnd(COLUMN_END);
    }

    @Test
    void shouldCreateLocationWithLineRange() {
        var location = new Location(FILE_NAME, LINE_START, LINE_END);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_END)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldCreateLocationWithSingleLine() {
        var location = new Location(FILE_NAME, LINE_START);

        assertThat(location)
                .hasFileName(FILE_NAME)
                .hasLineStart(LINE_START)
                .hasLineEnd(LINE_START)
                .hasColumnStart(0)
                .hasColumnEnd(0);
    }

    @Test
    void shouldGenerateCorrectToString() {
        assertThat(new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END))
                .hasToString("test.cpp:10-20:5-15");
        assertThat(new Location(FILE_NAME, LINE_START, LINE_END))
                .hasToString("test.cpp:10-20");
        assertThat(new Location(FILE_NAME, LINE_START))
                .hasToString("test.cpp:10");
        assertThat(new Location(FILE_NAME, 0))
                .hasToString("test.cpp");
    }

    @Test
    void shouldObeyEqualsContract() {
        EqualsVerifier.simple()
                .withPrefabValues(TreeString.class,
                        TREE_STRING_BUILDER.intern("file1.txt"),
                        TREE_STRING_BUILDER.intern("file2.txt"))
                .forClass(Location.class)
                .verify();
    }
}
