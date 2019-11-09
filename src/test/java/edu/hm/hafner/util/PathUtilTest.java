package edu.hm.hafner.util;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assumptions.*;

/**
 * Tests the class {@link PathUtil}.
 *
 * @author Ullrich Hafner
 */
@SuppressFBWarnings("DMI")
class PathUtilTest extends ResourceTest {
    private static final String NOT_EXISTING = "/should/not/exist";
    private static final String ILLEGAL = "\0 Null-Byte";
    private static final String FILE_NAME = "fileName.txt";

    @DisplayName("Should be absolute path")
    @ParameterizedTest(name = "[{index}] path={0}")
    @ValueSource(strings = {"/", "/tmp", "C:\\", "C:\\Tmp"})
    void shouldFindAbsolutePaths(final String path) {
        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.isAbsolute(path)).isTrue();
    }

    @Test
    void shouldReturnFallback() {
        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.getAbsolutePath(NOT_EXISTING)).isEqualTo(NOT_EXISTING);
        assertThat(pathUtil.getAbsolutePath("C:\\should\\not\\exist")).isEqualTo("C:" + NOT_EXISTING);
        assertThat(pathUtil.getAbsolutePath(ILLEGAL)).isEqualTo(ILLEGAL);
    }

    @Test
    void shouldConvertToAbsolute() {
        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.createAbsolutePath(null, FILE_NAME)).isEqualTo(FILE_NAME);
        assertThat(pathUtil.createAbsolutePath("", FILE_NAME)).isEqualTo(FILE_NAME);
        assertThat(pathUtil.createAbsolutePath("/", FILE_NAME)).isEqualTo("/" + FILE_NAME);
        assertThat(pathUtil.createAbsolutePath("/tmp", FILE_NAME)).isEqualTo("/tmp/" + FILE_NAME);
        assertThat(pathUtil.createAbsolutePath("/tmp/", FILE_NAME)).isEqualTo("/tmp/" + FILE_NAME);
    }

    @Test
    void shouldSkipAlreadyAbsoluteOnUnix() {
        assumeThat(isWindows()).isFalse();

        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.createAbsolutePath("/tmp/", "/tmp/file.txt")).isEqualTo("/tmp/file.txt");
    }

    @Test
    void shouldSkipAlreadyAbsoluteOnWindows() {
        assumeThat(isWindows()).isTrue();

        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.createAbsolutePath("C:\\tmp", "C:\\tmp\\file.txt")).isEqualTo("C:/tmp/file.txt");
    }

    @Test
    void normalizeDriveLetter() {
        PathUtil pathUtil = new PathUtil();

        assertThat(pathUtil.getAbsolutePath("c:\\tmp")).isEqualTo("C:/tmp");
    }

    @Test
    void stayInSymbolicLinks() throws IOException {
        Path current = Paths.get(".");
        Path real = current.toRealPath();
        Path realWithSymbolic = current.toRealPath(LinkOption.NOFOLLOW_LINKS);
        assumeThat(real).isNotEqualTo(realWithSymbolic);

        String fromUtil = new PathUtil().getAbsolutePath(current);
        String unixStyle = realWithSymbolic.toString().replace('\\', '/');
        assertThat(fromUtil).isEqualTo(unixStyle);
    }
}
