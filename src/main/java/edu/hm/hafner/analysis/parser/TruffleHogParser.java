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
 * Parser for TruffleHog secret detection JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/trufflesecurity/trufflehog">TruffleHog on GitHub</a>
 * @see <a href="https://trufflesecurity.com/trufflehog-enterprise">TruffleHog Enterprise</a>
 */
public class TruffleHogParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 4815162342162342819L;

    private static final String RESULTS = "results";
    private static final String FINDINGS = "findings";
    private static final String FOUND_BY = "found_by";
    private static final String TYPE = "type";
    private static final String FILE_PATH = "file_path";
    private static final String LINE_NUMBER = "line_number";
    private static final String IS_VERIFIED = "is_verified";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(RESULTS)) {
            var results = jsonReport.getJSONArray(RESULTS);
            parseResultsArray(report, results, issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseResultsArray(report, jsonReport, issueBuilder);
    }

    private void parseResultsArray(final Report report, final JSONArray array, final IssueBuilder issueBuilder) {
        for (int i = 0; i < array.length(); i++) {
            var result = array.optJSONObject(i);
            if (result != null && result.has(FINDINGS)) {
                var findings = result.getJSONArray(FINDINGS);
                for (int j = 0; j < findings.length(); j++) {
                    var finding = findings.getJSONObject(j);
                    report.add(convertToIssue(finding, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject finding, final IssueBuilder issueBuilder) {
        var type = firstNonBlank(finding, TYPE, FOUND_BY);
        var fileName = firstNonBlank(finding, FILE_PATH);
        var lineNumber = finding.optInt(LINE_NUMBER, 0);
        var message = buildMessage(finding);
        var isVerified = finding.optBoolean(IS_VERIFIED, false);
        var severity = isVerified ? Severity.ERROR : Severity.WARNING_NORMAL;

        return issueBuilder
                .setType(StringUtils.defaultIfBlank(type, "-"))
                .setMessage(message)
                .setFileName(fileName)
                .setLineStart(lineNumber)
                .setSeverity(severity)
                .buildAndClean();
    }

    private String buildMessage(final JSONObject finding) {
        var type = finding.optString(TYPE, "");
        var foundBy = finding.optString(FOUND_BY, "");

        if (!type.isEmpty() && !foundBy.isEmpty()) {
            return type + " (detected by: " + foundBy + ")";
        } 
        else if (!type.isEmpty()) {
            return type;
        } 
        else if (!foundBy.isEmpty()) {
            return "Secret detected by: " + foundBy;
        }
        return "Secret detected";
    }
}
