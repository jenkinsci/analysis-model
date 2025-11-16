//LocationTest.java
package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.TreeString;
import edu.hm.hafner.util.TreeStringBuilder;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_END);
        assertThat(location.getColumnStart()).isEqualTo(COLUMN_START);
        assertThat(location.getColumnEnd()).isEqualTo(COLUMN_END);
    }

    @Test
    void shouldCreateLocationWithLineRange() {
        var location = new Location(FILE_NAME, LINE_START, LINE_END);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_END);
        assertThat(location.getColumnStart()).isZero();
        assertThat(location.getColumnEnd()).isZero();
    }

    @Test
    void shouldCreateLocationWithSingleLine() {
        var location = new Location(FILE_NAME, LINE_START);

        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getLineStart()).isEqualTo(LINE_START);
        assertThat(location.getLineEnd()).isEqualTo(LINE_START);
        assertThat(location.getColumnStart()).isZero();
        assertThat(location.getColumnEnd()).isZero();
    }

    @Test
    void shouldGenerateCorrectToString() {
        var location1 = new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);
        assertThat(location1.toString()).isEqualTo("test.cpp:10-20:5-15");

        var location2 = new Location(FILE_NAME, LINE_START, LINE_END);
        assertThat(location2.toString()).isEqualTo("test.cpp:10-20");

        var location3 = new Location(FILE_NAME, LINE_START);
        assertThat(location3.toString()).isEqualTo("test.cpp:10");

        var location4 = new Location(FILE_NAME, 0);
        assertThat(location4.toString()).isEqualTo("test.cpp");
    }

    @Test
    void shouldBeEqual() {
        var location1 = new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);
        var location2 = new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);

        assertThat(location1).isEqualTo(location2);
        assertThat(location1.hashCode()).isEqualTo(location2.hashCode());
    }

    @Test
    void shouldNotBeEqual() {
        var location1 = new Location(FILE_NAME, LINE_START, LINE_END, COLUMN_START, COLUMN_END);
        var location2 = new Location(TREE_STRING_BUILDER.intern("other.cpp"), LINE_START, LINE_END, COLUMN_START, COLUMN_END);
        var location3 = new Location(FILE_NAME, 5, LINE_END, COLUMN_START, COLUMN_END);

        assertThat(location1).isNotEqualTo(location2);
        assertThat(location1).isNotEqualTo(location3);
    }
}
