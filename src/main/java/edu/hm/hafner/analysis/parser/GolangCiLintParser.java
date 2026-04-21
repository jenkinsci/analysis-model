package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for golangci-lint JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://golangci-lint.run/">golangci-lint</a>
 * @see <a href="https://github.com/golangci/golangci-lint">golangci-lint on GitHub</a>
 */
public class GolangCiLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 4862678927537265606L;

    private static final String ISSUES = "Issues";
    private static final String POS = "Pos";
    private static final String LINE_RANGE = "LineRange";
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String FROM_LINTER = "FromLinter";
    private static final String TEXT = "Text";
    private static final String SEVERITY = "Severity";
    private static final String FILENAME = "Filename";
    private static final String LINE = "Line";
    private static final String COLUMN = "Column";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var issues = jsonReport.optJSONArray(ISSUES);
        if (issues != null) {
            parseIssues(report, issues, issueBuilder);
        }
        else if (looksLikeIssue(jsonReport)) {
            report.add(convertToIssue(jsonReport, issueBuilder));
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseIssues(report, jsonReport, issueBuilder);
    }

    private void parseIssues(final Report report, final JSONArray issues, final IssueBuilder issueBuilder) {
        for (int i = 0; i < issues.length(); i++) {
            var issue = issues.optJSONObject(i);
            if (issue != null) {
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private boolean looksLikeIssue(final JSONObject issue) {
        return issue.has(TEXT) || issue.has(FROM_LINTER) || issue.has(POS);
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        issueBuilder
                .setType(jsonIssue.optString(FROM_LINTER, "-"))
                .guessSeverity(getSeverity(jsonIssue))
                .setMessage(jsonIssue.optString(TEXT));

        var position = jsonIssue.optJSONObject(POS);

        applyPosition(position, issueBuilder);
        applyLineRange(jsonIssue.optJSONObject(LINE_RANGE), issueBuilder, position);

        return issueBuilder.buildAndClean();
    }

    private String getSeverity(final JSONObject jsonIssue) {
        var severity = jsonIssue.optString(SEVERITY, "warning");
        return StringUtils.defaultIfBlank(severity, "warning");
    }

    private void applyPosition(@CheckForNull final JSONObject position, final IssueBuilder issueBuilder) {
        if (position == null) {
            return;
        }

        issueBuilder.setFileName(position.optString(FILENAME))
                .setLineStart(position.optInt(LINE))
                .setColumnStart(position.optInt(COLUMN));
    }

    private void applyLineRange(@CheckForNull final JSONObject lineRange, final IssueBuilder issueBuilder,
            @CheckForNull final JSONObject position) {
        if (lineRange == null) {
            return;
        }

        var startLine = position == null ? 0 : position.optInt(LINE);
        if (startLine == 0) {
            startLine = lineRange.optInt(FROM);
            issueBuilder.setLineStart(startLine);
        }

        if (startLine == 0) {
            return;
        }

        issueBuilder.setLineEnd(lineRange.optInt(TO, startLine));
    }
}