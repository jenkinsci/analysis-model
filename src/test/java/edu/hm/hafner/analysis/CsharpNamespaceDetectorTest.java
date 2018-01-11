package edu.hm.hafner.analysis;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.hm.hafner.util.ResourceTest;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link CsharpNamespaceDetector}.
 *
 * @author Ullrich Hafner
 */
class CsharpNamespaceDetectorTest extends ResourceTest {
    @ParameterizedTest(name = "{index} => file={0}, expected package={1}")
    @CsvSource({
            "ActionBinding.cs, Avaloq.SmartClient.Utilities",
            "ActionBinding-Original-Formatting.cs, Avaloq.SmartClient.Utilities",
            "pom.xml, -",
            "MavenJavaTest.txt, -"})
    void shouldExtractPackageNameFromJavaSource(final String fileName, final String expectedPackage) {
        try (Stream<String> stream = asStream(fileName)) {
            assertThat(new CsharpNamespaceDetector().detectPackageName(stream))
                    .isEqualTo(expectedPackage);
        }
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        CsharpNamespaceDetector namespaceDetector = new CsharpNamespaceDetector();
        assertThat(namespaceDetector.accepts("ActionBinding.cs"))
                .as("Does not accept a C# file.").isTrue();
        assertThat(namespaceDetector.accepts("ActionBinding.cs.c"))
                .as("Accepts a non-C# file.").isFalse();
        assertThat(namespaceDetector.accepts("Action.java"))
                .as("Accepts a non-C# file.").isFalse();
        assertThat(namespaceDetector.accepts("pom.xml"))
                .as("Accepts a non-C# file.").isFalse();
    }
}

