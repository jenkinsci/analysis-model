package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * JMH Benchmarking of the {@link FingerprintGenerator}.
 *
 * @author Lion Kosiuk
 */
public class FingerprintGeneratorBenchmark extends AbstractBenchmark {
    private static final Charset CHARSET_AFFECTED_FILE = StandardCharsets.UTF_8;

    /**
     * Benchmarking the {@link FingerprintGenerator} with one issue.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the report
     */
    @Benchmark
    public void benchmarkingOneIssue(final BenchmarkState state) {
        var generator = new FingerprintGenerator();

        generator.run(state.getFullTextFingerprint(), state.getSingleIssueReport(), CHARSET_AFFECTED_FILE);
    }

    /**
     * Benchmarking the {@link FingerprintGenerator} with multiple issues.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the report
     */
    @Benchmark
    public void benchmarkingMultipleIssues(final BenchmarkState state) {
        var generator = new FingerprintGenerator();

        generator.run(new FullTextFingerprint(), state.getMultipleIssuesReport(), CHARSET_AFFECTED_FILE);
    }

    /**
     * State for the benchmark containing all preconfigured and necessary objects.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        // TODO: Add some meaningful content into the file to fingerprint
        private static final String AFFECTED_FILE_NAME = "fingerprint.txt";

        private Report singleIssueReport = new Report();
        private FullTextFingerprint fingerprint = new FullTextFingerprint();
        private Report multipleIssuesReport = new Report();
        @SuppressWarnings("NullAway")
        private Random random;

        public Report getSingleIssueReport() {
            return singleIssueReport;
        }

        public Report getMultipleIssuesReport() {
            return multipleIssuesReport;
        }

        public FullTextFingerprint getFullTextFingerprint() {
            return fingerprint;
        }

        /**
         * Initializes the reports.
         */
        @Setup(Level.Iteration)
        public void doSetup() {
            singleIssueReport = new Report();
            singleIssueReport.add(new IssueBuilder().build());

            multipleIssuesReport = createMultipleIssues(10);

            fingerprint = new FullTextFingerprint();
            random = new Random();
        }

        @SuppressFBWarnings("PREDICTABLE_RANDOM")
        private Report createMultipleIssues(final int number) {
            var report = new Report();
            try (var builder = new IssueBuilder()) {
                builder.setFileName(AFFECTED_FILE_NAME);
                for (int i = 0; i < number; i++) {
                    builder.setLineStart(random.nextInt() * 26);
                    report.add(builder.setPackageName(Integer.toString(i)).build());
                }
            }
            return report;
        }
    }
}
