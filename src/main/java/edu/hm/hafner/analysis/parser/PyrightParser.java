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
 * A parser for Pyright JSON output reports.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/microsoft/pyright">Pyright on GitHub</a>
 */
public class PyrightParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 5343613090552490752L;

    private static final String GENERAL_DIAGNOSTICS = "generalDiagnostics";
    private static final String FILE = "file";
    private static final String SEVERITY = "severity";
    private static final String MESSAGE = "message";
    private static final String RULE = "rule";
    private static final String RANGE = "range";
    private static final String START = "start";
    private static final String END = "end";
    private static final String LINE = "line";
    private static final String CHARACTER = "character";
    private static final String INFORMATION = "information";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(GENERAL_DIAGNOSTICS)) {
            JSONArray diagnostics = jsonReport.getJSONArray(GENERAL_DIAGNOSTICS);
            for (int i = 0; i < diagnostics.length(); i++) {
                report.add(convertToIssue(diagnostics.getJSONObject(i), issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject diagnostic, final IssueBuilder issueBuilder) {
        var severityString = diagnostic.optString(SEVERITY, "warning");
        var severity = mapSeverity(severityString);

        issueBuilder
                .setFileName(diagnostic.optString(FILE, "-"))
                .setMessage(diagnostic.optString(MESSAGE, "-"))
                .setType(diagnostic.optString(RULE, "-"))
                .setSeverity(severity);

        applyRange(diagnostic.optJSONObject(RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private Severity mapSeverity(final String severityString) {
        if (INFORMATION.equalsIgnoreCase(severityString)) {
            return Severity.WARNING_LOW;
        }
        return Severity.guessFromString(severityString);
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        var start = range.optJSONObject(START);
        var end = range.optJSONObject(END);

        // Pyright uses 0-based line numbers; convert to 1-based
        if (start != null) {
            issueBuilder.setLineStart(start.optInt(LINE) + 1)
                    .setColumnStart(start.optInt(CHARACTER));
        }
        if (end != null) {
            issueBuilder.setLineEnd(end.optInt(LINE) + 1)
                    .setColumnEnd(end.optInt(CHARACTER));
        }
    }
}
