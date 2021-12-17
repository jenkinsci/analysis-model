package edu.hm.hafner.analysis.parser.violations;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link DartAnalyzeParserAdapter}.
 *
 * @author Ullrich Hafner
 */
class DartAnalyzeParserAdapterTest extends AbstractParserTest {
    DartAnalyzeParserAdapterTest() {
        super("dart.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(6);
        softly.assertThat(report.get(0))
                .hasMessage("The import of 'package:flutter/cupertino.dart' is unnecessary because all of the used elements are also provided by the import of 'package:flutter/material.dart'.")
                .hasFileName("C:/Users/david/StudioProjects/bat/lib/step_test.dart")
                .hasType("UNNECESSARY_IMPORT")
                .hasCategory("HINT")
                .hasLineStart(1)
                .hasColumnStart(8)
                .hasColumnEnd(32)
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new DartAnalyzeParserAdapter();
    }
}
