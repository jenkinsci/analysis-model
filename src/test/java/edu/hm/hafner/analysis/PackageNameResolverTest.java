package edu.hm.hafner.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
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
    private static final String ID = "ID";

    @Test
    void shouldDoNothingForEmptyIssues() {
        Report report = createIssues();

        PackageNameResolver resolver = new PackageNameResolver();
        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(0);
        assertThat(report).hasId(ID);
    }

    @Test
    void shouldSkipExistingPackage() {
        Report report = createIssues();
        report.add(ISSUE_WITH_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver();
        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(1);
        assertThat(report).hasId(ID);
        assertThat(report.get(0)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    @Test
    void shouldResolvePackage() throws IOException {
        Report report = createIssues();
        report.add(ISSUE_WITHOUT_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver(createFileSystemStub());

        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(1);
        assertThat(report).hasId(ID);
        assertThat(report.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
    }

    @Test
    void shouldResolvePackageAndSkipExistingPackage() throws IOException {
        Report report = createIssues();
        report.add(ISSUE_WITHOUT_PACKAGE);
        report.add(ISSUE_WITH_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver(createFileSystemStub());

        resolver.run(report, StandardCharsets.UTF_8);

        assertThat(report).hasSize(2);
        assertThat(report).hasId(ID);
        assertThat(report.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
        assertThat(report.get(1)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    private FileSystem createFileSystemStub() throws IOException {
        FileSystem fileSystemStub = mock(FileSystem.class);
        when(fileSystemStub.openFile(FILE_NO_PACKAGE))
                .thenReturn(new ByteArrayInputStream("package a.name;".getBytes(StandardCharsets.UTF_8)));
        return fileSystemStub;
    }

    private Report createIssues() {
        Report report = new Report();
        report.setId(ID);
        return report;
    }
}