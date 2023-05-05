package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static j2html.TagCreator.*;

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
            final Object item = resources.get(i);
            if (item instanceof JSONObject) {
                final JSONObject resourceWrapper = (JSONObject) item;
                if (!resourceWrapper.isNull("vulnerabilities") && !resourceWrapper.isNull("resource")) {
                    final JSONObject resource = resourceWrapper.getJSONObject("resource");
                    parseVulnerabilities(report, issueBuilder, resourceWrapper, resource);
                }
            }
        }
    }

    private void parseVulnerabilities(final Report report, final IssueBuilder issueBuilder,
            final JSONObject resourceWrapper, final JSONObject resource) {
        final JSONArray vulnerabilities = resourceWrapper.getJSONArray("vulnerabilities");
        for (Object vulnerability : vulnerabilities) {
            if (vulnerability instanceof JSONObject) {
                report.add(convertToIssue(resource, (JSONObject) vulnerability, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject resource, final JSONObject vulnerability,
            final IssueBuilder issueBuilder) {
        final String fileName = resource.optString("path", resource.optString("name", VALUE_NOT_SET));
        return issueBuilder
                .setFileName(fileName)
                .setSeverity(mapSeverity(vulnerability.optString("aqua_severity", "UNKNOWN")))
                .setMessage(vulnerability.optString("name", "UNKNOWN"))
                .setDescription(formatDescription(fileName, resource, vulnerability))
                .buildAndClean();
    }

    private Severity mapSeverity(final String string) {
        if (StringUtils.containsAnyIgnoreCase(string, AQUA_VULNERABILITY_LEVEL_TAG_LOW, AQUA_VULNERABILITY_LEVEL_TAG_NEGLIGIBLE)) {
            return Severity.WARNING_LOW;
        }
        else if (StringUtils.containsAnyIgnoreCase(string, AQUA_VULNERABILITY_LEVEL_TAG_MEDIUM)) {
            return Severity.WARNING_NORMAL;
        }
        else if (StringUtils.containsAnyIgnoreCase(string, AQUA_VULNERABILITY_LEVEL_TAG_HIGH, AQUA_VULNERABILITY_LEVEL_TAG_CRITICAL,
                AQUA_VULNERABILITY_LEVEL_TAG_MALWARE, AQUA_VULNERABILITY_LEVEL_TAG_SENSITIVE)) {
            return Severity.WARNING_HIGH;
        }
        else {
            return Severity.WARNING_HIGH;
        }
    }

    private String formatDescription(final String fileName, final JSONObject resource, final JSONObject vulnerability) {
        final String version = resource.optString("version", VALUE_NOT_SET);
        final String severity = vulnerability.optString("aqua_severity", "UNKOWN");
        final String description = vulnerability.optString("description", "");
        return join(div(b("Resource: "), text(fileName)),
                div(b("Installed Version: "), text(version)),
                div(b("Aqua Severity: "), text(severity)),
                p(text(description))).render();
    }

}
