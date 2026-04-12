package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;
import java.util.Map;
import java.util.TreeMap;

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
    private static final Map<String, Severity> STRING_SEVERITIES = createSeverityMapping();

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(RESULTS)) {
            parseIssues(report, jsonReport.optJSONArray(RESULTS), issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseIssues(report, jsonReport, issueBuilder);
    }

    private void parseIssues(final Report report, @CheckForNull final JSONArray issues, final IssueBuilder issueBuilder) {
        if (issues == null) {
            return;
        }

        for (int i = 0; i < issues.length(); i++) {
            var issue = issues.optJSONObject(i);
            if (issue != null) {
                report.add(convertToIssue(issue, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder issueBuilder) {
        issueBuilder.setType(jsonIssue.optString(CODE, "-"))
                .setMessage(jsonIssue.optString(MESSAGE))
                .setFileName(jsonIssue.optString(SOURCE, "-"));

        applySeverity(jsonIssue.opt(SEVERITY), issueBuilder);
        applyRange(jsonIssue.optJSONObject(RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applySeverity(@CheckForNull final Object rawSeverity, final IssueBuilder issueBuilder) {
        if (rawSeverity == null) {
            return;
        }

        issueBuilder.setSeverity(mapStringSeverity(String.valueOf(rawSeverity)));
    }

    private Severity mapStringSeverity(final String severity) {
        return STRING_SEVERITIES.getOrDefault(severity.trim(), Severity.WARNING_NORMAL);
    }

    private static Map<String, Severity> createSeverityMapping() {
        var mapping = new TreeMap<String, Severity>(String.CASE_INSENSITIVE_ORDER);

        mapping.put("0", Severity.ERROR);
        mapping.put("error", Severity.ERROR);
        mapping.put("fatal", Severity.ERROR);

        mapping.put("1", Severity.WARNING_NORMAL);
        mapping.put("warn", Severity.WARNING_NORMAL);
        mapping.put("warning", Severity.WARNING_NORMAL);

        mapping.put("2", Severity.WARNING_LOW);
        mapping.put("3", Severity.WARNING_LOW);
        mapping.put("info", Severity.WARNING_LOW);
        mapping.put("information", Severity.WARNING_LOW);
        mapping.put("hint", Severity.WARNING_LOW);

        return mapping;
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        applyStart(range.optJSONObject(START), issueBuilder, LINE, CHARACTER);
        applyEnd(range.optJSONObject(END), issueBuilder, LINE, CHARACTER);
    }
}