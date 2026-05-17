package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * A parser for Kube Hunter JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/aquasecurity/kube-hunter">Kube Hunter on GitHub</a>
 */
public class KubeHunterParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -3725241371563364430L;

    private static final String NOT_AVAILABLE = "-";

    private static final String VULNERABILITIES = "vulnerabilities";
    private static final String LOCATION = "location";
    private static final String CATEGORY = "category";
    private static final String VID = "vid";
    private static final String VULNERABILITY = "vulnerability";
    private static final String DESCRIPTION = "description";
    private static final String SEVERITY = "severity";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        parseVulnerabilities(report, jsonReport.optJSONArray(VULNERABILITIES), issueBuilder);
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        parseVulnerabilities(report, jsonReport, issueBuilder);
    }

    private void parseVulnerabilities(final Report report, final JSONArray vulnerabilities,
            final IssueBuilder issueBuilder) {
        if (vulnerabilities == null) {
            return;
        }

        for (Object entry : vulnerabilities) {
            if (entry instanceof JSONObject vulnerability) {
                report.add(convertToIssue(vulnerability, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        var fileName = fallback(firstNonBlank(vulnerability, LOCATION), NOT_AVAILABLE);
        var category = fallback(firstNonBlank(vulnerability, CATEGORY), NOT_AVAILABLE);
        var type = fallback(firstNonBlank(vulnerability, VID), NOT_AVAILABLE);
        var message = fallback(firstNonBlank(vulnerability, VULNERABILITY, DESCRIPTION), NOT_AVAILABLE);
        var severity = mapSeverity(firstNonBlank(vulnerability, SEVERITY));

        return issueBuilder
                .setFileName(fileName)
                .setCategory(category)
                .setType(type)
                .setMessage(message)
                .setDescription(firstNonBlank(vulnerability, DESCRIPTION))
                .setSeverity(severity)
                .buildAndClean();
    }

    private String fallback(final String value, final String defaultValue) {
        if (value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    private Severity mapSeverity(final String severity) {
        return switch (severity.toLowerCase()) {
            case "critical" -> Severity.ERROR;
            case "high" -> Severity.WARNING_HIGH;
            case "low" -> Severity.WARNING_LOW;
            case "medium" -> Severity.WARNING_NORMAL;
            default -> Severity.WARNING_NORMAL;
        };
    }
}
