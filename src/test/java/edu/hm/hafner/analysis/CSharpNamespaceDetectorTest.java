package edu.hm.hafner.analysis;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.hm.hafner.util.ResourceTest;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link CSharpNamespaceDetector}.
 *
 * @author Ullrich Hafner
 */
class CSharpNamespaceDetectorTest extends ResourceTest {
    @ParameterizedTest(name = "{index} => file={0}, expected package={1}")
    @CsvSource({
            "ActionBinding.cs, Avaloq.SmartClient.Utilities",
            "ActionBinding-Original-Formatting.cs, Avaloq.SmartClient.Utilities",
            "Program.cs, ConsoleApplication1", // see Jenkins-48869
            "Class1.cs, ConsoleApplication1",  // see Jenkins-48869
            "pom.xml, -",
            "MavenJavaTest.txt, -"})
    void shouldExtractPackageNameFromJavaSource(final String fileName, final String expectedPackage) throws IOException {
        try (InputStream stream = asInputStream(fileName)) {
            assertThat(new CSharpNamespaceDetector().detectPackageName(stream, StandardCharsets.UTF_8))
                    .isEqualTo(expectedPackage);
        }
    }

    @Test
    void shouldAcceptCorrectFileSuffix() {
        var namespaceDetector = new CSharpNamespaceDetector();
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

