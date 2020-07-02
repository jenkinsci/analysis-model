package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Base class for JMH benchmark tests.
 *
 * @author Ullrich Hafner
 */
public class AbstractBenchmark {
    /**
     * BenchmarkRunner - runs all benchmark tests in the concrete test class.
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
