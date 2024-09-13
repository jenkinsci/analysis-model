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
 * Tests the class {@link KotlinPackageDetector}.
 *
 * @author Bastian Kersting
 */
class KotlinPackageDetectorTest extends ResourceTest {
    @ParameterizedTest(name = "{index} => file={0}, expected package={1}")
    @CsvSource({
            "MavenKotlinTest.txt, edu.hm.kersting",
            "complicated-package-declaration-kotlin.txt, edu.hm.kersting",
            "pom.xml, -",
            "ActionBinding.cs, -"})
    void shouldExtractPackageNameFromKotlinSource(final String fileName, final String expectedPackage) throws IOException {
        try (InputStream stream = asInputStream(fileName)) {
            assertThat(new KotlinPackageDetector().detectPackageName(stream, UTF_8))
                    .isEqualTo(expectedPackage);
        }
    }

    @ParameterizedTest(name = "{index} => Invalid package name: {0}")
    @ValueSource(strings = {"package EDU.hm.hafner.analysis;", "package 0123.hm.hafner.analysis;"})
    void shouldSkipPackagesThatDoNotStartWithLowerCase(final String name) throws IOException {
        var detector = new KotlinPackageDetector();

        assertThat(detector.detectPackageName(IOUtils.toInputStream(name, UTF_8), UTF_8)).isEqualTo("-");
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        var packageDetector = new KotlinPackageDetector();
        assertThat(packageDetector.accepts("Action.kt")).as("Does not accept a Kotlin file.")
                .isTrue();
        assertThat(packageDetector.accepts("ActionBinding.cs")).as("Accepts a non-Java file.")
                .isFalse();
        assertThat(packageDetector.accepts("pom.xml")).as("Accepts a non-C# file.")
                .isFalse();
    }
}
