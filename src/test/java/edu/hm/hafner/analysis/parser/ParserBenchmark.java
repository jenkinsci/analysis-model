package edu.hm.hafner.analysis.parser;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import edu.hm.hafner.analysis.AbstractBenchmark;
import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.parser.checkstyle.CheckStyleParser;
import edu.hm.hafner.analysis.parser.pmd.PmdParser;

/**
 * Performance benchmarks for analysis parsers parsing xml files.
 *
 * @author Kevin Richter
 * @author Simon Schönwiese
 */
@BenchmarkMode(Mode.AverageTime)
@Fork(value = 1, warmups = 3)
public class ParserBenchmark extends AbstractBenchmark {
    /**
     * Benchmarking for parsing an XML file with a {@link CheckStyleParser}.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the FileReaderFactory object
     * @param blackhole
     *         a {@link Blackhole} to avoid dead code elimination
     */
    @Benchmark
    public void benchmarkCheckStyleParser(final BenchmarkState state, final Blackhole blackhole) {
        Report report = new CheckStyleParser().parse(state.getCheckstyleFileReaderFactory());

        blackhole.consume(report);
    }

    /**
     * Benchmarking for parsing an xml file with a {@link PmdParser}.
     *
     * @param state
     *         a {@link BenchmarkState} object containing the FileReaderFactory object
     * @param blackhole
     *         a {@link Blackhole} to avoid dead code elminination
     */
    @Benchmark
    public void benchmarkPmdParser(final BenchmarkState state, final Blackhole blackhole) {
        Report report = new PmdParser().parse(state.getPmdFileReaderFactory());

        blackhole.consume(report);
    }

    /**
     * State for the benchmark containing all preconfigured and necessary objects.
     */
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        private ReaderFactory checkstyleFileReaderFactory;
        private ReaderFactory pmdFileReaderFactory;

        private ReaderFactory createFileReaderFactory(final String fileName) throws URISyntaxException {
            return new FileReaderFactory(
                    Paths.get(Objects.requireNonNull(BenchmarkState.class.getResource(fileName)).toURI()));
        }

        public ReaderFactory getCheckstyleFileReaderFactory() {
            return checkstyleFileReaderFactory;
        }

        public ReaderFactory getPmdFileReaderFactory() {
            return pmdFileReaderFactory;
        }

        /**
         * Initializes history and FileReaderFactory object for the benchmarks.
         */
        @Setup(Level.Iteration)
        public void doSetup() throws URISyntaxException {
            checkstyleFileReaderFactory = createFileReaderFactory("checkstyle/issue19122.xml");
            pmdFileReaderFactory = createFileReaderFactory("pmd/issue54736.xml");
        }
    }
}
