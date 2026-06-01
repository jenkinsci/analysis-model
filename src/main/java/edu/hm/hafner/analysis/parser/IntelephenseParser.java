package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.hm.hafner.analysis.util.IntegerParser;

import java.io.Serial;
import java.util.regex.Pattern;

/**
 * A parser for Intelephense diagnostics in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://intelephense.com/">Intelephense</a>
 * @see <a href="https://intelephense.com/docs">Intelephense documentation</a>
 */
public class IntelephenseParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 5991253625633941181L;

    private static final String DIAGNOSTICS = "diagnostics";
    private static final String MESSAGE = "message";
    private static final String CODE = "code";
    private static final String SEVERITY = "severity";
    private static final String SOURCE = "source";
    private static final String URI_TAG = "uri";
    private static final String FILE_NAME = "file_name";
    private static final String FILE = "file";
    private static final String PATH = "path";
    private static final String RANGE = "range";
    private static final String START = "start";
    private static final String END = "end";
    private static final String LINE = "line";
    private static final String CHARACTER = "character";
    private static final Pattern FILE_URI_PREFIX = Pattern.compile("^file://(?<path>.*)$");
    private static final Pattern WINDOWS_FILE_URI_PATH = Pattern.compile("^/[A-Za-z]:/.*");

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var defaultFileName = resolveFileName(jsonReport, "-");
        var defaultSource = getStringOrDefaultIfBlank(jsonReport, SOURCE, "");
        var diagnostics = jsonReport.optJSONArray(DIAGNOSTICS);
        if (diagnostics != null) {
            parseDiagnostics(report, diagnostics, issueBuilder, defaultFileName, defaultSource);
            return;
        }

        if (isDiagnostic(jsonReport)) {
            report.add(convertToIssue(jsonReport, issueBuilder, defaultFileName, defaultSource));
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var item = jsonReport.opt(i);
            if (item instanceof JSONObject jsonObject) {
                var defaultFileName = resolveFileName(jsonObject, "-");
                var defaultSource = getStringOrDefaultIfBlank(jsonObject, SOURCE, "");
                var diagnostics = jsonObject.optJSONArray(DIAGNOSTICS);
                if (diagnostics != null) {
                    parseDiagnostics(report, diagnostics, issueBuilder, defaultFileName, defaultSource);
                }
                else if (isDiagnostic(jsonObject)) {
                    report.add(convertToIssue(jsonObject, issueBuilder, defaultFileName, defaultSource));
                }
            }
        }
    }

    private void parseDiagnostics(final Report report, final JSONArray diagnostics, final IssueBuilder issueBuilder,
            final String defaultFileName, final String defaultSource) {
        for (int i = 0; i < diagnostics.length(); i++) {
            var diagnostic = diagnostics.optJSONObject(i);
            if (diagnostic != null) {
                report.add(convertToIssue(diagnostic, issueBuilder, defaultFileName, defaultSource));
            }
        }
    }

    private boolean isDiagnostic(final JSONObject jsonObject) {
        return jsonObject.has(MESSAGE) || jsonObject.has(RANGE) || jsonObject.has(CODE) || jsonObject.has(SEVERITY);
    }

    private Issue convertToIssue(final JSONObject diagnostic, final IssueBuilder issueBuilder,
            final String defaultFileName, final String defaultSource) {
        var fileName = resolveFileName(diagnostic, defaultFileName);
        var type = firstNonBlank(diagnostic, CODE, "ruleId");
        var message = getStringOrDefaultIfBlank(diagnostic, MESSAGE, "-");
        var severity = parseSeverity(diagnostic.opt(SEVERITY));
        var source = firstNonBlank(diagnostic, SOURCE);
        if (StringUtils.isBlank(source)) {
            source = defaultSource;
        }

        issueBuilder.setFileName(fileName)
                .setType(type)
                .setMessage(message)
                .setSeverity(severity);

        if (StringUtils.isNotBlank(source)) {
            issueBuilder.setCategory(source);
        }

        applyRange(diagnostic.optJSONObject(RANGE), issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private String resolveFileName(final JSONObject issue, final String defaultFileName) {
        var explicitFileName = firstNonBlank(issue, FILE_NAME, FILE, PATH);
        if (StringUtils.isNotBlank(explicitFileName)) {
            return explicitFileName;
        }

        var uri = issue.optString(URI_TAG, "");
        if (StringUtils.isBlank(uri)) {
            return defaultFileName;
        }

        return resolveUri(uri);
    }

    private String resolveUri(final String uri) {
        if (!uri.startsWith("file:")) {
            return uri;
        }

        var matcher = FILE_URI_PREFIX.matcher(uri);
        if (!matcher.matches()) {
            return uri;
        }

        var fileUri = matcher.group("path");
        if (WINDOWS_FILE_URI_PATH.matcher(fileUri).matches()) {
            return fileUri.substring(1);
        }

        return fileUri;
    }

    private void applyRange(@CheckForNull final JSONObject range, final IssueBuilder issueBuilder) {
        if (range == null) {
            return;
        }

        applyPosition(range.optJSONObject(START), issueBuilder, true);
        applyPosition(range.optJSONObject(END), issueBuilder, false);
    }

    private void applyPosition(@CheckForNull final JSONObject position, final IssueBuilder issueBuilder,
            final boolean start) {
        if (position == null) {
            return;
        }

        var line = position.optInt(LINE, -1);
        if (line >= 0) {
            if (start) {
                issueBuilder.setLineStart(line + 1);
            }
            else {
                issueBuilder.setLineEnd(line + 1);
            }
        }

        var character = position.optInt(CHARACTER, -1);
        if (character >= 0) {
            if (start) {
                issueBuilder.setColumnStart(character + 1);
            }
            else {
                issueBuilder.setColumnEnd(character);
            }
        }
    }

    private Severity parseSeverity(final Object severityValue) {
        if (severityValue instanceof Number number) {
            return parseSeverity(number.intValue());
        }
        if (severityValue instanceof String string) {
            if (StringUtils.isNumeric(string)) {
                return parseSeverity(IntegerParser.parseInt(string));
            }
            else {
                return Severity.guessFromString(string);
            }
        }

        return Severity.WARNING_NORMAL;
    }

    private Severity parseSeverity(final int severityValue) {
        return switch (severityValue) {
            case 1 -> Severity.ERROR;
            case 2 -> Severity.WARNING_HIGH;
            case 3 -> Severity.WARNING_NORMAL;
            case 4 -> Severity.WARNING_LOW;
            default -> Severity.WARNING_NORMAL;
        };
    }
}