package edu.hm.hafner.analysis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import edu.hm.hafner.util.ResourceTest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link PackageDetectors}.
 *
 * @author Ullrich Hafner
 */
class PackageDetectorsTest extends ResourceTest {
    @ParameterizedTest(name = "{index} => file={0}, expected package={1}")
    @CsvSource({
            "MavenJavaTest.txt.java, hudson.plugins.tasks.util",
            "ActionBinding.cs, Avaloq.SmartClient.Utilities",
            "MavenJavaTest.txt, -",
            "pom.xml, -"})
    void shouldExtractPackageNames(final String fileName, final String expectedPackage) throws IOException {
        try (var stream = asInputStream(fileName)) {
            var fileSystem = mock(FileSystem.class);
            when(fileSystem.openFile(fileName)).thenReturn(stream);

            ArrayList<AbstractPackageDetector> detectors =  new ArrayList<>(Arrays.asList(
                    new JavaPackageDetector(fileSystem),
                    new CSharpNamespaceDetector(fileSystem),
                    new KotlinPackageDetector(fileSystem)
            ));

            assertThat(new PackageDetectors(detectors).detectPackageName(fileName, StandardCharsets.UTF_8))
                    .isEqualTo(expectedPackage);
        }
    }
}
