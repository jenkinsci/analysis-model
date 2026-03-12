package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serial;
import java.util.Locale;

/**
 * A parser for ShellCheck JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/koalaman/shellcheck">ShellCheck on GitHub</a>
 */
public class ShellCheckParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                report.add(convertToIssue(object, issueBuilder));
            }
        }
    }

    Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        String file = jsonIssue.optString("file", "");
        int line = jsonIssue.optInt("line", 0);
        int column = jsonIssue.optInt("column", 0);
        int endLine = jsonIssue.optInt("endLine", line);
        int endColumn = jsonIssue.optInt("endColumn", column);
        String level = jsonIssue.optString("level", "style");
        int code = jsonIssue.optInt("code", 0);
        String message = jsonIssue.optString("message", "");

        issueBuilder.setFileName(file)
                .setLineStart(line)
                .setColumnStart(column)
                .setLineEnd(endLine)
                .setColumnEnd(endColumn)
                .setCategory(String.valueOf(code))
                .setType("SC" + code)
                .setMessage(message)
                .setSeverity(mapSeverity(level));

        addFixableMessage(jsonIssue, issueBuilder, message);

        return issueBuilder.buildAndClean();
    }

    private void addFixableMessage(final JSONObject jsonIssue, final IssueBuilder issueBuilder, final String message) {
        if (!jsonIssue.has("fix")) {
            return;
        }
        
        JSONObject fix = jsonIssue.getJSONObject("fix");
        if (!fix.has("replacements") || fix.isNull("replacements")) {
            return;
        }
        
        JSONArray replacements = fix.getJSONArray("replacements");
        if (replacements.length() > 0) {
            issueBuilder.setMessage(message + " [fixable]");
        }
    }

    @SuppressFBWarnings(value = "IMPROPER_UNICODE", justification = "Locale.ENGLISH is explicitly used for case conversion")
    private Severity mapSeverity(final String level) {
        return switch (level.toLowerCase(Locale.ENGLISH)) {
            case "error" -> Severity.ERROR;
            case "warning" -> Severity.WARNING_HIGH;
            case "info" -> Severity.WARNING_NORMAL;
            case "style" -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}
