package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.util.ResourceTest;

import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link JavaPackageDetector}.
 *
 * @author Ullrich Hafner
 */
class JavaPackageDetectorTest extends ResourceTest {
    @ParameterizedTest(name = "{index} => file={0}, expected package={1}")
    @CsvSource({
            "MavenJavaTest.txt, hudson.plugins.tasks.util",
            "complicated-package-declaration.txt, hudson.plugins.findbugs.util",
            "pom.xml, -",
            "ActionBinding.cs, -"})
    void shouldExtractPackageNameFromJavaSource(final String fileName, final String expectedPackage) throws IOException {
        try (var stream = asInputStream(fileName)) {
            assertThat(new JavaPackageDetector().detectPackageName(stream, UTF_8))
                    .isEqualTo(expectedPackage);
        }
    }

    @ParameterizedTest(name = "{index} => Invalid package name: {0}")
    @ValueSource(strings = {"package EDU.hm.hafner.analysis;", "package 0123.hm.hafner.analysis;"})
    void shouldSkipPackagesThatDoNotStartWithLowerCase(final String name) throws IOException {
        var detector = new JavaPackageDetector();

        assertThat(detector.detectPackageName(IOUtils.toInputStream(name, UTF_8), UTF_8)).isEqualTo("-");
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        var packageDetector = new JavaPackageDetector();
        assertThat(packageDetector.accepts("Action.java")).as("Does not accept a Java file.")
                .isTrue();
        assertThat(packageDetector.accepts("ActionBinding.cs")).as("Accepts a non-Java file.")
                .isFalse();
        assertThat(packageDetector.accepts("pom.xml")).as("Accepts a non-C# file.")
                .isFalse();
    }
}
