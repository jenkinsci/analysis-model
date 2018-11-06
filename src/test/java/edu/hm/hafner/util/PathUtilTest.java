package edu.hm.hafner.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the class {@link PathUtil}.
 *
 * @author Ullrich Hafner
 */
class PathUtilTest {
    private static final String NOT_EXISTING = "/should/not/exist";
    private static final String ILLEGAL = "\0 Null-Byte";

    @Test
    void shouldReturnFallback() {
        PathUtil pathUtil = new PathUtil();
        
        assertThat(pathUtil.getAbsolutePath(NOT_EXISTING)).isEqualTo(NOT_EXISTING);
        assertThat(pathUtil.getAbsolutePath("C:\\should\\not\\exist")).isEqualTo("C:" + NOT_EXISTING);
        assertThat(pathUtil.getAbsolutePath(ILLEGAL)).isEqualTo(ILLEGAL);
    }
}