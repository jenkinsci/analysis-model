package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Checkov JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://www.checkov.io/">Checkov</a>
 */
public class CheckovParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1066649332927543406L;

    private static final String RESULTS = "results";
    private static final String FAILED_CHECKS = "failed_checks";
    private static final String CHECK_ID = "check_id";
    private static final String CHECK_NAME = "check_name";
    private static final String SEVERITY = "severity";
    private static final String FILE_PATH = "file_path";
    private static final String FILE_LINE_RANGE = "file_line_range";
    private static final String RESOURCE = "resource";
    private static final String GUIDELINE = "guideline";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var failedChecks = getFailedChecks(jsonReport);

        for (int i = 0; i < failedChecks.length(); i++) {
            var failedCheck = failedChecks.optJSONObject(i);
            if (failedCheck != null) {
                report.add(convertToIssue(failedCheck, issueBuilder));
            }
        }
    }

    private JSONArray getFailedChecks(final JSONObject jsonReport) {
        var topLevelFailedChecks = jsonReport.optJSONArray(FAILED_CHECKS);
        if (topLevelFailedChecks != null) {
            return topLevelFailedChecks;
        }

        var results = jsonReport.optJSONObject(RESULTS);
        if (results != null) {
            var nestedFailedChecks = results.optJSONArray(FAILED_CHECKS);
            if (nestedFailedChecks != null) {
                return nestedFailedChecks;
            }
        }

        return new JSONArray();
    }

    private Issue convertToIssue(final JSONObject failedCheck, final IssueBuilder issueBuilder) {
        issueBuilder
                .setType(failedCheck.optString(CHECK_ID, "-"))
                .setMessage(failedCheck.optString(CHECK_NAME, "-"))
                .guessSeverity(failedCheck.optString(SEVERITY, "warning"))
                .setFileName(failedCheck.optString(FILE_PATH));

        applyResource(failedCheck.optString(RESOURCE), issueBuilder);
        applyGuideline(failedCheck.optString(GUIDELINE), issueBuilder);
        applyLineRange(failedCheck.optJSONArray(FILE_LINE_RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyResource(@CheckForNull final String resource, final IssueBuilder issueBuilder) {
        if (StringUtils.isNotBlank(resource)) {
            issueBuilder.setCategory(resource);
        }
    }

    private void applyGuideline(@CheckForNull final String guideline, final IssueBuilder issueBuilder) {
        if (StringUtils.isNotBlank(guideline)) {
            issueBuilder.setDescription(guideline);
        }
    }

    private void applyLineRange(@CheckForNull final JSONArray fileLineRange, final IssueBuilder issueBuilder) {
        if (fileLineRange == null || fileLineRange.isEmpty()) {
            return;
        }

        var lineStart = fileLineRange.optInt(0, 0);
        var lineEnd = fileLineRange.optInt(1, lineStart);

        issueBuilder.setLineStart(lineStart).setLineEnd(lineEnd);
    }
}