package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests the OsgiModuleDetector class.
 */
class OsgiModuleDetectorTest extends AbstractModuleDetectorTest {
    private static final String PATH_PREFIX_OSGI = "path/to/osgi/";
    private static final String EXPECTED_OSGI_MODULE = "de.faktorlogik.prototyp";

    @Override
    String getPathPrefix() {
        return PATH_PREFIX_OSGI;
    }

    @Override
    String getFileName() {
        return getPathPrefix() + "something.txt";
    }

    @Override
    String getProjectFileName() {
        return OsgiModuleDetector.OSGI_BUNDLE;
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundle() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_OSGI + OsgiModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "something.txt"))
                .isEqualTo(EXPECTED_OSGI_MODULE);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "in/between/something.txt"))
                .isEqualTo(EXPECTED_OSGI_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundleWithVendorInL10nProperties() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_OSGI + OsgiModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST), read("l10n.properties"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        String expectedName = "de.faktorlogik.prototyp (My Vendor)";
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "something.txt"))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "in/between/something.txt"))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingOsgiBundleWithManifestName() {
        FileSystem fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(
                    new String[]{PATH_PREFIX_OSGI + OsgiModuleDetector.OSGI_BUNDLE});
            when(stub.open(anyString())).thenReturn(read(MANIFEST_NAME), read("l10n.properties"));
        });

        ModuleDetector detector = new ModuleDetector(ROOT, fileSystem);

        String expectedName = "My Bundle";
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "something.txt"))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_OSGI + "in/between/something.txt"))
                .isEqualTo(expectedName);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }
}
