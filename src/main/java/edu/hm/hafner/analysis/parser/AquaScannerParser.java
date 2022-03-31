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
 * Parser for Aqua Scanner CLI (scannercli) tool.
 *
 * @author Juri Duval
 */
public class AquaScannerParser extends JsonIssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_SENSITIVE = "sensitive";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_MALWARE = "malware";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_CRITICAL = "critical";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_HIGH = "high";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_MEDIUM = "medium";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_LOW = "low";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_NEGLIGIBLE = "negligible";
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONArray resources = jsonReport.optJSONArray("resources");
        if (resources != null) {
            parseResources(report, resources, issueBuilder);
        }
    }

    private void parseResources(final Report report, final JSONArray resources, final IssueBuilder issueBuilder) {
        for (int i = 0; i < resources.length(); i++) {
            JSONObject resourceWrapper = (JSONObject) resources.get(i);
            if (!resourceWrapper.isNull("vulnerabilities") && !resourceWrapper.isNull("resource")) {
                JSONObject resource = resourceWrapper.getJSONObject("resource");
                JSONArray vulnerabilities = resourceWrapper.getJSONArray("vulnerabilities");
                for (Object vulnerability : vulnerabilities) {
                    report.add(convertToIssue(resource, (JSONObject) vulnerability, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject resource, final JSONObject vulnerability,
            final IssueBuilder issueBuilder) {
        final String fileName = resource.isNull("path") ? resource.optString("name", VALUE_NOT_SET)
                : resource.getString("path");
        return issueBuilder
                .setFileName(fileName)
                .setSeverity(mapSeverity(vulnerability.optString("aqua_severity", "UNKNOWN")))
                .setMessage(vulnerability.optString("name", "UNKNOWN"))
                .setDescription(formatDescription(fileName, resource, vulnerability))
                .buildAndClean();
    }

    @SuppressFBWarnings("IMPROPER_UNICODE")
    private Severity mapSeverity(final String string) {
        if (AQUA_VULNERABILITY_LEVEL_TAG_LOW.equalsIgnoreCase(string)
                || AQUA_VULNERABILITY_LEVEL_TAG_NEGLIGIBLE.equalsIgnoreCase(string)) {
            return Severity.WARNING_LOW;
        }
        else if (AQUA_VULNERABILITY_LEVEL_TAG_MEDIUM.equalsIgnoreCase(string)) {
            return Severity.WARNING_NORMAL;
        }
        else if (AQUA_VULNERABILITY_LEVEL_TAG_HIGH.equalsIgnoreCase(string)
                || AQUA_VULNERABILITY_LEVEL_TAG_CRITICAL.equalsIgnoreCase(string)
                || AQUA_VULNERABILITY_LEVEL_TAG_MALWARE.equalsIgnoreCase(string)
                || AQUA_VULNERABILITY_LEVEL_TAG_SENSITIVE.equalsIgnoreCase(string)) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }

    private String formatDescription(final String fileName, final JSONObject resource,
            final JSONObject vulnerability) {
        return MessageFormat.format(
                "<p><div><b>Resource</b>: {0}</div><div><b>Installed Version:</b> {1}</div><div><b>Aqua Severity:</b> {2}</div>",
                fileName,
                resource.optString("version", VALUE_NOT_SET),
                vulnerability.optString("aqua_severity", "UNKOWN"))
                + "<p>" + vulnerability.optString("description", "") + "</p>";
    }
}
