package edu.hm.hafner.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import edu.hm.hafner.analysis.FingerprintGenerator;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.PackageDetectors.FileSystem;
import edu.hm.hafner.analysis.PackageNameResolver;
import edu.hm.hafner.analysis.Report;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

public class PackageNameResolverBenchmarkTest {

    private static final String FILE_NO_PACKAGE = "one.java";
    private static final String FILE_WITH_PACKAGE = "two.java";


    /**
     * Benchmarking the {@link PackageNameResolver} with 1000 Issues.
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(value = 5, warmups = 5)
    public void benchmark1000IssuesTest() throws IOException {
        Report report = createIssues();
        for(int i = 0; i < 1000; i++){
            report.add(new IssueBuilder().setFileName(FILE_WITH_PACKAGE + i)
                    .setPackageName("existing")
                    .build());
        }

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

    /**
     * BenchmarkRunner - runs all benchmark tests in this test class.
     * @throws Exception
     */
    @Test
    public void benchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .addProfiler(StackProfiler.class)
                .build();

        new Runner(opt).run();
    }
}
