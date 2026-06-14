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

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
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
        var priority = violation.optInt(PRIORITY, 2);
        var lineNumber = violation.optInt(LINE_NUMBER, 0);
        var message = violation.optString(MESSAGE, "");
        var sourceLine = violation.optString(SOURCE_LINE, "");

        issueBuilder
                .setFileName(fileName.isBlank() ? "-" : fileName)
                .setType(ruleName.isBlank() ? "-" : ruleName)
                .guessSeverity(priorityToSeverityName(priority))
                .setLineStart(lineNumber)
                .setMessage(message);

        if (!sourceLine.isBlank()) {
            issueBuilder.setDescription("<pre><code>" + sourceLine + "</code></pre>");
        }

        return issueBuilder.buildAndClean();
    }

    /**
     * Converts a Gradle Lint numeric priority to a severity name string that {@link IssueBuilder#guessSeverity}
     * can interpret.
     *
     * @param priority
     *         the numeric priority from the JSON report
     *
     * @return a severity name string understood by {@link Severity#guessFromString}
     */
    private String priorityToSeverityName(final int priority) {
        if (priority <= 1) {
            return "critical";
        }
        if (priority == 2) {
            return "warning";
        }
        return "low";
    }
}