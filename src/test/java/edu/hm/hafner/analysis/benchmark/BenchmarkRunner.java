package edu.hm.hafner.analysis.benchmark;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


public class BenchmarkRunner {

    @Test
    public void benchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(PackageNameResolverBenchmarkTest.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
