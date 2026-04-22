package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for GitGuardian (ggshield) JSON reports.
 *
 * @author Akash Manna
 * @see <a href="https://www.gitguardian.com/">GitGuardian</a>
 */
public class GitGuardianParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 2161757376885991300L;

    private static final String SCANNED_PATHS = "scanned_paths";
    private static final String RESULTS = "results";
    private static final String INCIDENTS = "incidents";
    private static final String POLICY_BREAKS = "policy_breaks";
    private static final String MATCHES = "matches";
    private static final String SECRETS = "secrets";
    private static final String POLICIES = "policies";

    private static final String FILENAME = "filename";
    private static final String FILE = "file";
    private static final String PATH = "path";
    private static final String FILE_PATH = "file_path";
    private static final String FILEPATH = "filepath";

    private static final String DETECTOR_NAME = "detector_name";
    private static final String DETECTOR = "detector";
    private static final String POLICY = "policy";
    private static final String POLICY_NAME = "policy_name";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String DETECTOR_ID = "detector_id";

    private static final String MESSAGE = "message";
    private static final String MATCH = "match";

    private static final String DETAILS = "details";
    private static final String DESCRIPTION = "description";
    private static final String INCIDENT_URL = "incident_url";

    private static final String SEVERITY = "severity";
    private static final String CONFIDENCE = "confidence";

    private static final String LOCATION = "location";
    private static final String LINE_START = "line_start";
    private static final String LINE = "line";
    private static final String START_LINE = "start_line";
    private static final String LINE_END = "line_end";
    private static final String END_LINE = "end_line";
    private static final String INDEX_START = "index_start";
    private static final String COLUMN = "column";
    private static final String START_COLUMN = "start_column";
    private static final String INDEX_END = "index_end";
    private static final String END_COLUMN = "end_column";
    private static final String START = "start";
    private static final String END = "end";
    private static final String BREAKS = "breaks";
    private static final String OCCURRENCES = "occurrences";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        int addedIssues = 0;
        var defaultFileName = firstNonBlank(jsonReport, PATH, FILENAME, FILE, FILE_PATH, FILEPATH);

        addedIssues += parseFileEntries(report, issueBuilder, jsonReport.optJSONArray(SCANNED_PATHS));
        addedIssues += parseFileEntries(report, issueBuilder, jsonReport.optJSONArray(RESULTS));
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray(INCIDENTS), defaultFileName, "", "");
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray(POLICY_BREAKS),
            defaultFileName, "", "");
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray(MATCHES), defaultFileName,
            "", "");
        addedIssues += parseSecrets(report, issueBuilder, jsonReport.optJSONArray(SECRETS), defaultFileName);

        if (addedIssues == 0 && looksLikeFinding(jsonReport)) {
            report.add(convertToIssue(issueBuilder, jsonReport, defaultFileName, "", ""));
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var item = jsonReport.optJSONObject(i);
            if (item != null) {
                parseJsonObject(report, item, issueBuilder);
            }
        }
    }

    private int parseFileEntries(final Report report, final IssueBuilder issueBuilder, final JSONArray entries) {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }

        int added = 0;
        for (int i = 0; i < entries.length(); i++) {
            var entry = entries.optJSONObject(i);
            if (entry == null) {
                continue;
            }

            var fileName = firstNonBlank(entry, PATH, FILENAME, FILE, FILE_PATH, FILEPATH);
            int addedForEntry = 0;
            addedForEntry += parsePolicies(report, issueBuilder, entry.optJSONArray(POLICIES), fileName);
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray(INCIDENTS), fileName, "", "");
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray(POLICY_BREAKS), fileName, "", "");
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray(MATCHES), fileName, "", "");
            addedForEntry += parseSecrets(report, issueBuilder, entry.optJSONArray(SECRETS), fileName);

            if (addedForEntry == 0 && looksLikeFinding(entry)) {
                report.add(convertToIssue(issueBuilder, entry, fileName, "", ""));
                addedForEntry++;
            }

            added += addedForEntry;
        }
        return added;
    }

    private int parsePolicies(final Report report, final IssueBuilder issueBuilder, final JSONArray policies,
            final String defaultFileName) {
        if (policies == null || policies.isEmpty()) {
            return 0;
        }

        int added = 0;
        for (int i = 0; i < policies.length(); i++) {
            var policy = policies.optJSONObject(i);
            if (policy == null) {
                continue;
            }

            var defaultType = firstNonBlank(policy, NAME, POLICY, POLICY_NAME, TYPE);
            var defaultDescription = firstNonBlank(policy, DETAILS, DESCRIPTION);

            int addedForPolicy = 0;
            addedForPolicy += parseFindings(report, issueBuilder,
                    policy.optJSONArray(BREAKS), defaultFileName, defaultType, defaultDescription);
            addedForPolicy += parseFindings(report, issueBuilder,
                    policy.optJSONArray(INCIDENTS), defaultFileName, defaultType, defaultDescription);

            if (addedForPolicy == 0 && looksLikeFinding(policy)) {
                report.add(convertToIssue(issueBuilder, policy, defaultFileName, defaultType, defaultDescription));
                addedForPolicy++;
            }

            added += addedForPolicy;
        }
        return added;
    }

    private int parseSecrets(final Report report, final IssueBuilder issueBuilder, final JSONArray secrets,
            final String defaultFileName) {
        if (secrets == null || secrets.isEmpty()) {
            return 0;
        }

        int added = 0;
        for (int i = 0; i < secrets.length(); i++) {
            var secret = secrets.optJSONObject(i);
            if (secret == null) {
                continue;
            }

            var defaultType = firstNonBlank(secret, POLICY, POLICY_NAME, NAME, DETECTOR_NAME, TYPE);
            var defaultDescription = firstNonBlank(secret, DETAILS, DESCRIPTION);

            int addedForSecret = 0;
            addedForSecret += parseFindings(report, issueBuilder,
                    secret.optJSONArray(OCCURRENCES), defaultFileName, defaultType, defaultDescription);
            addedForSecret += parseFindings(report, issueBuilder,
                    secret.optJSONArray(MATCHES), defaultFileName, defaultType, defaultDescription);

            if (addedForSecret == 0 && looksLikeFinding(secret)) {
                report.add(convertToIssue(issueBuilder, secret, defaultFileName, defaultType, defaultDescription));
                addedForSecret++;
            }

            added += addedForSecret;
        }
        return added;
    }

    private int parseFindings(final Report report, final IssueBuilder issueBuilder, final JSONArray findings,
            final String defaultFileName, final String defaultType, final String defaultDescription) {
        if (findings == null || findings.isEmpty()) {
            return 0;
        }

        int added = 0;
        for (int i = 0; i < findings.length(); i++) {
            var finding = findings.optJSONObject(i);
            if (finding != null) {
                report.add(convertToIssue(issueBuilder, finding, defaultFileName, defaultType, defaultDescription));
                added++;
            }
        }
        return added;
    }

    private Issue convertToIssue(final IssueBuilder issueBuilder, final JSONObject finding,
            final String defaultFileName, final String defaultType, final String defaultDescription) {
        var fileName = firstNonBlank(finding, FILENAME, FILE, PATH, FILE_PATH, FILEPATH);
        if (fileName.isBlank()) {
            fileName = defaultFileName;
        }

        var type = firstNonBlank(finding,
                DETECTOR_NAME, DETECTOR, POLICY, POLICY_NAME, NAME, TYPE, ID, DETECTOR_ID);
        if (type.isBlank()) {
            type = defaultType;
        }
        if (type.isBlank()) {
            type = "-";
        }

        var message = firstNonBlank(finding, MESSAGE, MATCH, NAME);
        if (message.isBlank()) {
            message = "Secret detected";
        }

        var description = firstNonBlank(finding, DETAILS, DESCRIPTION, INCIDENT_URL);
        if (description.isBlank()) {
            description = defaultDescription;
        }

        issueBuilder
                .setFileName(fileName)
                .setType(type)
                .setMessage(message);

        if (!description.isBlank()) {
            issueBuilder.setDescription(description);
        }

        var severity = firstNonBlank(finding, SEVERITY, CONFIDENCE);
        if (severity.isBlank()) {
            issueBuilder.setSeverity(Severity.WARNING_HIGH);
        }
        else {
            issueBuilder.guessSeverity(severity);
        }

        applyLocation(finding, issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private void applyLocation(final JSONObject finding, final IssueBuilder issueBuilder) {
        var location = finding.optJSONObject(LOCATION);

        int lineStart = firstPositive(
                finding.optInt(LINE_START, 0),
                finding.optInt(LINE, 0),
                finding.optInt(START_LINE, 0),
                getNestedInt(location, START, LINE));
        if (lineStart > 0) {
            issueBuilder.setLineStart(lineStart);
        }

        int lineEnd = firstPositive(
                finding.optInt(LINE_END, 0),
                finding.optInt(END_LINE, 0),
                getNestedInt(location, END, LINE),
                lineStart);
        if (lineEnd > 0) {
            issueBuilder.setLineEnd(lineEnd);
        }

        int columnStart = firstPositive(
                finding.optInt(INDEX_START, 0),
                finding.optInt(COLUMN, 0),
                finding.optInt(START_COLUMN, 0),
                getNestedInt(location, START, COLUMN));
        if (columnStart > 0) {
            issueBuilder.setColumnStart(columnStart);
        }

        int columnEnd = firstPositive(
                finding.optInt(INDEX_END, 0),
                finding.optInt(END_COLUMN, 0),
                getNestedInt(location, END, COLUMN),
                columnStart);
        if (columnEnd > 0) {
            issueBuilder.setColumnEnd(columnEnd);
        }
    }

    private int getNestedInt(final JSONObject object, final String container, final String key) {
        if (object == null) {
            return 0;
        }

        var nested = object.optJSONObject(container);
        if (nested == null) {
            return 0;
        }

        return nested.optInt(key, 0);
    }

    private boolean looksLikeFinding(final JSONObject jsonObject) {
        return jsonObject.has(LINE_START) || jsonObject.has(LINE)
                || jsonObject.has(MATCH) || jsonObject.has(MESSAGE)
                || jsonObject.has(DETECTOR_NAME) || jsonObject.has(POLICY)
                || jsonObject.has(TYPE) || jsonObject.has(ID)
                || jsonObject.has(SEVERITY) || jsonObject.has(CONFIDENCE);
    }

    private int firstPositive(final int... values) {
        for (int value : values) {
            if (value > 0) {
                return value;
            }
        }
        return 0;
    }

    private String firstNonBlank(final JSONObject jsonObject, final String... keys) {
        for (String key : keys) {
            var value = jsonObject.optString(key, "").trim();
            if (!value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
