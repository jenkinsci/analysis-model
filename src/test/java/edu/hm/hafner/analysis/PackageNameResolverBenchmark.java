package edu.hm.hafner.analysis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import edu.hm.hafner.analysis.PackageDetectors.FileSystem;

import static org.mockito.Mockito.*;

/**
 * JMH Benchmarking of the {@link PackageNameResolver}.
 *
 * @author Lion Kosiuk
 */
public class PackageNameResolverBenchmark extends AbstractBenchmark {
    private static final String FILE_NO_PACKAGE = "one.java";
    private static final String FILE_WITH_PACKAGE = "two.java";

    /**
     * Benchmarking the {@link PackageNameResolver} with 1000 Issues.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the report
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(value = 5, warmups = 5)
    public void benchmark1000IssuesTest(final BenchmarkState state) throws IOException {
        var resolver = new PackageNameResolver(createFileSystemStub());
        resolver.run(state.getReport(), StandardCharsets.UTF_8);
    }

    private FileSystem createFileSystemStub() throws IOException {
        var fileSystemStub = mock(FileSystem.class);
        when(fileSystemStub.openFile(FILE_NO_PACKAGE))
                .thenReturn(new ByteArrayInputStream("package a.name;".getBytes(StandardCharsets.UTF_8)));
        return fileSystemStub;
    }

    /**
     * State for the benchmark containing all preconfigured and necessary objects.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private Report report = new Report();

        public Report getReport() {
            return report;
        }

        /**
         * Initializes the reports.
         */
        @Setup(Level.Iteration)
        public void doSetup() {
            report = new Report();
            for (int i = 0; i < 1000; i++) {
                report.add(new IssueBuilder().setFileName(FILE_WITH_PACKAGE + i)
                        .setPackageName("existing")
                        .build());
            }
        }
    }
}
