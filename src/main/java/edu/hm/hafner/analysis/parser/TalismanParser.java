package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Talisman security scanner JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/thoughtworks/talisman">Talisman on GitHub</a>
 */
public class TalismanParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 8469918575898239045L;

    private static final String RESULTS_KEY = "results";
    private static final String FILENAME_KEY = "filename";
    private static final String FAILURE_LIST_KEY = "failure_list";
    private static final String WARNING_LIST_KEY = "warning_list";
    private static final String TYPE_KEY = "type";
    private static final String MESSAGE_KEY = "message";
    private static final String SEVERITY_KEY = "severity";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(RESULTS_KEY)) {
            var results = jsonReport.getJSONArray(RESULTS_KEY);
            for (int i = 0; i < results.length(); i++) {
                var fileResult = results.getJSONObject(i);
                parseFileResult(report, fileResult, issueBuilder);
            }
        }
    }

    private void parseFileResult(final Report report, final JSONObject fileResult, final IssueBuilder issueBuilder) {
        var filename = fileResult.optString(FILENAME_KEY, "-");

        parseDetailList(report, fileResult.optJSONArray(FAILURE_LIST_KEY), filename, issueBuilder, false);
        parseDetailList(report, fileResult.optJSONArray(WARNING_LIST_KEY), filename, issueBuilder, true);
    }

    private void parseDetailList(final Report report, @CheckForNull final JSONArray detailList,
            final String filename, final IssueBuilder issueBuilder, final boolean isWarning) {
        if (detailList == null) {
            return;
        }

        for (int i = 0; i < detailList.length(); i++) {
            var detail = detailList.getJSONObject(i);
            report.add(issueBuilder
                    .setFileName(filename)
                    .setType(detail.optString(TYPE_KEY, "-"))
                    .setMessage(detail.optString(MESSAGE_KEY, ""))
                    .setSeverity(mapSeverity(detail.optString(SEVERITY_KEY, ""), isWarning))
                    .buildAndClean());
        }
    }

    private Severity mapSeverity(final String severity, final boolean isWarning) {
        if (isWarning) {
            return Severity.WARNING_LOW;
        }
        return switch (severity.toLowerCase()) {
            case "high" -> Severity.ERROR;
            case "medium" -> Severity.WARNING_HIGH;
            case "low" -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}
