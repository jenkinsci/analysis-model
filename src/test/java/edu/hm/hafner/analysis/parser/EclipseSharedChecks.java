package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Categories;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Common assertions for differently formatted Eclipse output, from the same source code.
 *
 * @author Jason Faust
 */
final class EclipseSharedChecks {
    private EclipseSharedChecks() {
        // prevents instantiation
    }

    /**
     * Verifies that warnings are categorized as {@code Code} or {@code JavaDoc}.
     *
     * @param warnings
     *         the warnings to check
     */
    static void verifyCategory(final Report warnings) {
        assertThat(warnings).hasSize(5);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(warnings.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(1)
                    .hasMessage("A default nullness annotation has not been specified for the package a")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.OTHER);

            softly.assertThat(warnings.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(3)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(2))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(5)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(3))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(7)
                    .hasMessage("Javadoc: Missing comment for public declaration")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.JAVADOC);

            softly.assertThat(warnings.get(4))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(8)
                    .hasMessage("The value of the local variable unused is not used")
                    .hasFileName("C:/devenv/workspace/x/y/src/main/java/a/B.java")
                    .hasCategory(Categories.OTHER);
        }
    }
}
