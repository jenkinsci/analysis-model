package edu.hm.hafner.analysis.benchmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

/**
 * JMH Benchmarking the serialization and deserialization of the class {@link Report}.
 *
 * @author Patrick Rogg
 */
public class ReportSerializationBenchmarkTest {
    private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final int ISSUE_COUNT = 1000;
    private static final Report REPORT = createReportWith(ISSUE_COUNT);
    private static final byte[] REPORT_AS_BYTES = toByteArray(REPORT);

    /**
     * Benchmarking the serialization of {@link Report}.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 1)
    public void benchmarkingReportSerialization(final Blackhole blackhole) {
        final byte[] bytes = toByteArray(REPORT);
        blackhole.consume(bytes);
    }

    /**
     * Benchmarking the deserialization of a byte array to a {@link Report}.
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 1)
    public void benchmarkingReportDeserialization(final Blackhole blackhole) {
        Report report = toReport(REPORT_AS_BYTES);
        blackhole.consume(report);
    }

    /**
     * BenchmarkRunner - runs all benchmark tests in this test class.
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
     *
     * @return report
     */
    private static Report createReportWith(final int number) {
        Report report = new Report();
        IssueBuilder builder = new IssueBuilder();
        builder.setFileName(AFFECTED_FILE_NAME);
        builder.setLineStart(5);

        for (int i = 0; i < number; i++) {
            report.add(builder.setPackageName(Integer.toString(i)).build());
        }

        return report;
    }

    /**
     * Converts a report to a byte array
     *
     * @return bytes
     */
    private static byte[] toByteArray(final Report report) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (ObjectOutputStream stream = new ObjectOutputStream(out)) {
            stream.writeObject(report);
        }
        catch (IOException exception) {
            throw new IllegalStateException("Can't serialize report " + report, exception);
        }

        return out.toByteArray();
    }

    /**
     * Converts a byte array to a report
     *
     * @return report
     */
    private static Report toReport(final byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);

        try (ObjectInputStream stream = new ObjectInputStream(in)) {
            return (Report) stream.readObject();
        }
        catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Can't deserialize byte array " + Arrays.toString(bytes), exception);
        }
    }
}
