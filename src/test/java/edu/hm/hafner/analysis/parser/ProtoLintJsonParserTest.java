package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Test class for {@link ProtoLintJsonParser}.
 *
 * @author github@profhenry.de
 */
class ProtoLintJsonParserTest extends AbstractParserTest {
    ProtoLintJsonParserTest() {
        super("protolint_0.50.2.json");
    }

    @Override
    public void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(462);

        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(3)
                .hasColumnStart(5)
                .hasMessage("Found an incorrect indentation style \"    \". \"  \" is correct.")
                .hasFileName("/home/jwiesner/Development/github/profhenry/protolint/_example/proto/issue_111/multipleFixersApplicable.proto")
                .hasType("INDENT");

        softly.assertThat(report.get(12))
                .hasSeverity(Severity.ERROR)
                .hasLineStart(3)
                .hasColumnStart(5)
                .hasMessage("EnumField name \"UNKNOWN\" should have the prefix \"ENUM_ALLOWING_ALIAS\"")
                .hasFileName("/home/jwiesner/Development/github/profhenry/protolint/_example/proto/issue_111/multipleFixersApplicable.proto")
                .hasType("ENUM_FIELD_NAMES_PREFIX");

        softly.assertThat(report.get(224))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(207)
                .hasColumnStart(3)
                .hasMessage("Field \"amount\" should have a comment")
                .hasFileName("/home/jwiesner/Development/github/profhenry/protolint/_example/proto/issue_128/grpc-gateway_a_bit_of_everything.proto")
                .hasType("FIELDS_HAVE_COMMENT");
    }

    @Override
    public IssueParser createParser() {
        return new ProtoLintJsonParser();
    }

    @Test
    @DisplayName("Parsing json report generated with protolint 0.49.8")
    void json0498() {
        var report = parse("protolint_0.49.8.json");

        assertThat(report).hasSize(352);

        try (var softly = new SoftAssertions()) {
            softly.assertThat(report.get(2))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(3)
                    .hasColumnStart(5)
                    .hasMessage("Found an incorrect indentation style \"    \". \"  \" is correct.")
                    .hasFileName("_example/proto/issue_111/multipleFixersApplicable.proto")
                    .hasType("INDENT");

            softly.assertThat(report.get(10))
                    .hasSeverity(Severity.WARNING_LOW)
                    .hasLineStart(3)
                    .hasColumnStart(5)
                    .hasMessage("EnumField name \"UNKNOWN\" should have the prefix \"ENUM_ALLOWING_ALIAS\"")
                    .hasFileName("_example/proto/issue_111/multipleFixersApplicable.proto")
                    .hasType("ENUM_FIELD_NAMES_PREFIX");
        }
    }
}
