package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;
import java.util.Map;

/**
 * A parser for ShellCheck JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/koalaman/shellcheck">ShellCheck on GitHub</a>
 */
public class ShellCheckParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Map<String, Severity> SEVERITY_MAP = Map.of(
            "error", Severity.ERROR,
            "warning", Severity.WARNING_HIGH,
            "info", Severity.WARNING_NORMAL,
            "style", Severity.WARNING_LOW);

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
                .setSeverity(SEVERITY_MAP.getOrDefault(level, Severity.WARNING_NORMAL));

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
}
