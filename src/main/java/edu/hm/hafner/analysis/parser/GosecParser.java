package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for gosec JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/securego/gosec">gosec on GitHub</a>
 */
public class GosecParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -7819686487626201186L;

    private static final String ISSUES = "Issues";
    private static final String FILE = "file";
    private static final String LINE = "line";
    private static final String COLUMN = "column";
    private static final String RULE_ID = "rule_id";
    private static final String DETAILS = "details";
    private static final String CODE = "code";
    private static final String SEVERITY = "severity";
    private static final String CONFIDENCE = "confidence";
    private static final String CWE = "cwe";
    private static final String CWE_ID = "id";
    private static final String CWE_URL = "url";

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
        return issue.has(RULE_ID) || issue.has(DETAILS) || issue.has(FILE);
    }

    private Issue convertToIssue(final JSONObject issue, final IssueBuilder issueBuilder) {
        var severity = getSeverity(issue);
        var message = issue.optString(DETAILS, "Security issue detected");

        issueBuilder
                .setFileName(issue.optString(FILE))
                .setType(issue.optString(RULE_ID, "-"))
                .setMessage(message)
                .setSeverity(severity)
                .setDescription(createDescription(issue));

        int line = issue.optInt(LINE, 0);
        if (line > 0) {
            issueBuilder.setLineStart(line).setLineEnd(line);
            int column = issue.optInt(COLUMN, 0);
            if (column > 0) {
                issueBuilder.setColumnStart(column).setColumnEnd(column);
            }
        }

        return issueBuilder.buildAndClean();
    }

    private Severity getSeverity(final JSONObject issue) {
        var severity = issue.optString(SEVERITY, "");
        if (StringUtils.isBlank(severity)) {
            severity = issue.optString(CONFIDENCE, "");
        }

        if (StringUtils.isBlank(severity)) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.guessFromString(severity);
    }

    private String createDescription(final JSONObject issue) {
        var description = new StringBuilder();

        var cwe = issue.optJSONObject(CWE);
        if (cwe != null) {
            appendCweDescription(description, cwe);
        }

        var code = issue.optString(CODE, "");
        if (StringUtils.isNotBlank(code)) {
            if (!description.isEmpty()) {
                description.append("<br/>");
            }
            description.append(code.trim());
        }

        return description.toString();
    }

    private void appendCweDescription(final StringBuilder description, final JSONObject cwe) {
        var id = cwe.optString(CWE_ID, "").trim();
        var url = cwe.optString(CWE_URL, "").trim();

        if (id.isBlank() && url.isBlank()) {
            return;
        }

        if (!id.isBlank()) {
            description.append("CWE-").append(id);
        }
        if (!url.isBlank()) {
            if (!id.isBlank()) {
                description.append(": ");
            }
            description.append(url);
        }
    }
}