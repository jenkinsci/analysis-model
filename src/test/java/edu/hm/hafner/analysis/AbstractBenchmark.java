package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Base class for JMH benchmark tests.
 *
 * @author Ullrich Hafner
 */
@BenchmarkMode(Mode.All)
@Fork(value = 2, warmups = 2)
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod", "PMD.JUnit5TestShouldBePackagePrivate"})
public abstract class AbstractBenchmark {
    /**
     * BenchmarkRunner - runs all benchmark tests in the concrete test class.
     *
     * @throws RunnerException
     *         if the benchmark failed
     */
    @Test
    public void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .addProfiler(StackProfiler.class)
                .build();

        new Runner(opt).run();
    }
}
