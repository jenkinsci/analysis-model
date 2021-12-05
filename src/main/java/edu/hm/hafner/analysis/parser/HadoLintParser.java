package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for hadolint json output.
 *
 * <p>
 * Possible usage via docker is:
 * </p>
 * {@code
 * <pre>
 *     docker run --rm -i hadolint/hadolint hadolint -f json - < Dockerfile | jq.
 * </pre>}
 *
 * @author Andreas Mandel
 * @see <a href="https://github.com/hadolint/hadolint">hadolint</a>
 */
public class HadoLintParser extends JsonIssueParser {
    private static final long serialVersionUID = 1618503559862246224L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject) {
                report.add(convertToIssue((JSONObject) issue, issueBuilder));
            }
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue.has("code")) {
            issueBuilder.setCategory(jsonIssue.getString("code"));
        }
        if (jsonIssue.has("level")) {
            issueBuilder.setSeverity(toSeverity(jsonIssue.getString("level")));
        }
        if (jsonIssue.has("line")) {
            issueBuilder.setLineStart(jsonIssue.getInt("line"));
        }
        if (jsonIssue.has("column")) {
            issueBuilder.setColumnStart(jsonIssue.getInt("column"));
        }
        if (jsonIssue.has("message")) {
            issueBuilder.setMessage(jsonIssue.getString("message"));
        }
        if (jsonIssue.has("file")) {
            issueBuilder.setFileName(jsonIssue.getString("file"));
        }
        return issueBuilder.buildAndClean();
    }

    private Severity toSeverity(final String level) {
        switch (level) {
            case "style":
                return Severity.WARNING_LOW;
            case "info":
                return Severity.WARNING_NORMAL;
            case "warning":
                return Severity.WARNING_HIGH;
            case "error":
                return Severity.ERROR;
            default:
                return Severity.WARNING_LOW;
        }
    }
}
