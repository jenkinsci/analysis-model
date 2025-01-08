package edu.hm.hafner.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.ModuleDetectorRunner.FileSystemFacade;
import edu.hm.hafner.util.PathUtil;
import edu.hm.hafner.util.ResourceTest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

abstract class AbstractModuleDetectorTest extends ResourceTest {
    static final Path ROOT = Path.of(File.pathSeparatorChar == ';' ? "C:\\Windows" : "/tmp");
    static final Path ROOT_ABSOLUTE = Path.of(File.pathSeparatorChar == ';' ? "C:\\" : "/");
    static final String PREFIX = new PathUtil().getAbsolutePath(ROOT) + "/";
    static final String MANIFEST = "MANIFEST.MF";
    static final String MANIFEST_NAME = "MANIFEST-NAME.MF";

    @Test
    void shouldIgnoreExceptionsDuringParsing() {
        var fileSystem = createFileSystemStub(stub -> {
            when(stub.find(any(), anyString())).thenReturn(List.of(getPathPrefix() + getProjectFileName()));
            when(stub.open(anyString())).thenThrow(new FileNotFoundException("File not found"));
        });

        var detector = new ModuleDetectorRunner(ROOT, fileSystem);

        assertThat(detector.guessModuleName(PREFIX + getFileName())).isEqualTo(StringUtils.EMPTY);
    }

    abstract String getPathPrefix();

    abstract String getFileName();

    abstract String getProjectFileName();

    protected FileSystemFacade createFileSystemStub(final Stub stub) {
        try {
            var fileSystem = mock(FileSystemFacade.class);
            stub.apply(fileSystem);
            return fileSystem;
        }
        catch (IOException exception) {
            throw new AssertionError(exception);
        }
    }

    protected InputStream read(final String fileName) {
        return asInputStream(fileName);
    }

    /**
     * Stubs the {@link FileSystemFacade} using a lambda.
     */
    @FunctionalInterface
    protected interface Stub {
        void apply(FileSystemFacade f) throws IOException;
    }
}
