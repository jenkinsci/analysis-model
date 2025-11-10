package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.TreeString;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FileLocation}.
 *
 * @author Akash Manna
 */
class FileLocationTest {
    private static final String PATH = "/path/to";
    private static final String FILE_NAME = "file.cpp";

    @Test
    void shouldCreateFileLocation() {
        var location = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);

        assertThat(location.getPathName()).isEqualTo(PATH);
        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getAbsolutePath()).isEqualTo("/path/to/file.cpp");
        assertThat(location.getLineStart()).isEqualTo(10);
        assertThat(location.getLineEnd()).isEqualTo(20);
        assertThat(location.getColumnStart()).isEqualTo(5);
        assertThat(location.getColumnEnd()).isEqualTo(15);
    }

    @Test
    void shouldHandleEmptyPath() {
        var location = new FileLocation("", TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);

        assertThat(location.getPathName()).isEmpty();
        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getAbsolutePath()).isEqualTo(FILE_NAME);
    }

    @Test
    void shouldHandleNullPath() {
        var location = new FileLocation(null, TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);

        assertThat(location.getPathName()).isEmpty();
        assertThat(location.getFileName()).isEqualTo(FILE_NAME);
        assertThat(location.getAbsolutePath()).isEqualTo(FILE_NAME);
    }

    @Test
    void shouldNormalizeLineAndColumnRanges() {
        var location = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 20, 10, 15, 5);

        assertThat(location.getLineStart()).isEqualTo(10);
        assertThat(location.getLineEnd()).isEqualTo(20);
        assertThat(location.getColumnStart()).isEqualTo(5);
        assertThat(location.getColumnEnd()).isEqualTo(15);
    }

    @Test
    void shouldHandleZeroValues() {
        var location = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 0, 0, 0, 0);

        assertThat(location.getLineStart()).isEqualTo(0);
        assertThat(location.getLineEnd()).isEqualTo(0);
        assertThat(location.getColumnStart()).isEqualTo(0);
        assertThat(location.getColumnEnd()).isEqualTo(0);
    }

    @Test
    void shouldFormatToString() {
        var location = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);

        assertThat(location.toString()).isEqualTo("/path/to/file.cpp(10:5-20:15)");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        var location1 = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);
        var location2 = new FileLocation(PATH, TreeString.valueOf(FILE_NAME), 10, 20, 5, 15);
        var location3 = new FileLocation(PATH, TreeString.valueOf("other.cpp"), 10, 20, 5, 15);

        assertThat(location1).isEqualTo(location2);
        assertThat(location1).hasSameHashCodeAs(location2);
        assertThat(location1).isNotEqualTo(location3);
    }
}
