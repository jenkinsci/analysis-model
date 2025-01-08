package edu.hm.hafner.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.util.PackageDetectorFactory;
import edu.hm.hafner.util.PackageDetectorFactory.FileSystemFacade;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link PackageNameResolver}.
 *
 * @author Ullrich Hafner
 */
class PackageNameResolverTest {
    private static final String FILE_NO_PACKAGE = "one.java";
    private static final String FILE_WITH_PACKAGE = "two.java";
    private static final Issue ISSUE_WITHOUT_PACKAGE = new IssueBuilder().setFileName(FILE_NO_PACKAGE).build();
    private static final Issue ISSUE_WITH_PACKAGE = new IssueBuilder().setFileName(FILE_WITH_PACKAGE)
            .setPackageName("existing")
            .build();

    @Test
    void shouldDoNothingForEmptyIssues() {
        var report = createIssues();

        var resolver = new PackageNameResolver();
        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(0);
    }

    @Test
    void shouldSkipExistingPackage() {
        var report = createIssues();
        report.add(ISSUE_WITH_PACKAGE);

        var resolver = new PackageNameResolver();
        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    @Test
    void shouldResolvePackage() throws IOException {
        var report = createIssues();
        report.add(ISSUE_WITHOUT_PACKAGE);

        var resolver = getResolver();

        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(1);
        assertThat(report.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
    }

    private PackageNameResolver getResolver() throws IOException {
        var detectors = PackageDetectorFactory.createPackageDetectors(createFileSystemStub());

        return new PackageNameResolver(detectors);
    }

    @Test
    void shouldResolvePackageAndSkipExistingPackage() throws IOException {
        var report = createIssues();
        report.add(ISSUE_WITHOUT_PACKAGE);
        report.add(ISSUE_WITH_PACKAGE);

        var resolver = getResolver();

        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
        assertThat(report.get(1)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    private FileSystemFacade createFileSystemStub() throws IOException {
        var fileSystemStub = mock(FileSystemFacade.class);
        when(fileSystemStub.openFile(FILE_NO_PACKAGE)).thenAnswer(
                r -> new ByteArrayInputStream("package a.name;".getBytes(StandardCharsets.UTF_8)));
        return fileSystemStub;
    }

    private Report createIssues() {
        return new Report();
    }
}
