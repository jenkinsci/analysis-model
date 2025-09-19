package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.Strings;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

import static j2html.TagCreator.*;

/**
 * Parser for Aqua Scanner CLI (scannercli) tool.
 *
 * @author Juri Duval
 */
public class AquaScannerParser extends JsonIssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_HIGH = "high";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_MEDIUM = "medium";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_LOW = "low";
    private static final String AQUA_VULNERABILITY_LEVEL_TAG_NEGLIGIBLE = "negligible";
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var resources = jsonReport.optJSONArray("resources");
        if (resources != null) {
            parseResources(report, resources, issueBuilder);
        }
    }

    private void parseResources(final Report report, final JSONArray resources, final IssueBuilder issueBuilder) {
        for (int i = 0; i < resources.length(); i++) {
            var item = resources.get(i);
            if (item instanceof JSONObject resourceWrapper
                    && !resourceWrapper.isNull("vulnerabilities")
                    && !resourceWrapper.isNull("resource")) {
                var resource = resourceWrapper.getJSONObject("resource");
                parseVulnerabilities(report, issueBuilder, resourceWrapper, resource);
            }
        }
    }

    private void parseVulnerabilities(final Report report, final IssueBuilder issueBuilder,
            final JSONObject resourceWrapper, final JSONObject resource) {
        var vulnerabilities = resourceWrapper.getJSONArray("vulnerabilities");
        for (Object vulnerability : vulnerabilities) {
            if (vulnerability instanceof JSONObject object) {
                report.add(convertToIssue(resource, object, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject resource, final JSONObject vulnerability,
            final IssueBuilder issueBuilder) {
        var fileName = resource.optString("path", resource.optString("name", VALUE_NOT_SET));
        return issueBuilder
                .setFileName(fileName)
                .setSeverity(mapSeverity(vulnerability.optString("aqua_severity", "UNKNOWN")))
                .setMessage(vulnerability.optString("name", "UNKNOWN"))
                .setDescription(formatDescription(fileName, resource, vulnerability))
                .buildAndClean();
    }

    private Severity mapSeverity(final String string) {
        if (Strings.CI.containsAny(string, AQUA_VULNERABILITY_LEVEL_TAG_LOW,
                AQUA_VULNERABILITY_LEVEL_TAG_NEGLIGIBLE)) {
            return Severity.WARNING_LOW;
        }
        else if (Strings.CI.equals(string, AQUA_VULNERABILITY_LEVEL_TAG_MEDIUM)) {
            return Severity.WARNING_NORMAL;
        }
        else if (Strings.CI.equals(string, AQUA_VULNERABILITY_LEVEL_TAG_HIGH)) {
            return Severity.WARNING_HIGH;
        }
        return Severity.ERROR;
    }

    private String formatDescription(final String fileName, final JSONObject resource, final JSONObject vulnerability) {
        var version = resource.optString("version", VALUE_NOT_SET);
        var severity = vulnerability.optString("aqua_severity", "UNKOWN");
        var description = vulnerability.optString("description", "");
        return join(div(b("Resource: "), text(fileName)),
                div(b("Installed Version: "), text(version)),
                div(b("Aqua Severity: "), text(severity)),
                p(text(description))).render();
    }
}
