package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for hadolint json output.
 * <p>
 * See <a href='https://github.com/hadolint/hadolint'>hadolint</a> for project details. Possible usage via docker is:
 * {@code docker run --rm -i hadolint/hadolint hadolint -f json - < Dockerfile | jq}.
 *
 * @author Andreas Mandel
 */
public class HadoLintParser extends JsonIssueParser {
    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject) {
                report.add(convertToIssue((JSONObject) issue, issueBuilder));
            }
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        if (jsonIssue.has("code")) {
            builder.setCategory(jsonIssue.getString("code"));
        }
        if (jsonIssue.has("level")) {
            builder.setSeverity(toSeverity(jsonIssue.getString("level")));
        }
        if (jsonIssue.has("line")) {
            builder.setLineStart(jsonIssue.getInt("line"));
        }
        if (jsonIssue.has("column")) {
            builder.setColumnStart(jsonIssue.getInt("column"));
        }
        if (jsonIssue.has("message")) {
            builder.setMessage(jsonIssue.getString("message"));
        }
        if (jsonIssue.has("file")) {
            builder.setFileName(jsonIssue.getString("file"));
        }
        return builder.build();
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
