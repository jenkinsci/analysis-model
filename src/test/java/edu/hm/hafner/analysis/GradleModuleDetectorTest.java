package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GradleModuleDetectorTest extends AbstractModuleDetectorTest {
    private static final String EXPECTED_GRADLE_MODULE_ROOT = "root-project";
    private static final String EXPECTED_GRADLE_MODULE_A = "a-module";
    private static final String EXPECTED_GRADLE_MODULE_B = "moduleB";
    private static final String EXPECTED_GRADLE_MODULE_ROOT_BY_PATH = "gradle";
    private static final String PATH_PREFIX_GRADLE = "path/to/gradle/";

    @Override
    String getPathPrefix() {
        return PATH_PREFIX_GRADLE;
    }

    @Override
    String getFileName() {
        return getPathPrefix() + "build/reports/something.txt";
    }

    @Override
    String getProjectFileName() {
        return GradleModuleDetector.SETTINGS_GRADLE;
    }

    @Test
    void shouldIdentifyModuleByReadingGradlePath() {
        var factory = createFileSystemStub(stub ->
                when(stub.find(any(), anyString())).thenReturn(
                        new String[]{PATH_PREFIX_GRADLE + GradleModuleDetector.BUILD_GRADLE}));

        var detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_GRADLE + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT_BY_PATH);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByFindingClosestGradlePath() {
        var factory = createFileSystemStub(stub ->
                when(stub.find(any(), anyString())).thenReturn(new String[]{
                        PATH_PREFIX_GRADLE + GradleModuleDetector.BUILD_GRADLE,
                        PATH_PREFIX_GRADLE + "moduleB/" + GradleModuleDetector.BUILD_GRADLE,
                        PATH_PREFIX_GRADLE + "a-module/" + GradleModuleDetector.BUILD_GRADLE,
                }));

        var detector = new ModuleDetector(ROOT, factory);
        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        assertThat(detector.guessModuleName(
                gradleWorkspace + "a-module/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_A);
        assertThat(detector.guessModuleName(
                gradleWorkspace + "moduleB/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_B);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT_BY_PATH);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingGradleKtsPath() {
        var factory = createFileSystemStub(stub ->
                when(stub.find(any(), anyString())).thenReturn(
                        new String[]{PATH_PREFIX_GRADLE + GradleModuleDetector.BUILD_GRADLE_KTS}));

        var detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_GRADLE + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT_BY_PATH);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingGradleSettings() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
                    PATH_PREFIX_GRADLE + "moduleB/" + GradleModuleDetector.BUILD_GRADLE,
                    PATH_PREFIX_GRADLE + "a-module/" + GradleModuleDetector.BUILD_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(filename -> read("settings-1.gradle"));
        });

        var detector = new ModuleDetector(ROOT, factory);
        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        assertThat(detector.guessModuleName(
                gradleWorkspace + "a-module/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_A);
        assertThat(detector.guessModuleName(
                gradleWorkspace + "moduleB/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_B);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldIdentifyModuleByReadingGradleSettingsKts() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE_KTS,
            });
            when(stub.open(anyString())).thenAnswer(filename -> read("settings-1.gradle"));
        });

        var detector = new ModuleDetector(ROOT, factory);
        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldEnsureThatGradleSettingsHasPrecedenceOverRootBuild() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.BUILD_GRADLE,
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
                    PATH_PREFIX_GRADLE + "moduleB/" + GradleModuleDetector.BUILD_GRADLE,
                    PATH_PREFIX_GRADLE + "a-module/" + GradleModuleDetector.BUILD_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(filename -> read("settings-1.gradle"));
        });

        var detector = new ModuleDetector(ROOT, factory);
        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        assertThat(detector.guessModuleName(
                gradleWorkspace + "a-module/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_A);
        assertThat(detector.guessModuleName(
                gradleWorkspace + "moduleB/build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_B);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT);
        assertThat(detector.guessModuleName(PREFIX + "build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }

    @Test
    void shouldEnsureThatGradleSettingsCanParseFormat1() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(fileName -> read("settings-1.gradle"));
        });

        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        var detector = new ModuleDetector(ROOT, factory);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT);
    }

    @Test
    void shouldEnsureThatGradleSettingsCanParseFormat2() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(fileName -> read("settings-2.gradle"));
        });

        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        var detector = new ModuleDetector(ROOT, factory);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo("root-project-2");
    }

    @Test
    void shouldEnsureThatGradleSettingsCanParseFormat3() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(fileName -> read("settings-3.gradle"));
        });

        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        var detector = new ModuleDetector(ROOT, factory);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo("root-project-3");
    }

    @Test
    void shouldEnsureThatGradleSettingsCanParseFormat4() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(fileName -> read("settings-4.gradle"));
        });

        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        var detector = new ModuleDetector(ROOT, factory);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo("root-project-4");
    }

    @Test
    void shouldIgnoreGradleSettingsWithoutProjectName() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{
                    PATH_PREFIX_GRADLE + GradleModuleDetector.BUILD_GRADLE,
                    PATH_PREFIX_GRADLE + GradleModuleDetector.SETTINGS_GRADLE,
            });
            when(stub.open(anyString())).thenAnswer(fileName -> read("settings-5.gradle"));
        });

        var gradleWorkspace = PREFIX + PATH_PREFIX_GRADLE;

        var detector = new ModuleDetector(ROOT, factory);
        assertThat(detector.guessModuleName(gradleWorkspace + "build/reports/something.txt"))
                .isEqualTo(EXPECTED_GRADLE_MODULE_ROOT_BY_PATH);
    }

    @Test
    void shouldIgnoreGradleFileWithNoParentPath() {
        var factory = createFileSystemStub(stub ->
                when(stub.find(any(), anyString())).thenReturn(new String[]{
                        GradleModuleDetector.BUILD_GRADLE,
                }));

        var detector = new ModuleDetector(ROOT_ABSOLUTE, factory);
        assertThat(detector.guessModuleName("build/reports/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }
}
