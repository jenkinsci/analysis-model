package edu.hm.hafner.analysis;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AntModuleDetectorTest extends AbstractModuleDetectorTest {
    private static final String PATH_PREFIX_ANT = "path/to/ant/";
    private static final String EXPECTED_ANT_MODULE = "checkstyle";

    @Override
    String getPathPrefix() {
        return PATH_PREFIX_ANT;
    }

    @Override @SuppressFBWarnings("NM")
    String getFileName() {
        return getPathPrefix() + "something.txt";
    }

    @Override
    String getProjectFileName() {
        return AntModuleDetector.ANT_PROJECT;
    }

    @Test
    void shouldIdentifyModuleByReadingAntProjectFile() {
        var factory = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(List.of(PATH_PREFIX_ANT + AntModuleDetector.ANT_PROJECT));
            when(stub.open(anyString())).thenAnswer(filename -> read(AntModuleDetector.ANT_PROJECT));
        });
        var detector = new ModuleDetectorRunner(ROOT, factory);

        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_ANT + "something.txt"))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + PATH_PREFIX_ANT + "in/between/something.txt"))
                .isEqualTo(EXPECTED_ANT_MODULE);
        assertThat(detector.guessModuleName(PREFIX + "path/to/something.txt"))
                .isEqualTo(StringUtils.EMPTY);
    }
}
