package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Staticcheck JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://staticcheck.dev/">Staticcheck</a>
 * @see <a href="https://github.com/dominikh/go-tools">Staticcheck on GitHub</a>
 */
public class StaticcheckParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 413414650434266749L;

    private static final String DIAGNOSTICS = "diagnostics";
    private static final String LOCATION = "location";
    private static final String END = "end";
    private static final String FILE = "file";
    private static final String LINE = "line";
    private static final String COLUMN = "column";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String SEVERITY = "severity";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(DIAGNOSTICS)) {
            parseDiagnostics(report, jsonReport.getJSONArray(DIAGNOSTICS), issueBuilder);
        }
        else if (looksLikeIssue(jsonReport)) {
            report.add(convertToIssue(jsonReport, issueBuilder));
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseDiagnostics(report, jsonReport, issueBuilder);
    }

    private void parseDiagnostics(final Report report, final JSONArray diagnostics, final IssueBuilder issueBuilder) {
        for (int i = 0; i < diagnostics.length(); i++) {
            report.add(convertToIssue(diagnostics.optJSONObject(i), issueBuilder));
        }
    }

    private boolean looksLikeIssue(final JSONObject jsonReport) {
        return jsonReport.has(MESSAGE) || jsonReport.has(CODE) || jsonReport.has(LOCATION);
    }

    private Issue convertToIssue(@CheckForNull final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue == null) {
            return issueBuilder.buildAndClean();
        }

        issueBuilder.setType(jsonIssue.optString(CODE, "-"))
                .guessSeverity(jsonIssue.optString(SEVERITY, "warning"))
                .setMessage(jsonIssue.optString(MESSAGE));

        applyLocation(jsonIssue.optJSONObject(LOCATION), issueBuilder);
        applyEnd(jsonIssue.optJSONObject(END), issueBuilder, LINE, COLUMN);

        return issueBuilder.buildAndClean();
    }

    private void applyLocation(@CheckForNull final JSONObject location, final IssueBuilder issueBuilder) {
        if (location == null) {
            return;
        }

        issueBuilder.setFileName(location.optString(FILE));
        applyStart(location, issueBuilder, LINE, COLUMN);
        applyEnd(location.optJSONObject(END), issueBuilder, LINE, COLUMN);
    }
}