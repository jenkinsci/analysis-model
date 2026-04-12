package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for Psalm JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://psalm.dev/">Psalm</a>
 */
public class PsalmParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 8130060123329964716L;

    private static final String ISSUES = "issues";
    private static final String FILES = "files";
    private static final String FILE_NAME = "file_name";
    private static final String TYPE = "type";
    private static final String MESSAGE = "message";
    private static final String SEVERITY = "severity";
    private static final String LINE_FROM = "line_from";
    private static final String LINE_TO = "line_to";
    private static final String COLUMN_FROM = "column_from";
    private static final String COLUMN_TO = "column_to";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        parseIssues(report, jsonReport.optJSONArray(ISSUES), issueBuilder, "-");

        var files = jsonReport.optJSONObject(FILES);
        if (files != null) {
            for (String fileName : files.keySet()) {
                var file = files.optJSONObject(fileName);
                if (file != null) {
                    parseIssues(report, file.optJSONArray(ISSUES), issueBuilder, fileName);
                }
            }
        }
    }

    private void parseIssues(final Report report, final JSONArray issues,
            final IssueBuilder issueBuilder, final String fallbackFileName) {
        if (issues == null) {
            return;
        }

        for (int i = 0; i < issues.length(); i++) {
            var issue = issues.optJSONObject(i);
            if (issue != null) {
                report.add(convertToIssue(issue, issueBuilder, fallbackFileName));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue,
            final IssueBuilder issueBuilder, final String fallbackFileName) {
        var lineStart = jsonIssue.optInt(LINE_FROM, 0);
        var lineEnd = jsonIssue.optInt(LINE_TO, lineStart);

        return issueBuilder
                .setType(jsonIssue.optString(TYPE, "-"))
                .setMessage(jsonIssue.optString(MESSAGE, "-"))
                .guessSeverity(jsonIssue.optString(SEVERITY, "warning"))
                .setFileName(resolveFileName(jsonIssue, fallbackFileName))
                .setLineStart(lineStart)
                .setLineEnd(lineEnd)
                .setColumnStart(jsonIssue.optInt(COLUMN_FROM, 0))
                .setColumnEnd(jsonIssue.optInt(COLUMN_TO, 0))
                .buildAndClean();
    }

    private String resolveFileName(final JSONObject issue, final String fallbackFileName) {
        var primary = issue.optString(FILE_NAME);
        return StringUtils.defaultIfBlank(primary, StringUtils.defaultIfBlank(fallbackFileName, "-"));
    }
}