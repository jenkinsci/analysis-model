package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link ModuleResolver}.
 *
 * @author Ullrich Hafner
 */
class ModuleResolverTest {
    @Test
    void shouldAssignModuleName() {
        try (var builder = new IssueBuilder()) {
            var report = new Report();
            var fileName = "/file/with/warnings.txt";
            builder.setFileName(fileName);
            var noModule = builder.build();
            report.add(noModule);

            builder.setModuleName("module2");
            var withModule = builder.build();
            report.add(withModule);

            var detector = mock(ModuleDetectorRunner.class);
            when(detector.guessModuleName(fileName)).thenReturn("module1");

            var resolver = new ModuleResolver(detector);
            resolver.run(report);

            assertThat(report.get(0)).hasModuleName("module1");
            assertThat(report.get(1)).hasModuleName("module2");

            assertThat(report.getInfoMessages()).contains("-> resolved module names for 1 issues");
        }
    }
}
