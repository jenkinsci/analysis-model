package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.Locale;
import java.util.Map;

/**
 * A parser for Spectral JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/stoplightio/spectral">Spectral on GitHub</a>
 */
public class SpectralParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 5715106237400999068L;

    private static final String RESULTS = "results";
    private static final String CODE = "code";
    private static final String MESSAGE = "message";
    private static final String SEVERITY = "severity";
    private static final String SOURCE = "source";
    private static final String RANGE = "range";
    private static final String START = "start";
    private static final String END = "end";
    private static final String LINE = "line";
    private static final String CHARACTER = "character";
    private static final Map<String, String> STRING_SEVERITIES = Map.ofEntries(
            Map.entry("0", "error"),
            Map.entry("error", "error"),
            Map.entry("fatal", "error"),
            Map.entry("1", "warning"),
            Map.entry("warn", "warning"),
            Map.entry("warning", "warning"),
            Map.entry("2", "notice"),
            Map.entry("3", "notice"),
            Map.entry("info", "notice"),
            Map.entry("information", "notice"),
            Map.entry("hint", "notice")
    );

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(RESULTS)) {
            parseJsonArray(report, jsonReport.optJSONArray(RESULTS), issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport == null) {
            return;
        }

        for (int i = 0; i < jsonReport.length(); i++) {
            var issue = jsonReport.optJSONObject(i);
            report.add(convertToIssue(issue, issueBuilder));
        }
    }

    private Issue convertToIssue(@CheckForNull final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        if (jsonIssue == null) {
            return issueBuilder.buildAndClean();
        }

        issueBuilder.setType(jsonIssue.optString(CODE, "-"))
                .setMessage(jsonIssue.optString(MESSAGE))
                .setFileName(jsonIssue.optString(SOURCE, "-"));

        applySeverity(jsonIssue.opt(SEVERITY), issueBuilder);
        applyRange(jsonIssue.optJSONObject(RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applySeverity(@CheckForNull final Object rawSeverity, final IssueBuilder issueBuilder) {
        if (rawSeverity == null) {
            issueBuilder.guessSeverity("warning");
            return;
        }

        if (rawSeverity instanceof Number severityNumber) {
            issueBuilder.guessSeverity(mapNumericSeverity(severityNumber.intValue()));
            return;
        }

        if (rawSeverity instanceof String severityLabel) {
            issueBuilder.guessSeverity(mapStringSeverity(severityLabel));
            return;
        }

        issueBuilder.guessSeverity("warning");
    }

    private String mapNumericSeverity(final int severity) {
        return switch (severity) {
            case 0 -> "error";
            case 1 -> "warning";
            case 2, 3 -> "notice";
            default -> "warning";
        };
    }

    private String mapStringSeverity(final String severity) {
        var normalized = severity.trim().toLowerCase(Locale.ROOT);
        return STRING_SEVERITIES.getOrDefault(normalized, "warning");
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        applyStart(range.optJSONObject(START), issueBuilder);
        applyEnd(range.optJSONObject(END), issueBuilder);
    }

    private void applyStart(@CheckForNull final JSONObject start, final IssueBuilder issueBuilder) {
        if (start == null) {
            return;
        }

        issueBuilder.setLineStart(start.optInt(LINE)).setColumnStart(start.optInt(CHARACTER));
    }

    private void applyEnd(@CheckForNull final JSONObject end, final IssueBuilder issueBuilder) {
        if (end == null) {
            return;
        }

        issueBuilder.setLineEnd(end.optInt(LINE)).setColumnEnd(end.optInt(CHARACTER));
    }
}