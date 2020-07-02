package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

/**
 * JMH Benchmarking of the {@link FingerprintGenerator}.
 *
 * @author Lion Kosiuk
 */
public class FingerprintGeneratorBenchmark extends AbstractBenchmark {
    // TODO: Add some meaningful content into the file to fingerprint
    private static final String AFFECTED_FILE_NAME = "fingerprint.txt";
    private static final Charset CHARSET_AFFECTED_FILE = StandardCharsets.UTF_8;

    /**
     * Benchmarking the {@link FingerprintGenerator} with one Issue.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 1)
    public void benchmarkingOneIssue() {
        FingerprintGenerator generator = new FingerprintGenerator();

        Report report = new Report();
        report.add(new IssueBuilder().build());

        generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);
    }

    /**
     * Benchmarking the {@link FingerprintGenerator} with multiple Issues.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 1)
    public void benchmarkingMultipleIssues() {
        FingerprintGenerator generator = new FingerprintGenerator();

        Report report = createMultipleIssues(10);

        generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);
    }

    private Report createMultipleIssues(final int number) {
        Report report = new Report();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        for (int i = 0; i < number; i++) {
            builder.setLineStart((int) (Math.random() * 26));
            report.add(builder.setPackageName(Integer.toString(i)).build());
        }
        return report;
    }

}
