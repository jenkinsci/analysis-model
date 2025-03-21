package edu.hm.hafner.analysis;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetectorRunner.FileSystemFacade;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.ResourceTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link ModuleDetectorRunner}.
 */
class ModuleDetectorTest extends ResourceTest {
    private static final String MANIFEST = "MANIFEST.MF";
    private static final Path ROOT = Path.of(File.pathSeparatorChar == ';' ? "C:\\Windows" : "/tmp");
    private static final String PREFIX = new PathUtil().getAbsolutePath(ROOT) + "/";

    private static final String PATH_PREFIX_MAVEN = "path/to/maven/";
    private static final String PATH_PREFIX_ANT = "path/to/ant/";

    private static final String EXPECTED_MAVEN_MODULE = "ADT Business Logic";
    private static final String EXPECTED_ANT_MODULE = "checkstyle";
    private static final String EXPECTED_OSGI_MODULE = "de.faktorlogik.prototyp";

    private InputStream read(final String fileName) {
        return asInputStream(fileName);
    }

    @Test
    void shouldIdentifyModuleIfThereAreMoreEntries() {
        var factory = createFileSystemStub(stub -> {
            var ant = PATH_PREFIX_ANT + AntModuleDetector.ANT_PROJECT;
            var maven = PATH_PREFIX_MAVEN + MavenModuleDetector.MAVEN_POM;
            when(stub.find(any(), anyString())).thenReturn(List.of(ant, maven));
            when(stub.open(PREFIX + ant)).thenAnswer(fileName -> read(AntModuleDetector.ANT_PROJECT));
            when(stub.open(PREFIX + maven)).thenAnswer(filename -> read(MavenModuleDetector.MAVEN_POM));
        });

        var detector = new ModuleDetectorRunner(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_ANT + "something.txt"))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_MAVEN + "something.txt"))
                .isEqualTo(EXPECTED_MAVEN_MODULE);
    }

    @Test
    void shouldEnsureThatMavenHasPrecedenceOverAnt() {
        var prefix = "/prefix/";
        var ant = prefix + AntModuleDetector.ANT_PROJECT;
        var maven = prefix + MavenModuleDetector.MAVEN_POM;

        verifyOrder(prefix, ant, maven, new String[]{ant, maven});
        verifyOrder(prefix, ant, maven, new String[]{maven, ant});
    }

    @Test
    void shouldEnsureThatOsgiHasPrecedenceOverMavenAndAnt() {
        var prefix = "/prefix/";
        var ant = prefix + AntModuleDetector.ANT_PROJECT;
        var maven = prefix + MavenModuleDetector.MAVEN_POM;
        var osgi = prefix + OsgiModuleDetector.OSGI_BUNDLE;

        verifyOrder(prefix, ant, maven, osgi, ant, maven, osgi);
        verifyOrder(prefix, ant, maven, osgi, ant, osgi, maven);
        verifyOrder(prefix, ant, maven, osgi, maven, ant, osgi);
        verifyOrder(prefix, ant, maven, osgi, maven, osgi, ant);
        verifyOrder(prefix, ant, maven, osgi, osgi, ant, maven);
        verifyOrder(prefix, ant, maven, osgi, osgi, maven, osgi);
    }

    @SuppressWarnings("PMD.UseVarargs")
    private void verifyOrder(final String prefix, final String ant, final String maven, final String[] foundFiles) {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(List.of(foundFiles));
            when(stub.open(ant)).thenAnswer(fileName -> read(AntModuleDetector.ANT_PROJECT));
            when(stub.open(maven)).thenAnswer(filename -> read(MavenModuleDetector.MAVEN_POM));
        });

        var detector = new ModuleDetectorRunner(ROOT, factory);

        assertThat(detector.guessModuleName(prefix + "something.txt")).isEqualTo(EXPECTED_MAVEN_MODULE);
    }

    private void verifyOrder(final String prefix, final String ant, final String maven, final String osgi,
            final String... foundFiles) {
        var fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(List.of(foundFiles));
            when(stub.open(ant)).thenAnswer(filename -> read(AntModuleDetector.ANT_PROJECT));
            when(stub.open(maven)).thenAnswer(filename -> read(MavenModuleDetector.MAVEN_POM));
            when(stub.open(osgi)).thenAnswer(filename -> read(MANIFEST));
            when(stub.open(prefix + "/" + OsgiModuleDetector.PLUGIN_PROPERTIES)).thenAnswer(filename -> createEmptyStream());
            when(stub.open(prefix + "/" + OsgiModuleDetector.BUNDLE_PROPERTIES)).thenAnswer(filename -> createEmptyStream());
        });

        var detector = new ModuleDetectorRunner(ROOT, fileSystem);

        assertThat(detector.guessModuleName(prefix + "something.txt")).isEqualTo(EXPECTED_OSGI_MODULE);
    }

    private InputStream createEmptyStream() {
        return IOUtils.toInputStream("", "UTF-8");
    }

    private FileSystemFacade createFileSystemStub(final Stub stub) {
        try {
            var fileSystem = mock(FileSystemFacade.class);
            stub.apply(fileSystem);
            return fileSystem;
        }
        catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    /**
     * Stubs the {@link FileSystemFacade} using a lambda.
     */
    @FunctionalInterface
    private interface Stub {
        void apply(FileSystemFacade f) throws IOException;
    }
}
