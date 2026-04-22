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

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        int addedIssues = 0;
        var defaultFileName = firstNonBlank(jsonReport, "path", "filename", "file", "file_path", "filepath");

        addedIssues += parseFileEntries(report, issueBuilder, jsonReport.optJSONArray("scanned_paths"));
        addedIssues += parseFileEntries(report, issueBuilder, jsonReport.optJSONArray("results"));
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray("incidents"), defaultFileName, "", "");
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray("policy_breaks"),
            defaultFileName, "", "");
        addedIssues += parseFindings(report, issueBuilder, jsonReport.optJSONArray("matches"), defaultFileName,
            "", "");
        addedIssues += parseSecrets(report, issueBuilder, jsonReport.optJSONArray("secrets"), defaultFileName);

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

            var fileName = firstNonBlank(entry, "path", "filename", "file", "file_path", "filepath");
            int addedForEntry = 0;
            addedForEntry += parsePolicies(report, issueBuilder, entry.optJSONArray("policies"), fileName);
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray("incidents"), fileName, "", "");
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray("policy_breaks"), fileName, "", "");
            addedForEntry += parseFindings(report, issueBuilder, entry.optJSONArray("matches"), fileName, "", "");
            addedForEntry += parseSecrets(report, issueBuilder, entry.optJSONArray("secrets"), fileName);

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

            var defaultType = firstNonBlank(policy, "name", "policy", "policy_name", "type");
            var defaultDescription = firstNonBlank(policy, "details", "description");

            int addedForPolicy = 0;
            addedForPolicy += parseFindings(report, issueBuilder,
                    policy.optJSONArray("breaks"), defaultFileName, defaultType, defaultDescription);
            addedForPolicy += parseFindings(report, issueBuilder,
                    policy.optJSONArray("incidents"), defaultFileName, defaultType, defaultDescription);

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

            var defaultType = firstNonBlank(secret, "policy", "policy_name", "name", "detector_name", "type");
            var defaultDescription = firstNonBlank(secret, "details", "description");

            int addedForSecret = 0;
            addedForSecret += parseFindings(report, issueBuilder,
                    secret.optJSONArray("occurrences"), defaultFileName, defaultType, defaultDescription);
            addedForSecret += parseFindings(report, issueBuilder,
                    secret.optJSONArray("matches"), defaultFileName, defaultType, defaultDescription);

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
        var fileName = firstNonBlank(finding, "filename", "file", "path", "file_path", "filepath");
        if (fileName.isBlank()) {
            fileName = defaultFileName;
        }

        var type = firstNonBlank(finding,
                "detector_name", "detector", "policy", "policy_name", "name", "type", "id", "detector_id");
        if (type.isBlank()) {
            type = defaultType;
        }
        if (type.isBlank()) {
            type = "-";
        }

        var message = firstNonBlank(finding, "message", "match", "name");
        if (message.isBlank()) {
            message = "Secret detected";
        }

        var description = firstNonBlank(finding, "details", "description", "incident_url");
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

        var severity = firstNonBlank(finding, "severity", "confidence");
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
        var location = finding.optJSONObject("location");

        int lineStart = firstPositive(
                finding.optInt("line_start", 0),
                finding.optInt("line", 0),
                finding.optInt("start_line", 0),
                getNestedInt(location, "start", "line"));
        if (lineStart > 0) {
            issueBuilder.setLineStart(lineStart);
        }

        int lineEnd = firstPositive(
                finding.optInt("line_end", 0),
                finding.optInt("end_line", 0),
                getNestedInt(location, "end", "line"),
                lineStart);
        if (lineEnd > 0) {
            issueBuilder.setLineEnd(lineEnd);
        }

        int columnStart = firstPositive(
                finding.optInt("index_start", 0),
                finding.optInt("column", 0),
                finding.optInt("start_column", 0),
                getNestedInt(location, "start", "column"));
        if (columnStart > 0) {
            issueBuilder.setColumnStart(columnStart);
        }

        int columnEnd = firstPositive(
                finding.optInt("index_end", 0),
                finding.optInt("end_column", 0),
                getNestedInt(location, "end", "column"),
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
        return jsonObject.has("line_start") || jsonObject.has("line")
                || jsonObject.has("match") || jsonObject.has("message")
                || jsonObject.has("detector_name") || jsonObject.has("policy")
                || jsonObject.has("type") || jsonObject.has("id")
                || jsonObject.has("severity") || jsonObject.has("confidence");
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
