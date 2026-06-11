package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

/**
 * A parser for Fortify Software Security Center (SSC) JSON reports.
 *
 * @see <a href="https://www.microfocus.com/documentation/fortify-software-security-center/">Fortify Software Security Center Documentation</a>
 * @author Akash Manna
 */
public class FortifySscParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 2676345875913046357L;

    private static final String DATA_KEY = "data";
    private static final String ISSUE_NAME_KEY = "issueName";
    private static final String PRIMARY_LOCATION_KEY = "primaryLocation";
    private static final String FULL_FILE_NAME_KEY = "fullFileName";
    private static final String LINE_KEY = "line";
    private static final String FRIORITY_KEY = "friority";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var data = jsonReport.optJSONArray(DATA_KEY);
        if (data == null) {
            return;
        }

        for (int i = 0; i < data.length(); i++) {
            var issue = data.optJSONObject(i);
            if (issue != null) {
                parseIssue(report, issue, issueBuilder);
            }
        }
    }

    private void parseIssue(final Report report, final JSONObject issue, final IssueBuilder issueBuilder) {
        var issueName = issue.optString(ISSUE_NAME_KEY, "-");
        var friority = issue.optString(FRIORITY_KEY, "");

        var fileName = "-";
        var line = 0;

        var location = issue.optJSONObject(PRIMARY_LOCATION_KEY);
        if (location != null) {
            fileName = location.optString(FULL_FILE_NAME_KEY, "-");
            line = location.optInt(LINE_KEY, 0);
        }

        report.add(issueBuilder
                .setFileName(fileName)
                .setLineStart(line)
                .setType(issueName)
                .setMessage(issueName)
                .guessSeverity(friority)
                .buildAndClean());
    }
}