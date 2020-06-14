package edu.hm.hafner.analysis.benchmark;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import edu.hm.hafner.analysis.FingerprintGenerator;
import edu.hm.hafner.analysis.FullTextFingerprint;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;


public class FingerprintGeneratorBenchmarkTest {

    //private static final String AFFECTED_FILE_NAME = "file.txt";
    private static final Charset CHARSET_AFFECTED_FILE = StandardCharsets.UTF_8;



    @Benchmark
    public void init() throws IOException {
        FingerprintGenerator generator = new FingerprintGenerator();

        Report report = new Report();
        report.add(new IssueBuilder().build());

        generator.run(new FullTextFingerprint(), report, CHARSET_AFFECTED_FILE);
    }

    @Test
    public void benchmark() throws Exception {
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .addProfiler(StackProfiler.class)
                .build();

        new Runner(opt).run();
    }
}
