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
        Issues<Issue> issues = createIssues();

        PackageNameResolver resolver = new PackageNameResolver();
        Issues<Issue> resolved = resolver.run(issues, new IssueBuilder(), StandardCharsets.UTF_8);

        assertThat(resolved).hasSize(0);
        assertThat(resolved).hasId(ID);
    }

    @Test
    void shouldSkipExistingPackage() {
        Issues<Issue> issues = createIssues();
        issues.add(ISSUE_WITH_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver();
        Issues<Issue> resolved = resolver.run(issues, new IssueBuilder(), StandardCharsets.UTF_8);

        assertThat(resolved).hasSize(1);
        assertThat(resolved).hasId(ID);
        assertThat(resolved.get(0)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    @Test
    void shouldResolvePackage() throws IOException {
        Issues<Issue> issues = createIssues();
        issues.add(ISSUE_WITHOUT_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver(createFileSystemStub());

        Issues<Issue> resolved = resolver.run(issues, new IssueBuilder(), StandardCharsets.UTF_8);

        assertThat(resolved).hasSize(1);
        assertThat(resolved).hasId(ID);
        assertThat(resolved.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
    }

    @Test
    void shouldResolvePackageAndSkipExistingPackage() throws IOException {
        Issues<Issue> issues = createIssues();
        issues.add(ISSUE_WITHOUT_PACKAGE);
        issues.add(ISSUE_WITH_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver(createFileSystemStub());

        Issues<Issue> resolved = resolver.run(issues, new IssueBuilder(), StandardCharsets.UTF_8);

        assertThat(resolved).hasSize(2);
        assertThat(resolved).hasId(ID);
        assertThat(resolved.get(0)).hasFileName(FILE_NO_PACKAGE).hasPackageName("a.name");
        assertThat(resolved.get(1)).hasFileName(FILE_WITH_PACKAGE).hasPackageName("existing");
    }

    private FileSystem createFileSystemStub() throws IOException {
        FileSystem fileSystemStub = mock(FileSystem.class);
        when(fileSystemStub.openFile(FILE_NO_PACKAGE))
                .thenReturn(new ByteArrayInputStream("package a.name;".getBytes()));
        return fileSystemStub;
    }

    private Issues<Issue> createIssues() {
        Issues<Issue> issues = new Issues<>();
        issues.setId(ID);
        return issues;
    }
}