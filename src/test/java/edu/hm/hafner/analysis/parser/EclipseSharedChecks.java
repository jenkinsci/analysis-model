package edu.hm.hafner.analysis.parser;

import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.assertSoftly;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * Common assertions for differently formatted Eclipse output, from the same source code.
 * 
 * @author Jason Faust
 */
final class EclipseSharedChecks {

    private EclipseSharedChecks() {
    }

    /**
     * Tests that warnings are categorized as {@code Code} or {@code JavaDoc}.
     */
    static void javadocCategory(Report warnings) {
        assertThat(warnings).hasSize(5);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(1)
                    .hasMessage("A default nullness annotation has not been specified for the package a")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory("Code");

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(3)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory("Javadoc");

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(5)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory("Javadoc");

            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(7)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory("Javadoc");

            softly.assertThat(warnings.get(4))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(8)
                    .hasMessage("The value of the local variable unused is not used")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory("Code");
        });
    }

}
