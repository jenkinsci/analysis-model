package edu.hm.hafner.analysis;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import edu.hm.hafner.util.LineRange;
import edu.hm.hafner.util.LineRangeList;

/**
 * Performance benchmarks for the class {@link IssueDifference}.
 *
 * @author Kevin Richter
 * @author Simon Schönwiese
 */
public class IssueDifferenceBenchmark extends AbstractBenchmark {
    private static final String CURRENT_BUILD = "2";

    /**
     * Benchmarking for the creation of a {@link IssueDifference}.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the predefined objects for the test
     * @param blackhole
     *         a {@link Blackhole} to avoid dead code elimination
     */
    @Benchmark
    public void benchmarkDeltaReportCreation(final BenchmarkState state, final Blackhole blackhole) {
        blackhole.consume(new IssueDifference(state.getCurrent(), CURRENT_BUILD, state.getReference()));
    }

    /**
     * State for the benchmark containing all preconfigured and necessary objects.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private static final String REFERENCE_BUILD = "100";

        private Report reference = new Report();
        private Report current = new Report();

        public Report getReference() {
            return reference;
        }

        public Report getCurrent() {
            return current;
        }

        /**
         * Initializes reports and history for the benchmarks.
         */
        @Setup(Level.Iteration)
        public void doSetup() {
            reference = new Report().addAll(
                    createIssue("OUTSTANDING 1", "OUT 1"),
                    createIssue("OUTSTANDING 2", "OUT 2"),
                    createIssue("OUTSTANDING 3", "OUT 3"),
                    createIssue("TO FIX 1", "FIX 1"),
                    createIssue("TO FIX 2", "FIX 2"));

            current = new Report().addAll(
                    createIssue("UPD OUTSTANDING 1", "OUT 1"),
                    createIssue("OUTSTANDING 2", "UPD OUT 2"),
                    createIssue("OUTSTANDING 3", "OUT 3"),
                    createIssue("NEW 1", "NEW 1"));
        }

        private Issue createIssue(final String message, final String fingerprint) {
            try (var builder = new IssueBuilder()) {
                builder.setFileName("file-name")
                        .setLineStart(1)
                        .setLineEnd(2)
                        .setColumnStart(3)
                        .setColumnEnd(4)
                        .setCategory("category")
                        .setType("type")
                        .setPackageName("package-name")
                        .setModuleName("module-name")
                        .setSeverity(Severity.WARNING_HIGH)
                        .setMessage(message)
                        .setDescription("description")
                        .setOrigin("origin")
                        .setLineRanges(new LineRangeList(List.of(new LineRange(5, 6))))
                        .setFingerprint(fingerprint)
                        .setReference(REFERENCE_BUILD);
                return builder.build();
            }
        }
    }
}
