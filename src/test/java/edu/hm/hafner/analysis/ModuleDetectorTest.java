package edu.hm.hafner.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.ResourceTest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link ModuleDetector}.
 */
@SuppressFBWarnings("DMI")
class ModuleDetectorTest extends ResourceTest {
    private static final String MANIFEST = "MANIFEST.MF";
    private static final String MANIFEST_NAME = "MANIFEST-NAME.MF";
    private static final Path ROOT = Paths.get(File.pathSeparatorChar == ';' ? "C:\\Windows" : "/tmp");
    private static final String PREFIX = new PathUtil().getAbsolutePath(ROOT) + "/";

    private static final int NO_RESULT = 0;

    private static final String PATH_PREFIX_MAVEN = "path/to/maven";
    private static final String PATH_PREFIX_OSGI = "path/to/osgi";
    private static final String PATH_PREFIX_ANT = "path/to/ant";

    private static final String EXPECTED_MAVEN_MODULE = "ADT Business Logic";
    private static final String EXPECTED_ANT_MODULE = "checkstyle";
    private static final String EXPECTED_OSGI_MODULE = "de.faktorlogik.prototyp";

    private InputStream read(final String fileName) {
        return asInputStream(fileName);
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundle() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_OSGI + ModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST));
        });
        
        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/something.txt")))
                .isEqualTo(EXPECTED_OSGI_MODULE);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/in/between/something.txt")))
                .isEqualTo(EXPECTED_OSGI_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundleWithVendorInL10nProperties() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_OSGI + ModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST), read("l10n.properties"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        String expectedName = "de.faktorlogik.prototyp (My Vendor)";
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/something.txt")))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/in/between/something.txt")))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundleWithManifestName() {
        FileSystem fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(
                    new String[]{PATH_PREFIX_OSGI + ModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST_NAME), read("l10n.properties"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, fileSystem);

        String expectedName = "My Bundle";
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/something.txt")))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_OSGI + "/in/between/something.txt")))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingMavenPom() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(
                    new String[]{PATH_PREFIX_MAVEN + ModuleDetector.MAVEN_POM});
            when(stub.open(anyString())).thenAnswer(fileName -> read(ModuleDetector.MAVEN_POM));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/something.txt"))).isEqualTo(
                EXPECTED_MAVEN_MODULE);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/in/between/something.txt"))).isEqualTo(
                EXPECTED_MAVEN_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt")).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingMavenPomWithoutName() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_MAVEN + ModuleDetector.MAVEN_POM});
            when(stub.open(anyString())).thenAnswer(filename -> read("no-name-pom.xml"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        String artifactId = "com.avaloq.adt.core";
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/something.txt")))
                .isEqualTo(artifactId);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/in/between/something.txt")))
                .isEqualTo(artifactId);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingAntProjectFile() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_ANT + ModuleDetector.ANT_PROJECT});
            when(stub.open(anyString())).thenAnswer(filename -> read(ModuleDetector.ANT_PROJECT));
        });
        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "/something.txt")))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "/in/between/something.txt")))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "/path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIgnoreExceptionsDuringParsing() {
        FileSystem fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[NO_RESULT]);
            when(stub.open(anyString())).thenThrow(new FileNotFoundException("File not found"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, fileSystem);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "/something.txt")))
                .isEqualTo(StringUtils.EMPTY);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/something.txt")))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleIfThereAreMoreEntries() {
        FileSystem factory = createFileSystemStub(stub -> {
            String ant = PATH_PREFIX_ANT + ModuleDetector.ANT_PROJECT;
            String maven = PATH_PREFIX_MAVEN + ModuleDetector.MAVEN_POM;
            when(stub.find(any(), anyString())).thenReturn(new String[]{ant, maven});
            when(stub.open(PREFIX + ant)).thenReturn(read(ModuleDetector.ANT_PROJECT));
            when(stub.open(PREFIX + maven)).thenAnswer(filename -> read(ModuleDetector.MAVEN_POM));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "/something.txt")))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_MAVEN + "/something.txt")))
                .isEqualTo(EXPECTED_MAVEN_MODULE);
    }

    @Test
    void shouldEnsureThatMavenHasPrecedenceOverAnt() {
        String prefix = "/prefix/";
        String ant = prefix + ModuleDetector.ANT_PROJECT;
        String maven = prefix + ModuleDetector.MAVEN_POM;

        verifyOrder(prefix, ant, maven, new String[]{ant, maven});
        verifyOrder(prefix, ant, maven, new String[]{maven, ant});
    }

    @Test
    void shouldEnsureThatOsgiHasPrecedenceOverMavenAndAnt() {
        String prefix = "/prefix/";
        String ant = prefix + ModuleDetector.ANT_PROJECT;
        String maven = prefix + ModuleDetector.MAVEN_POM;
        String osgi = prefix + ModuleDetector.OSGI_BUNDLE;

        verifyOrder(prefix, ant, maven, osgi, ant, maven, osgi);
        verifyOrder(prefix, ant, maven, osgi, ant, osgi, maven);
        verifyOrder(prefix, ant, maven, osgi, maven, ant, osgi);
        verifyOrder(prefix, ant, maven, osgi, maven, osgi, ant);
        verifyOrder(prefix, ant, maven, osgi, osgi, ant, maven);
        verifyOrder(prefix, ant, maven, osgi, osgi, maven, osgi);
    }

    @SuppressWarnings("PMD.UseVarargs")
    private void verifyOrder(final String prefix, final String ant, final String maven, final String[] foundFiles) {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(foundFiles);
            when(stub.open(ant)).thenReturn(read(ModuleDetector.ANT_PROJECT));
            when(stub.open(maven)).thenAnswer(filename -> read(ModuleDetector.MAVEN_POM));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(prefix + "/something.txt")).isEqualTo(EXPECTED_MAVEN_MODULE);
    }

    private void verifyOrder(final String prefix, final String ant, final String maven, final String osgi,
            final String... foundFiles) {
        FileSystem fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(foundFiles);
            when(stub.open(ant)).thenAnswer(filename -> read(ModuleDetector.ANT_PROJECT));
            when(stub.open(maven)).thenAnswer(filename -> read(ModuleDetector.MAVEN_POM));
            when(stub.open(osgi)).thenAnswer(filename -> read(MANIFEST));
            when(stub.open(prefix + "/" + ModuleDetector.PLUGIN_PROPERTIES)).thenAnswer(filename -> createEmptyStream());
            when(stub.open(prefix + "/" + ModuleDetector.BUNDLE_PROPERTIES)).thenAnswer(filename -> createEmptyStream());
        });

        ModuleDetector detector = new ModuleDetector(ROOT, fileSystem);

        assertThat(detector.guessModuleName(prefix + "/something.txt")).isEqualTo(EXPECTED_OSGI_MODULE);
    }

    private InputStream createEmptyStream() {
        try {
            return IOUtils.toInputStream("", "UTF-8");
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private FileSystem createFileSystemStub(final Stub stub) {
        try {
            FileSystem fileSystem = mock(FileSystem.class);
            stub.apply(fileSystem);
            return fileSystem;
        }
        catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    /**
     * Stubs the {@link PackageDetectors.FileSystem} using a lambda.
     */
    @FunctionalInterface
    private interface Stub {
        void apply(FileSystem f) throws IOException;
    }
}
