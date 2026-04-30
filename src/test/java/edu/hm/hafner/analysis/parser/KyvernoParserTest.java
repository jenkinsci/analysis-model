package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link KyvernoParser}.
 *
 * @author Akash Manna
 */
class KyvernoParserTest extends AbstractParserTest {
    KyvernoParserTest() {
        super("kyverno-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        softly.assertThat(report.get(0))
                .hasFileName("default/test-pod")
                .hasType("require-labels")
                .hasCategory("validation")
                .hasMessage("Pod must have 'app' and 'version' labels")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("production/nginx-pod")
                .hasType("require-resource-limits")
                .hasCategory("validation")
                .hasMessage("CPU and memory limits must be specified")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("production/nginx-pod")
                .hasType("disallow-privileged")
                .hasCategory("validation")
                .hasMessage("Privileged containers are not allowed")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(3))
                .hasFileName("staging/backend-service")
                .hasType("require-non-root-user")
                .hasCategory("validation")
                .hasMessage("Container must run as non-root user")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("app-config")
                .hasType("require-resource-naming")
                .hasCategory("validation")
                .hasMessage("Resource name does not follow naming convention")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new KyvernoParser();
    }

    @Test
    void accepts() {
        assertThat(new KyvernoParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("kyverno-report.json")))).isTrue();
        assertThat(new KyvernoParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void errorStatus() throws ParsingException {
        var report = parse("kyverno-error-status.json");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("default/error-pod")
                .hasType("critical-rule")
                .hasCategory("validation")
                .hasMessage("Critical policy violation detected")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void edgeCases() throws ParsingException {
        var report = parse("kyverno-edge-cases.json");

        assertThat(report).hasSize(4);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasType("minimal-rule")
                .hasCategory("validation")
                .hasMessage("Rule with minimal metadata")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasFileName("-")
                .hasType("null-resource-rule")
                .hasCategory("validation")
                .hasMessage("Rule with null resource")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(2))
                .hasFileName("-")
                .hasType("no-resource-rule")
                .hasCategory("validation")
                .hasMessage("Rule without resource")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(3))
                .hasFileName("-")
                .hasType("no-metadata-rule")
                .hasCategory("validation")
                .hasMessage("Rule with resource but no metadata")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void passedPoliciesFiltered() throws ParsingException {
        var report = parse("kyverno-no-failures.json");

        assertThat(report).isEmpty();
    }

    @Test
    void noRulesArray() throws ParsingException {
        var report = parse("kyverno-no-rules.json");

        assertThat(report).isEmpty();
    }
}
