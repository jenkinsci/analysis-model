package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for markdownlint JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/DavidAnson/markdownlint">markdownlint on GitHub</a>
 */
public class MarkdownLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 2335221513744535623L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                issueBuilder.setSeverity(Severity.WARNING_NORMAL);
                report.add(convertToIssue(object, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        issueBuilder.setType(getRuleId(jsonIssue));
        issueBuilder.setMessage(getMessage(jsonIssue));

        if (jsonIssue.has("fileName")) {
            issueBuilder.setFileName(jsonIssue.getString("fileName"));
        }

        int line = jsonIssue.optInt("lineNumber", 0);
        if (line > 0) {
            issueBuilder.setLineStart(line);
        }

        var range = jsonIssue.optJSONArray("errorRange");
        if (range != null && !range.isEmpty()) {
            int column = range.optInt(0, 1);
            issueBuilder.setColumnStart(column);

            int length = range.optInt(1, 1);
            issueBuilder.setColumnEnd(Math.max(column, column + length - 1));
        }

        return issueBuilder.buildAndClean();
    }

    private String getRuleId(final JSONObject jsonIssue) {
        var ruleNames = jsonIssue.optJSONArray("ruleNames");
        if (ruleNames != null && !ruleNames.isEmpty()) {
            return ruleNames.optString(0, "-");
        }
        return "-";
    }

    private String getMessage(final JSONObject jsonIssue) {
        String description = jsonIssue.optString("ruleDescription", "");
        String detail = jsonIssue.optString("errorDetail", "");

        if (!detail.isBlank()) {
            return description + ": " + detail;
        }
        return description;
    }
}