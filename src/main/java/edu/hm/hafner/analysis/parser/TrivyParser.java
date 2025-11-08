package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static j2html.TagCreator.*;

/**
 * Parser for reports of aquasec trivy container vulnerability scanner.
 *
 * <p>
 * <strong>Usage: </strong>trivy image -f json -o results.json golang:1.12-alpine
 * </p>
 *
 * <p>
 * The parser supports scanner results from:
 * </p>
 * <ul>
 *  <li>Vulnerability Scanner</li>
 *  <li>Misconfiguration Scanner</li>
 * </ul>
 *
 * @author Thomas Fürer - tfuerer.javanet@gmail.com
 */
public class TrivyParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String VALUE_NOT_SET = "-";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_HIGH = "high";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_MEDIUM = "medium";
    private static final String TRIVY_VULNERABILITY_LEVEL_TAG_LOW = "low";

    /**
     * Used with schema version 2 starting with trivy 0.20.0.
     */
    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var results = jsonReport.optJSONArray("Results");
        if (results != null) {
            parseResults(report, results, issueBuilder);
        }
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
            var component = (JSONObject) jsonReport.get(i);
            if (!component.isNull("Vulnerabilities")) {
                for (Object vulnerability : component.getJSONArray("Vulnerabilities")) {
                    report.add(convertToVulnerabilityIssue((JSONObject) vulnerability, issueBuilder));
                }
            }
            if (!component.isNull("Misconfigurations")) {
                for (Object misconfiguration : component.getJSONArray("Misconfigurations")) {
                    issueBuilder.setFileName(component.optString("Target", VALUE_NOT_SET));
                    report.add(convertToMisconfigurationIssue((JSONObject) misconfiguration, issueBuilder));
                }
            }
        }
    }

    private Issue convertToVulnerabilityIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        return issueBuilder.setFileName(vulnerability.optString("PkgName", VALUE_NOT_SET))
                .setCategory(vulnerability.optString("SeveritySource", VALUE_NOT_SET))
                .setSeverity(mapSeverity(vulnerability.optString("Severity", "UNKNOWN")))
                .setType(vulnerability.optString("VulnerabilityID", VALUE_NOT_SET))
                .setMessage(vulnerability.optString("Title", "UNKNOWN"))
                .setDescription(formatVulnerabilityDescription(vulnerability))
                .buildAndClean();
    }

    private Issue convertToMisconfigurationIssue(final JSONObject misconfiguration, final IssueBuilder issueBuilder) {
        var causeMetadata = misconfiguration.optJSONObject("CauseMetadata");
        if (causeMetadata != null) {
            var startLine = causeMetadata.optInt("StartLine", 0);
            if (startLine > 0) {
                issueBuilder.setLineStart(startLine);
            }
            var endLine = causeMetadata.optInt("EndLine", 0);
            if (endLine > 0) {
                issueBuilder.setLineEnd(endLine);
            }
        }

        return issueBuilder
                .setCategory(misconfiguration.optString("Type", VALUE_NOT_SET))
                .setSeverity(mapSeverity(misconfiguration.optString("Severity", "UNKNOWN")))
                .setType(misconfiguration.optString("ID", VALUE_NOT_SET))
                .setMessage(misconfiguration.optString("Title", "UNKNOWN"))
                .setDescription(formatMisconfigurationDescription(misconfiguration))
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
        else if (TRIVY_VULNERABILITY_LEVEL_TAG_HIGH.equalsIgnoreCase(string)) {
            return Severity.WARNING_HIGH;
        }
        return Severity.ERROR;
    }

    private String formatVulnerabilityDescription(final JSONObject vulnerability) {
        var fileName = vulnerability.optString("PkgName", VALUE_NOT_SET);
        var installedVersion = vulnerability.optString("InstalledVersion", VALUE_NOT_SET);
        var fixedVersion = vulnerability.optString("FixedVersion", "still open");
        var severity = vulnerability.optString("Severity", "UNKNOWN");
        var description = vulnerability.optString("Description", "");
        return join(p(div(b("File: "), text(fileName)),
                div(b("Installed Version: "), text(installedVersion)),
                div(b("Fixed Version: "), text(fixedVersion)),
                div(b("Severity: "), text(severity)),
                p(text(description)))).render();
    }

    private String formatMisconfigurationDescription(final JSONObject misconfiguration) {
        DomContent description = p(misconfiguration.optString("Description", VALUE_NOT_SET));

        var message = misconfiguration.optString("Message", null);
        if (message != null) {
            description = join(description, p(message));
        }

        var resolution = misconfiguration.optString("Resolution", null);
        if (resolution != null) {
            description = join(description, p(resolution));
        }

        ContainerTag referencesList = null;
        var primaryUrl = misconfiguration.optString("PrimaryURL", null);
        if (primaryUrl != null) {
            referencesList = ul();
            referencesList.with(li(a(primaryUrl).withHref(primaryUrl)));
        }

        var references = misconfiguration.optJSONArray("References");
        if (references != null) {
            if (referencesList == null) {
                referencesList = ul();
            }
            for (int i = 0; i < references.length(); i++) {
                var reference = references.getString(i);
                referencesList.with(li(a(reference).withHref(reference)));
            }
        }

        if (referencesList != null) {
            description = join(description, p("References:"), referencesList);
        }

        return description.render();
    }
}
