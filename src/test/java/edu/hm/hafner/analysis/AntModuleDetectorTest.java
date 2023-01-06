package edu.hm.hafner.analysis;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetector.FileSystem;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AntModuleDetectorTest extends AbstractModuleDetectorTest {
    private static final String PATH_PREFIX_ANT = "path/to/ant/";
    private static final String EXPECTED_ANT_MODULE = "checkstyle";

    @Override
    String getPathPrefix() {
        return PATH_PREFIX_ANT;
    }

    @Override
    String getFileName() {
        return getPathPrefix() + "something.txt";
    }

    @Override
    String getProjectFileName() {
        return AntModuleDetector.ANT_PROJECT;
    }

    @Test
    void shouldIdentifyModuleByReadingAntProjectFile() {
        FileSystem factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(new String[]{PATH_PREFIX_ANT + AntModuleDetector.ANT_PROJECT});
            when(stub.open(anyString())).thenAnswer(filename -> read(AntModuleDetector.ANT_PROJECT));
        });
        ModuleDetector detector = new ModuleDetector(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "something.txt")))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + (PATH_PREFIX_ANT + "in/between/something.txt")))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }
}