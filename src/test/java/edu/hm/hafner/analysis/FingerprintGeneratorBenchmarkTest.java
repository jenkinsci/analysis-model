package edu.hm.hafner.analysis;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import edu.hm.hafner.analysis.FingerprintGenerator;
import edu.hm.hafner.analysis.FullTextFingerprint;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

/**
 * JMH Benchmarking the class {@link FingerprintGenerator}.
 *
 * @author Lion Kosiuk
 */
public class FingerprintGeneratorBenchmarkTest {
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

    /**
     * Creates a specified number of issues
     * @param number
     * @return report
     */
    private Report createMultipleIssues(int number) {
        Report report = createIssues();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        for(int i = 0; i < number; i++) {
            builder.setLineStart((int) (Math.random()*26));
            report.add(builder.setPackageName(Integer.toString(i)).build());
        }
        return report;
    }

    /**
     * Creates issues
     * @return report
     */
    private Report createIssues() {
        return new Report();
    }
}
