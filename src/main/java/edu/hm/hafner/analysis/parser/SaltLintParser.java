package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for Salt Lint JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/warpnet/salt-lint">salt-lint on GitHub</a>
 */
public class SaltLintParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -9216241878140028645L;

    private static final String FILE_TAG = "file";
    private static final String LINE_TAG = "line";
    private static final String COLUMN_TAG = "column";
    private static final String MESSAGE_TAG = "message";
    private static final String SEVERITY_TAG = "severity";
    private static final String RULE_TAG = "rule";
    private static final String RULE_ID_TAG = "id";

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object item : jsonReport) {
            if (item instanceof JSONObject issue) {
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        var rule = jsonIssue.optJSONObject(RULE_TAG);

        return issueBuilder
                .setFileName(jsonIssue.optString(FILE_TAG, "-"))
                .setLineStart(jsonIssue.optInt(LINE_TAG, 0))
                .setColumnStart(jsonIssue.optInt(COLUMN_TAG, 0))
                .setMessage(jsonIssue.optString(MESSAGE_TAG, ""))
                .guessSeverity(jsonIssue.optString(SEVERITY_TAG, ""))
                .setType(extractRuleId(jsonIssue, rule))
                .buildAndClean();
    }

    private String extractRuleId(final JSONObject jsonIssue, final JSONObject rule) {
        if (rule != null) {
            var id = rule.optString(RULE_ID_TAG, "");
            if (!id.isBlank()) {
                return id;
            }
        }
        // Fallback: try the top-level "id" field
        var topLevelId = jsonIssue.optString(RULE_ID_TAG, "");
        return topLevelId.isBlank() ? "-" : topLevelId;
    }
}
