package edu.hm.hafner.analysis.benchmark;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.openjdk.jmh.annotations.Benchmark;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import edu.hm.hafner.analysis.PackageNameResolver;
import edu.hm.hafner.analysis.Report;

import static org.mockito.Mockito.*;

public class PackageNameResolverBenchmarkTest {

    private static final String FILE_NO_PACKAGE = "one.java";
    private static final String FILE_WITH_PACKAGE = "two.java";
    private static final Issue ISSUE_WITHOUT_PACKAGE = new IssueBuilder().setFileName(FILE_NO_PACKAGE).build();
    private static final Issue ISSUE_WITH_PACKAGE = new IssueBuilder().setFileName(FILE_WITH_PACKAGE)
            .setPackageName("existing")
            .build();



    @Benchmark
    public void init() throws IOException {
        Report report = createIssues();
        report.add(ISSUE_WITHOUT_PACKAGE);
        report.add(ISSUE_WITH_PACKAGE);

        PackageNameResolver resolver = new PackageNameResolver(createFileSystemStub());

        resolver.run(report, StandardCharsets.UTF_8);

    }

    private FileSystem createFileSystemStub() throws IOException {
        FileSystem fileSystemStub = mock(FileSystem.class);
        when(fileSystemStub.openFile(FILE_NO_PACKAGE))
                .thenReturn(new ByteArrayInputStream("package a.name;".getBytes(StandardCharsets.UTF_8)));
        return fileSystemStub;
    }

    private Report createIssues() {
        return new Report();
    }
}
