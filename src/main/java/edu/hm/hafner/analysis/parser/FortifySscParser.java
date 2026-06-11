package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;

/**
 * A parser for Fortify Software Security Center (SSC) JSON reports.
 *
 * @author Akash Manna
 */
public class FortifySscParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DATA_KEY = "data";
    private static final String ISSUE_NAME_KEY = "issueName";
    private static final String PRIMARY_LOCATION_KEY = "primaryLocation";
    private static final String FULL_FILE_NAME_KEY = "fullFileName";
    private static final String LINE_KEY = "line";
    private static final String FRIORITY_KEY = "friority";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(DATA_KEY)) {
            JSONArray data = jsonReport.getJSONArray(DATA_KEY);
            for (int i = 0; i < data.length(); i++) {
                JSONObject issue = data.getJSONObject(i);
                parseIssue(report, issue, issueBuilder);
            }
        }
    }

    private void parseIssue(final Report report, final JSONObject issue, final IssueBuilder issueBuilder) {
        String issueName = issue.optString(ISSUE_NAME_KEY, "-");
        String friority = issue.optString(FRIORITY_KEY, "");

        String fileName = "-";
        int line = 0;

        if (issue.has(PRIMARY_LOCATION_KEY)) {
            JSONObject location = issue.getJSONObject(PRIMARY_LOCATION_KEY);
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
