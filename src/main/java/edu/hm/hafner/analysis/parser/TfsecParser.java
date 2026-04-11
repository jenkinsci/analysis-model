package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for tfsec JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/aquasecurity/tfsec">tfsec on GitHub</a>
 * @see <a href="https://aquasecurity.github.io/tfsec/">tfsec documentation</a>
 */
public class TfsecParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1234567890123456789L;

    private static final String RESULTS = "results";
    private static final String RULE_ID = "rule_id";
    private static final String DESCRIPTION = "description";
    private static final String SEVERITY = "severity";
    private static final String RANGE = "range";
    private static final String FILENAME = "filename";
    private static final String START = "start";
    private static final String END = "end";
    private static final String LINE = "line";
    private static final String COLUMN = "column";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var results = jsonReport.optJSONArray(RESULTS);

        if (results != null) {
            for (int i = 0; i < results.length(); i++) {
                var result = results.optJSONObject(i);
                if (result != null) {
                    report.add(convertToIssue(result, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject result, final IssueBuilder issueBuilder) {
        issueBuilder
                .setType(result.optString(RULE_ID, "-"))
                .setMessage(result.optString(DESCRIPTION, "-"))
                .guessSeverity(result.optString(SEVERITY, "warning"));

        applyRange(result.optJSONObject(RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyRange(final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        issueBuilder.setFileName(range.optString(FILENAME));
        applyStart(range.optJSONObject(START), issueBuilder);
        applyEnd(range.optJSONObject(END), issueBuilder);
    }

    private void applyStart(final JSONObject start, final IssueBuilder issueBuilder) {
        if (start == null) {
            return;
        }

        issueBuilder.setLineStart(start.optInt(LINE)).setColumnStart(start.optInt(COLUMN));
    }

    private void applyEnd(final JSONObject end, final IssueBuilder issueBuilder) {
        if (end == null) {
            return;
        }

        issueBuilder.setLineEnd(end.optInt(LINE)).setColumnEnd(end.optInt(COLUMN));
    }
}
