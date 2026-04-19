package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for Phan JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/phan/phan">Phan on GitHub</a>
 */
public class PhanParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1719197864112329217L;

    private static final String ISSUES = "issues";
    private static final String TYPE = "type";
    private static final String CHECK_NAME = "check_name";
    private static final String DESCRIPTION = "description";
    private static final String SEVERITY = "severity";
    private static final String LOCATION = "location";
    private static final String PATH = "path";
    private static final String LINES = "lines";
    private static final String BEGIN = "begin";
    private static final String END = "end";
    private static final String BEGIN_COLUMN = "begin_column";
    private static final String COLUMN = "column";
    private static final int SPLIT_LIMIT = 3;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var issues = jsonReport.optJSONArray(ISSUES);
        if (issues != null) {
            parseIssues(report, issues, issueBuilder);
            return;
        }

        if (isIssue(jsonReport)) {
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
            if (issue != null && isIssue(issue)) {
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private boolean isIssue(final JSONObject jsonIssue) {
        return jsonIssue.has(CHECK_NAME) || jsonIssue.has(TYPE) || jsonIssue.has(DESCRIPTION) || jsonIssue.has(LOCATION);
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        var description = jsonIssue.optString(DESCRIPTION, "");
        var checkName = jsonIssue.optString(CHECK_NAME, jsonIssue.optString(TYPE, "-"));

        issueBuilder
                .setType(checkName)
                .setMessage(extractMessage(description, checkName))
                .setDescription(description)
                .setSeverity(parseSeverity(jsonIssue.opt(SEVERITY)));

        applyLocation(jsonIssue.optJSONObject(LOCATION), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private Severity parseSeverity(final Object severityValue) {
        if (severityValue instanceof Number number) {
            return switch (number.intValue()) {
                case 10 -> Severity.ERROR;
                case 5 -> Severity.WARNING_NORMAL;
                case 0 -> Severity.WARNING_LOW;
                default -> number.intValue() > 5 ? Severity.ERROR : Severity.WARNING_LOW;
            };
        }
        if (severityValue instanceof String string) {
            try {
                return parseSeverity(Integer.valueOf(string));
            }
            catch (NumberFormatException exception) {
                return Severity.guessFromString(string);
            }
        }

        return Severity.WARNING_LOW;
    }

    private void applyLocation(final JSONObject location, final IssueBuilder issueBuilder) {
        if (location == null) {
            return;
        }

        issueBuilder.setFileName(location.optString(PATH, ""));

        var lines = location.optJSONObject(LINES);
        if (lines == null) {
            return;
        }

        var lineStart = lines.optInt(BEGIN, 0);
        if (lines.has(BEGIN)) {
            issueBuilder.setLineStart(lineStart).setLineEnd(lines.optInt(END, lineStart));
        }

        var columnStart = lines.has(BEGIN_COLUMN) ? lines.optInt(BEGIN_COLUMN, 0) : lines.optInt(COLUMN, 0);
        if (lines.has(BEGIN_COLUMN) || lines.has(COLUMN)) {
            issueBuilder.setColumnStart(columnStart).setColumnEnd(columnStart);
        }
    }

    private String extractMessage(final String description, final String checkName) {
        if (description.isBlank()) {
            return description;
        }

        var checkNameIndex = description.indexOf(checkName);
        if (checkNameIndex >= 0) {
            var messageStart = checkNameIndex + checkName.length();
            if (messageStart < description.length()) {
                return description.substring(messageStart).stripLeading();
            }
        }

        var parts = description.split(" ", SPLIT_LIMIT);
        if (parts.length == SPLIT_LIMIT) {
            return parts[2];
        }

        return description;
    }
}