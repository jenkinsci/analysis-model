package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static j2html.TagCreator.*;

/**
 * Parser for Yocto Scanner CLI (scannercli) tool.
 *
 * @author Juri Duval
 */
public class YoctoScannerParser extends JsonIssueParser {
    private static final String VALUE_NOT_SET = "-";
    private static final long serialVersionUID = 1L;
    private static final Double INVALID_SCORE = -1.0;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONArray packages = jsonReport.optJSONArray("package");
        if (packages != null) {
            parseResources(report, packages, issueBuilder);
        }
    }

    private void parseResources(final Report report, final JSONArray packages, final IssueBuilder issueBuilder) {
        for (int i = 0; i < packages.length(); i++) {
            final Object item = packages.get(i);
            if (item instanceof JSONObject) {
                final JSONObject resourceWrapper = (JSONObject) item;
                if (!resourceWrapper.isNull("issue")) {
                    parseVulnerabilities(report, issueBuilder, resourceWrapper);
                }
            }
        }
    }

    private void parseVulnerabilities(final Report report, final IssueBuilder issueBuilder,
            final JSONObject resourceWrapper) {
        final JSONArray vulnerabilities = resourceWrapper.getJSONArray("issue");
        for (Object vulnerability : vulnerabilities) {
            if (vulnerability instanceof JSONObject) {
                final JSONObject obj = (JSONObject) vulnerability;
                final String status = obj.getString("status");
                if (status.equals("Unpatched")) {
                    report.add(convertToIssue(resourceWrapper, obj, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject resource, final JSONObject vulnerability,
            final IssueBuilder issueBuilder) {
        final String packageName = resource.getString("name");
        return issueBuilder
                .setPackageName(packageName)
                .setSeverity(mapSeverity(vulnerability))
                .setMessage(vulnerability.optString("id", "UNKNOWN"))
                .setDescription(formatDescription(packageName, resource, vulnerability))
                .buildAndClean();
    }

    private Severity mapSeverity(final JSONObject vulnerability) {
        Double score_v2 = INVALID_SCORE;
        Double score_v3 = INVALID_SCORE;
        Double score = INVALID_SCORE;

        if (vulnerability.has("scorev2")) {
            score_v2 = vulnerability.getDouble("scorev2");
        }

        if (vulnerability.has("scorev3")) {
            score_v3 = vulnerability.getDouble("scorev3");
        }

        if (score_v3 == 0.0) {
            if (score_v2 != 0.0) {
                score = score_v2;
            }
        } else {
            score = score_v3;
        }

        if (score < 0) {
            return Severity.ERROR;
        }

        if (score == 0.0) {
            return Severity.WARNING_LOW;
        }

        if (score < 4.0) {
            return Severity.WARNING_LOW;
        }

        if (score >= 4.0 && score < 7.0) {
            return Severity.WARNING_NORMAL;
        }

        if (score >= 7 && score <= 10)
            return Severity.WARNING_HIGH;

        return Severity.ERROR;
    }

    private String formatDescription(final String packageName, final JSONObject resource, final JSONObject vulnerability) {
        final String version = resource.optString("version", VALUE_NOT_SET);
        final String layer = resource.optString("layer", "UNKOWN");
        final String vector = vulnerability.optString("vector", "UNKOWN");
        final String link = vulnerability.optString("link", "UNKOWN");
        final String description = vulnerability.optString("summary", "");

        return join(div(b("Package: "), text(packageName)),
                div(b("Version: "), text(version)),
                div(b("Link: "), text(link)),
                div(b("Yocto Layer: "), text(layer)),
                div(b("Vector: "), text(vector)),
                p(text(description))).render();
    }
}
