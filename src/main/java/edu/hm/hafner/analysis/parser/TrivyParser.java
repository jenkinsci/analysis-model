package edu.hm.hafner.analysis.parser;

import java.text.MessageFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * <p>
 * Parser for reports of aquasec trivy container vulnerability scanner.
 * </p>
 * <p>
 * <strong>Usage: </strong>trivy image -f json -o results.json golang:1.12-alpine
 * </p>
 *
 * @author Thomas Fürer - tfuerer.javanet@gmail.com
 */
public class TrivyParser extends JsonIssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_CRITICAL = "critcal";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_HIGH = "high";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_MEDIUM = "medium";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_LOW = "low";
    private static final long serialVersionUID = 1L;

    /**
     * Used with schema version 2 starting with trivy 0.20.0.
     */
    @Override
    protected void parseJsonObject(Report report, JSONObject jsonReport, IssueBuilder issueBuilder) {
        parseResults(report, jsonReport.optJSONArray("Results"), issueBuilder);
    }

    /**
     * Used with older schema before trivy 0.20.0.
     */
    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseResults(report, jsonReport, issueBuilder);
    }

    private void parseResults(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            JSONObject component = (JSONObject)jsonReport.get(i);
            if (!component.isNull("Vulnerabilities")) {
                JSONArray vulnerabilities = component.getJSONArray("Vulnerabilities");
                for (Object vulnerability : vulnerabilities) {
                    report.add(convertToIssue((JSONObject)vulnerability, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        return issueBuilder.setFileName(vulnerability.optString("PkgName", VALUE_NOT_SET))
                .setCategory(vulnerability.optString("SeveritySource", VALUE_NOT_SET))
                .setSeverity(mapSeverity(vulnerability.optString("Severity", "UNKNOWN")))
                .setType(vulnerability.optString("VulnerabilityID", VALUE_NOT_SET))
                .setMessage(vulnerability.optString("Title", "UNKNOWN"))
                .setDescription(formatDescription(vulnerability))
                .buildAndClean();
    }

    @SuppressFBWarnings("IMPROPER_UNICODE")
    private Severity mapSeverity(final String string) {
        if (TRIVY_VULNERABILITY_LEVEL_TAG_LOW.equalsIgnoreCase(string)) {
            return Severity.WARNING_LOW;
        }
        else if (TRIVY_VULNERABILITY_LEVEL_TAG_MEDIUM.equalsIgnoreCase(string)) {
            return Severity.WARNING_NORMAL;
        }
        else if (TRIVY_VULNERABILITY_LEVEL_TAG_HIGH.equalsIgnoreCase(string)
                || TRIVY_VULNERABILITY_LEVEL_TAG_CRITICAL.equalsIgnoreCase(string)) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }

    private String formatDescription(final JSONObject vulnerability) {
        return MessageFormat.format(
                "<p><div><b>File</b>: {0}</div><div><b>Installed Version:</b> {1}</div><div><b>Fixed Version:</b> {2}</div><div><b>Severity:</b> {3}</div>",
                vulnerability.optString("PkgName", VALUE_NOT_SET),
                vulnerability.optString("InstalledVersion", VALUE_NOT_SET),
                vulnerability.optString("FixedVersion", "still open"), vulnerability.optString("Severity", "UNKOWN"))
                + "<p>" + vulnerability.optString("Description", "") + "</p>";
    }
}
