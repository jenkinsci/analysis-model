package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.ResourceTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link JavaPackageDetector}.
 */
class JavaPackageDetectorTest extends ResourceTest {
    @Test
    void shouldExtractPackageNameFromJavaSource() throws IOException {
        try (InputStream stream = asInputStream("MavenJavaTest.txt")) {
            assertThat(new JavaPackageDetector().detectPackageName(stream))
                    .isEqualTo("hudson.plugins.tasks.util");
        }
    }

    @Test
    void shouldFindNoPackageInPomFile() throws IOException {
        try (InputStream stream = asInputStream("pom.xml")) {
            assertThat(new JavaPackageDetector().detectPackageName(stream))
                    .isEqualTo("-");
        }
    }

    @Test
    void shouldExtractPackageNameInFileWithComplicatedFormatting() throws IOException {
        try (InputStream stream = asInputStream("complicated-package-declaration.txt")) {
            assertThat(new JavaPackageDetector().detectPackageName(stream))
                    .isEqualTo("hudson.plugins.findbugs.util");
        }
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        JavaPackageDetector packageDetector = new JavaPackageDetector();
        assertThat(packageDetector.accepts("Action.java")).as("Does not accept a Java file.")
                .isTrue();
        assertThat(packageDetector.accepts("ActionBinding.cs")).as("Accepts a non-Java file.")
                .isFalse();
        assertThat(packageDetector.accepts("pom.xml")).as("Accepts a non-C# file.")
                .isFalse();
    }
}
