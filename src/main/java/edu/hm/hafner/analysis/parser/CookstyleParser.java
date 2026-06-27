package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for Cookstyle (Chef's RuboCop-based linter) JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/chef/cookstyle">Cookstyle on GitHub</a>
 */
public class CookstyleParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 4227581723969319657L;

    private static final String FILES_TAG = "files";
    private static final String PATH_TAG = "path";
    private static final String OFFENSES_TAG = "offenses";
    private static final String SEVERITY_TAG = "severity";
    private static final String MESSAGE_TAG = "message";
    private static final String COP_NAME_TAG = "cop_name";
    private static final String CORRECTABLE_TAG = "correctable";
    private static final String LOCATION_TAG = "location";
    private static final String START_LINE_TAG = "start_line";
    private static final String START_COLUMN_TAG = "start_column";
    private static final String LAST_LINE_TAG = "last_line";
    private static final String LAST_COLUMN_TAG = "last_column";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (!jsonReport.has(FILES_TAG)) {
            return;
        }

        var files = jsonReport.getJSONArray(FILES_TAG);
        for (int i = 0; i < files.length(); i++) {
            var fileEntry = files.getJSONObject(i);
            var filePath = fileEntry.optString(PATH_TAG, "-");
            var offenses = fileEntry.optJSONArray(OFFENSES_TAG);
            if (offenses != null) {
                parseOffenses(report, offenses, filePath, issueBuilder);
            }
        }
    }

    private void parseOffenses(final Report report, final JSONArray offenses,
            final String filePath, final IssueBuilder issueBuilder) {
        for (int i = 0; i < offenses.length(); i++) {
            var offense = offenses.getJSONObject(i);
            report.add(convertToIssue(offense, filePath, issueBuilder));
        }
    }

    private edu.hm.hafner.analysis.Issue convertToIssue(final JSONObject offense, final String filePath,
            final IssueBuilder issueBuilder) {
        issueBuilder.setFileName(filePath)
                .setMessage(offense.optString(MESSAGE_TAG, "-"))
                .setType(offense.optString(COP_NAME_TAG, "-"))
                .setSeverity(mapSeverity(offense.optString(SEVERITY_TAG, "warning")));

        applyLocation(offense.optJSONObject(LOCATION_TAG), issueBuilder);

        if (offense.optBoolean(CORRECTABLE_TAG, false)) {
            issueBuilder.setAdditionalProperties("correctable");
        }

        return issueBuilder.buildAndClean();
    }

    private void applyLocation(final JSONObject location, final IssueBuilder issueBuilder) {
        if (location == null) {
            return;
        }

        issueBuilder.setLineStart(location.optInt(START_LINE_TAG))
                .setLineEnd(location.optInt(LAST_LINE_TAG))
                .setColumnStart(location.optInt(START_COLUMN_TAG))
                .setColumnEnd(location.optInt(LAST_COLUMN_TAG));
    }

    /**
     * Maps a RuboCop/Cookstyle severity string to an analysis model {@link Severity}.
     *
     * @param severity
     *         the RuboCop severity string
     *
     * @return the mapped {@link Severity}
     */
    static Severity mapSeverity(final String severity) {
        return switch (severity.toLowerCase()) {
            case "fatal", "error" -> Severity.ERROR;
            case "warning" -> Severity.WARNING_HIGH;
            case "convention", "refactor" -> Severity.WARNING_NORMAL;
            case "info" -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}
