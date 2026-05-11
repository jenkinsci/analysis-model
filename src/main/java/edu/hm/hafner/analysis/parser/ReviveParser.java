package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for Revive JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/mgechev/revive">Revive on GitHub</a>
 */
public class ReviveParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        for (String fileName : jsonReport.keySet()) {
            var issues = jsonReport.optJSONArray(fileName);
            if (issues != null) {
                for (int i = 0; i < issues.length(); i++) {
                    var jsonIssue = issues.optJSONObject(i);
                    if (jsonIssue != null) {
                        report.add(convertToIssue(fileName, jsonIssue, issueBuilder));
                    }
                }
            }
        }
    }

    private Issue convertToIssue(final String fileName, final JSONObject jsonIssue,
            final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(fileName)
                .setLineStart(jsonIssue.optInt("line", 0))
                .setColumnStart(jsonIssue.optInt("column", 0))
                .setType(jsonIssue.optString("rule", "-"))
                .setMessage(jsonIssue.optString("message", ""))
                .guessSeverity(jsonIssue.optString("severity", "warning"));

        return issueBuilder.buildAndClean();
    }
}
