package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link ShellCheckParser}.
 *
 * @author Akash Manna
 */
class ShellCheckParserTest extends AbstractParserTest {
    ShellCheckParserTest() {
        super("shellcheck.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(8);
        
        // Test first issue - SC2086: error level (unquoted variable)
        softly.assertThat(report.get(0))
                .hasLineStart(10)
                .hasColumnStart(5)
                .hasLineEnd(10)
                .hasColumnEnd(10)
                .hasType("SC2086")
                .hasCategory("2086")
                .hasMessage("Double quote to prevent globbing and word splitting. [fixable]")
                .hasFileName("script.sh")
                .hasSeverity(Severity.ERROR);
        
        // Test second issue - SC2046: warning level
        softly.assertThat(report.get(1))
                .hasLineStart(15)
                .hasColumnStart(10)
                .hasLineEnd(15)
                .hasColumnEnd(25)
                .hasType("SC2046")
                .hasCategory("2046")
                .hasMessage("Quote this to prevent word splitting.")
                .hasFileName("script.sh")
                .hasSeverity(Severity.WARNING_NORMAL);
        
        // Test third issue - SC2155: warning level
        softly.assertThat(report.get(2))
                .hasLineStart(20)
                .hasColumnStart(7)
                .hasLineEnd(20)
                .hasColumnEnd(18)
                .hasType("SC2155")
                .hasCategory("2155")
                .hasMessage("Declare and assign separately to avoid masking return values.")
                .hasFileName("deploy.sh")
                .hasSeverity(Severity.WARNING_NORMAL);
        
        // Test fourth issue - SC2164: warning level (cd without error check)
        softly.assertThat(report.get(3))
                .hasLineStart(8)
                .hasColumnStart(1)
                .hasLineEnd(8)
                .hasColumnEnd(15)
                .hasType("SC2164")
                .hasCategory("2164")
                .hasMessage("Use 'cd ... || exit' or 'cd ... || return' in case cd fails.")
                .hasFileName("deploy.sh")
                .hasSeverity(Severity.WARNING_NORMAL);
        
        // Test fifth issue - SC2034: info level (unused variable)
        softly.assertThat(report.get(4))
                .hasLineStart(12)
                .hasColumnStart(1)
                .hasLineEnd(12)
                .hasColumnEnd(8)
                .hasType("SC2034")
                .hasCategory("2034")
                .hasMessage("unused_var appears unused. Verify use (or export if used externally).")
                .hasFileName("utils.sh")
                .hasSeverity(Severity.WARNING_LOW);
        
        // Test sixth issue - SC2006: style level (deprecated backticks)
        softly.assertThat(report.get(5))
                .hasLineStart(25)
                .hasColumnStart(9)
                .hasLineEnd(25)
                .hasColumnEnd(22)
                .hasType("SC2006")
                .hasCategory("2006")
                .hasMessage("Use $(...) notation instead of legacy backticked `...`. [fixable]")
                .hasFileName("legacy.sh")
                .hasSeverity(Severity.WARNING_LOW);
        
        // Test seventh issue - SC2230: info level
        softly.assertThat(report.get(6))
                .hasLineStart(30)
                .hasColumnStart(5)
                .hasLineEnd(30)
                .hasColumnEnd(14)
                .hasType("SC2230")
                .hasCategory("2230")
                .hasMessage("which is non-standard. Use builtin 'command -v' instead.")
                .hasFileName("check.sh")
                .hasSeverity(Severity.WARNING_LOW);
        
        // Test eighth issue - SC1091: info level (source file not found)
        softly.assertThat(report.get(7))
                .hasLineStart(5)
                .hasColumnStart(1)
                .hasLineEnd(5)
                .hasColumnEnd(22)
                .hasType("SC1091")
                .hasCategory("1091")
                .hasMessage("Not following: /etc/config: openBinaryFile: does not exist (No such file or directory)")
                .hasFileName("init.sh")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldIgnoreFixWithMissingReplacementsKey() {
        var parser = new ShellCheckParser();
        var jsonIssue = new JSONObject()
                .put("file", "test.sh")
                .put("line", 1)
                .put("column", 1)
                .put("level", "warning")
                .put("code", 2086)
                .put("message", "Some warning")
                .put("fix", new JSONObject());

        var issue = parser.convertToIssue(jsonIssue, new IssueBuilder());
        assertThat(issue).hasMessage("Some warning");
    }

    @Test
    void shouldIgnoreFixWithNullReplacements() {
        var parser = new ShellCheckParser();
        var jsonIssue = new JSONObject()
                .put("file", "test.sh")
                .put("line", 1)
                .put("column", 1)
                .put("level", "warning")
                .put("code", 2086)
                .put("message", "Some warning")
                .put("fix", new JSONObject().put("replacements", JSONObject.NULL));

        var issue = parser.convertToIssue(jsonIssue, new IssueBuilder());
        assertThat(issue).hasMessage("Some warning");
    }

    @Test
    void shouldIgnoreFixWithEmptyReplacements() {
        var parser = new ShellCheckParser();
        var jsonIssue = new JSONObject()
                .put("file", "test.sh")
                .put("line", 1)
                .put("column", 1)
                .put("level", "warning")
                .put("code", 2086)
                .put("message", "Some warning")
                .put("fix", new JSONObject().put("replacements", new JSONArray()));

        var issue = parser.convertToIssue(jsonIssue, new IssueBuilder());
        assertThat(issue).hasMessage("Some warning");
    }

    @Override
    protected IssueParser createParser() {
        return new ShellCheckParser();
    }
}
