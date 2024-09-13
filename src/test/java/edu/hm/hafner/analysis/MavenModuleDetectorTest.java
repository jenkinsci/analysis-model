package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MavenModuleDetectorTest extends AbstractModuleDetectorTest {
    private static final String PATH_PREFIX_MAVEN = "path/to/maven/";
    private static final String EXPECTED_MAVEN_MODULE = "ADT Business Logic";

    @Override
    String getPathPrefix() {
        return PATH_PREFIX_MAVEN;
    }

    @Override
    String getFileName() {
        return getPathPrefix() + "something.txt";
    }

    @Override
    String getProjectFileName() {
        return MavenModuleDetector.MAVEN_POM;
    }

    @Test
    void shouldIdentifyModuleByReadingMavenPom() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(
                    new String[]{PATH_PREFIX_MAVEN + MavenModuleDetector.MAVEN_POM});
            when(stub.open(anyString())).thenAnswer(fileName -> read(MavenModuleDetector.MAVEN_POM));
        });

        var detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_MAVEN + "something.txt")).isEqualTo(
                EXPECTED_MAVEN_MODULE);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_MAVEN + "in/between/something.txt")).isEqualTo(
                EXPECTED_MAVEN_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt")).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingMavenPomWithoutName() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_MAVEN + MavenModuleDetector.MAVEN_POM});
            when(stub.open(anyString())).thenAnswer(filename -> read("no-name-pom.xml"));
        });

        var detector = new ModuleDetector(ROOT, factory);

        String artifactId = "com.avaloq.adt.core";
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_MAVEN + "something.txt"))
                .isEqualTo(artifactId);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_MAVEN + "in/between/something.txt"))
                .isEqualTo(artifactId);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }
}
