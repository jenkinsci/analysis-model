package edu.hm.hafner.analysis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * JMH Benchmarking the serialization and deserialization of the class {@link Report}.
 *
 * @author Patrick Rogg
 */
public class ReportSerializationBenchmark extends AbstractBenchmark {
    private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final int ISSUE_COUNT = 1000;
    private static final Report REPORT = createReportWith(ISSUE_COUNT);
    private static final byte[] REPORT_AS_BYTES = toByteArray(REPORT);

    /**
     * Benchmarking the serialization of {@link Report}.
     *
     * @param blackhole
     *         the black hole that will consume the read bytes
     */
    @Benchmark
    public void benchmarkingReportSerialization(final Blackhole blackhole) {
        blackhole.consume(toByteArray(REPORT));
    }

    /**
     * Benchmarking the deserialization of a byte array to a {@link Report}.
     *
     * @param blackhole
     *         the black hole that will consume the created report
     */
    @Benchmark
    public void benchmarkingReportDeserialization(final Blackhole blackhole) {
        blackhole.consume(toReport(REPORT_AS_BYTES));
    }

    private static Report createReportWith(final int number) {
        try (var builder = new IssueBuilder()) {
            var report = new Report();
            builder.setFileName(AFFECTED_FILE_NAME);
            builder.setLineStart(5);

            for (int i = 0; i < number; i++) {
                report.add(builder.setPackageName(Integer.toString(i)).build());
            }
            return report;
        }
    }

    /**
     * Converts a report to a byte array.
     *
     * @param report
     *         the report to convert to a byte array
     *
     * @return bytes
     */
    private static byte[] toByteArray(final Report report) {
        try (var out = new ByteArrayOutputStream(); var stream = new ObjectOutputStream(out)) {
            stream.writeObject(report);
            return out.toByteArray();
        }
        catch (IOException exception) {
            throw new IllegalStateException("Can't serialize report " + report, exception);
        }
    }

    /**
     * Converts a byte array to a report.
     *
     * @param bytes
     *         the byte array to convert to a report
     *
     * @return report
     */
    @SuppressFBWarnings("OBJECT_DESERIALIZATION")
    @SuppressWarnings("BanSerializableRead")
    private static Report toReport(final byte[] bytes) {
        try (var in = new ByteArrayInputStream(bytes); var stream = new ObjectInputStream(in)) {
            return (Report) stream.readObject();
        }
        catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Can't deserialize byte array " + Arrays.toString(bytes), exception);
        }
    }
}
