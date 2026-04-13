package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for CFN-Nag JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/stelligent/cfn_nag">cfn_nag on GitHub</a>
 */
public class CfnNagParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -3892714194841984933L;

    private static final String FILE_NAME = "filename";
    private static final String FILE_RESULTS = "file_results";
    private static final String VIOLATIONS = "violations";
    private static final String ID = "id";
    private static final String TYPE = "type";
    private static final String MESSAGE = "message";
    private static final String LOGICAL_RESOURCE_IDS = "logical_resource_ids";
    private static final String LINE_NUMBERS = "line_numbers";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        addIssues(jsonReport, issueBuilder, report);
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var issueContainer = jsonReport.optJSONObject(i);
            if (issueContainer != null) {
                addIssues(issueContainer, issueBuilder, report);
            }
        }
    }

    private void addIssues(final JSONObject issueContainer, final IssueBuilder issueBuilder, final Report report) {
        var fileResults = issueContainer.optJSONObject(FILE_RESULTS);
        if (fileResults == null) {
            return;
        }

        var violations = fileResults.optJSONArray(VIOLATIONS);
        if (violations == null) {
            return;
        }

        var fileName = issueContainer.optString(FILE_NAME);
        for (int i = 0; i < violations.length(); i++) {
            var violation = violations.optJSONObject(i);
            if (violation != null) {
                report.add(convertToIssue(violation, fileName, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject violation, final String fileName, final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(fileName)
                .setType(violation.optString(ID, "-"))
                .setMessage(violation.optString(MESSAGE));

        applySeverity(violation.optString(TYPE), issueBuilder);
        applyLineNumbers(violation.optJSONArray(LINE_NUMBERS), issueBuilder);
        applyLogicalResourceIds(violation.optJSONArray(LOGICAL_RESOURCE_IDS), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applySeverity(final String type, final IssueBuilder issueBuilder) {
        if (equalsIgnoreCase(type, "FAIL")) {
            issueBuilder.setSeverity(Severity.ERROR);
        }
        else if (equalsIgnoreCase(type, "WARN")) {
            issueBuilder.setSeverity(Severity.WARNING_NORMAL);
        }
        else {
            issueBuilder.guessSeverity(type);
        }
    }

    private void applyLineNumbers(@CheckForNull final JSONArray lineNumbers, final IssueBuilder issueBuilder) {
        if (lineNumbers == null || lineNumbers.isEmpty()) {
            return;
        }

        var lineStart = lineNumbers.optInt(0, 0);
        var lineEnd = lineNumbers.optInt(lineNumbers.length() - 1, lineStart);

        issueBuilder.setLineStart(lineStart).setLineEnd(lineEnd);
    }

    private void applyLogicalResourceIds(@CheckForNull final JSONArray logicalResourceIds,
            final IssueBuilder issueBuilder) {
        if (logicalResourceIds == null || logicalResourceIds.isEmpty()) {
            return;
        }

        var resources = new StringBuilder();
        for (int i = 0; i < logicalResourceIds.length(); i++) {
            var id = logicalResourceIds.optString(i);
            if (id.isBlank()) {
                continue;
            }

            if (!resources.isEmpty()) {
                resources.append(", ");
            }
            resources.append(id);
        }

        if (!resources.isEmpty()) {
            issueBuilder.setDescription("Logical resource ID(s): " + resources);
        }
    }
}