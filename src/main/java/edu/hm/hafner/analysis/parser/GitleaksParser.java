package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * Parser for Gitleaks JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/zricethezav/gitleaks">gitleaks on GitHub</a>
 */
public class GitleaksParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -1234567890123456789L;

    private static final String LEAKS = "leaks";
    private static final String RULE = "rule";
    private static final String RULE_ID = "rule_id";
    private static final String DESCRIPTION = "description";
    private static final String FILE = "file";
    private static final String START_LINE = "start_line";
    private static final String END_LINE = "end_line";
    private static final String LINE = "line";
    private static final String SEVERITY = "severity";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(LEAKS)) {
            var leaks = jsonReport.getJSONArray(LEAKS);
            parseLeaksArray(report, leaks, issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseLeaksArray(report, jsonReport, issueBuilder);
    }

    private void parseLeaksArray(final Report report, final JSONArray array, final IssueBuilder issueBuilder) {
        for (int i = 0; i < array.length(); i++) {
            var leak = array.getJSONObject(i);
            report.add(convertToIssue(leak, issueBuilder));
        }
    }

    private Issue convertToIssue(@CheckForNull final JSONObject leak, final IssueBuilder issueBuilder) {
        if (leak == null) {
            return issueBuilder.buildAndClean();
        }

        var ruleId = firstNonBlank(leak, RULE_ID, RULE);
        var description = leak.optString(DESCRIPTION, "");
        var fileName = firstNonBlank(leak, FILE);

        if (ruleId.isEmpty()) {
            issueBuilder.setType("-");
        }
        else {
            issueBuilder.setType(ruleId);
        }

        issueBuilder.setMessage(description.isBlank() ? ruleId : description);

        if (!fileName.isBlank()) {
            issueBuilder.setFileName(fileName);
        }

        if (leak.has(START_LINE) || leak.has(END_LINE)) {
            issueBuilder.setLineStart(leak.optInt(START_LINE)).setLineEnd(leak.optInt(END_LINE));
        }
        else if (leak.has(LINE)) {
            var line = leak.optInt(LINE);
            issueBuilder.setLineStart(line).setLineEnd(line);
        }

        issueBuilder.guessSeverity(leak.optString(SEVERITY, "warning"));

        return issueBuilder.buildAndClean();
    }
}
