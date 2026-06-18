package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for Gradle Lint (nebula.lint) JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/nebula-plugins/gradle-lint-plugin">Gradle Lint Plugin on GitHub</a>
 */
public class GradleLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 3182905019499892061L;

    private static final String VIOLATIONS = "violations";
    private static final String BUILD_FILE = "buildFile";
    private static final String RULE_NAME = "ruleName";
    private static final String PRIORITY = "priority";
    private static final String LINE_NUMBER = "lineNumber";
    private static final String MESSAGE = "message";
    private static final String SOURCE_LINE = "sourceLine";

    private static final int CRITICAL_PRIORITY = 1;
    private static final int WARNING_PRIORITY = 2;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport,
            final IssueBuilder issueBuilder) {
        var violations = jsonReport.optJSONArray(VIOLATIONS);
        if (violations == null) {
            return;
        }

        for (int i = 0; i < violations.length(); i++) {
            var violation = violations.optJSONObject(i);
            if (violation != null) {
                report.add(convertToIssue(violation, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject violation, final IssueBuilder issueBuilder) {
        var fileName = violation.optString(BUILD_FILE, "-");
        var ruleName = violation.optString(RULE_NAME, "-");
        var priority = violation.optInt(PRIORITY, WARNING_PRIORITY);
        var lineNumber = violation.optInt(LINE_NUMBER, 0);
        var message = violation.optString(MESSAGE, "");
        var sourceLine = violation.optString(SOURCE_LINE, "");

        issueBuilder
                .setFileName(fileName.isBlank() ? "-" : fileName)
                .setType(ruleName.isBlank() ? "-" : ruleName)
                .setSeverity(mapPriorityToSeverity(priority))
                .setLineStart(lineNumber)
                .setMessage(message);

        if (!sourceLine.isBlank()) {
            issueBuilder.setDescription("<pre><code>" + sourceLine + "</code></pre>");
        }

        return issueBuilder.buildAndClean();
    }

    /**
     * Maps a Gradle Lint numeric priority to a {@link Severity}.
     *
     * @param priority
     *         the numeric priority from the JSON report
     *
     * @return the corresponding {@link Severity}
     */
    private Severity mapPriorityToSeverity(final int priority) {
        if (priority <= CRITICAL_PRIORITY) {
            return Severity.ERROR;
        }
        if (priority == WARNING_PRIORITY) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }
}