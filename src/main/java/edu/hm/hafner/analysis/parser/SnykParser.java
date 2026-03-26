package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

/**
 * Parser for Snyk security vulnerability reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://snyk.io/">Snyk</a>
 * @see <a href="https://github.com/snyk">Snyk on GitHub</a>
 */
public class SnykParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -4522651029150852849L;

    private static final String VULNERABILITIES_TAG = "vulnerabilities";
    private static final String ID_TAG = "id";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String SEVERITY_TAG = "severity";
    private static final String PACKAGE_NAME_TAG = "packageName";
    private static final String PACKAGE_VERSION_TAG = "packageVersion";
    private static final String FILE_PATH_TAG = "filePath";
    private static final String IDENTIFIERS_TAG = "identifiers";
    private static final String CVE_TAG = "CVE";
    private static final String CWE_TAG = "CWE";
    private static final String UPGRADE_PATH_TAG = "upgradePath";
    private static final String IS_UPGRADABLE_TAG = "isUpgradable";
    private static final String CVSS_V3_TAG = "CVSSv3";
    private static final String LANGUAGE_TAG = "language";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(VULNERABILITIES_TAG)) {
            var vulnerabilities = jsonReport.getJSONArray(VULNERABILITIES_TAG);
            for (int i = 0; i < vulnerabilities.length(); i++) {
                var vulnerability = vulnerabilities.getJSONObject(i);
                var issue = createIssue(issueBuilder, vulnerability);
                report.add(issue);
            }
        }
    }

    private Issue createIssue(final IssueBuilder issueBuilder, final JSONObject vulnerability) {
        var packageName = formatPackageName(vulnerability);
        var message = vulnerability.optString(TITLE_TAG, "");
        var description = vulnerability.optString(DESCRIPTION_TAG, "");
        var descriptionHtml = buildDescription(message, description, vulnerability);

        return issueBuilder
                .setFileName(getFileName(vulnerability, packageName))
                .setPackageName(packageName)
                .setCategory(vulnerability.optString(LANGUAGE_TAG, "Unknown"))
                .guessSeverity(vulnerability.optString(SEVERITY_TAG, "medium"))
                .setType(vulnerability.optString(ID_TAG, "-"))
                .setMessage(message)
                .setDescription(descriptionHtml)
                .build();
    }

    private String formatPackageName(final JSONObject vulnerability) {
        var packageName = vulnerability.optString(PACKAGE_NAME_TAG, "Unknown");
        var packageVersion = vulnerability.optString(PACKAGE_VERSION_TAG, "");
        return packageVersion.isEmpty() ? packageName : packageName + "@" + packageVersion;
    }

    private String getFileName(final JSONObject vulnerability, final String packageName) {
        var fileName = vulnerability.optString(FILE_PATH_TAG, "");
        return fileName.isEmpty() ? packageName : fileName;
    }

    private String buildDescription(final String message, final String description, final JSONObject vulnerability) {
        var html = new StringBuilder();
        appendMessageSection(html, message);
        appendDescriptionSection(html, description);
        appendCveSection(html, vulnerability);
        appendCweSection(html, vulnerability);
        appendCvssSection(html, vulnerability);
        appendUpgradeSection(html, vulnerability);
        return html.toString();
    }

    private void appendMessageSection(final StringBuilder html, final String message) {
        if (!message.isEmpty()) {
            var escapedMessage = StringEscapeUtils.escapeHtml4(message);
            var messageHtml = "<p><strong>" + escapedMessage + "</strong></p>";
            html.append(messageHtml);
        }
    }

    private void appendDescriptionSection(final StringBuilder html, final String description) {
        if (!description.isEmpty()) {
            var escapedDescription = StringEscapeUtils.escapeHtml4(description);
            var descriptionHtml = "<p>" + escapedDescription + "</p>";
            html.append(descriptionHtml);
        }
    }

    private void appendCveSection(final StringBuilder html, final JSONObject vulnerability) {
        if (!hasIdentifierArray(vulnerability, CVE_TAG)) {
            return;
        }
        var cves = vulnerability.getJSONObject(IDENTIFIERS_TAG).getJSONArray(CVE_TAG);
        if (cves.length() == 0) {
            return;
        }
        html.append("<p><strong>CVE ID(s):</strong> ");
        for (int i = 0; i < cves.length(); i++) {
            if (i > 0) {
                html.append(", ");
            }
            appendCveLink(html, cves.getString(i));
        }
        html.append("</p>");
    }

    private void appendCveLink(final StringBuilder html, final String cve) {
        var trimmedCve = cve.trim();
        if (!trimmedCve.isEmpty() && trimmedCve.matches("CVE-\\d{4}-\\d+")) {
            var cveLink = "<a href=\"https://nvd.nist.gov/vuln/detail/" + trimmedCve + "\">" + trimmedCve + "</a>";
            html.append(cveLink);
        } 
        else {
            html.append(StringEscapeUtils.escapeHtml4(trimmedCve));
        }
    }

    private void appendCweSection(final StringBuilder html, final JSONObject vulnerability) {
        if (!hasIdentifierArray(vulnerability, CWE_TAG)) {
            return;
        }
        var cwes = vulnerability.getJSONObject(IDENTIFIERS_TAG).getJSONArray(CWE_TAG);
        if (cwes.length() == 0) {
            return;
        }
        html.append("<p><strong>CWE ID(s):</strong> ");
        for (int i = 0; i < cwes.length(); i++) {
            if (i > 0) {
                html.append(", ");
            }
            html.append(cwes.getString(i));
        }
        html.append("</p>");
    }

    private void appendCvssSection(final StringBuilder html, final JSONObject vulnerability) {
        if (!vulnerability.has(CVSS_V3_TAG)) {
            return;
        }
        var cvss = vulnerability.getString(CVSS_V3_TAG);
        var escapedCvss = StringEscapeUtils.escapeHtml4(cvss);
        var cvssHtml = "<p><strong>CVSS:</strong> " + escapedCvss + "</p>";
        html.append(cvssHtml);
    }

    private void appendUpgradeSection(final StringBuilder html, final JSONObject vulnerability) {
        if (!vulnerability.has(UPGRADE_PATH_TAG) || !vulnerability.optBoolean(IS_UPGRADABLE_TAG, false)) {
            return;
        }
        var upgradePath = vulnerability.getJSONArray(UPGRADE_PATH_TAG);
        if (upgradePath.length() == 0) {
            return;
        }
        html.append("<p><strong>Suggested Fix:</strong> ");
        for (int i = 0; i < upgradePath.length(); i++) {
            if (i > 0) {
                html.append(" → ");
            }
            html.append(StringEscapeUtils.escapeHtml4(upgradePath.getString(i)));
        }
        html.append("</p>");
    }

    private boolean hasIdentifierArray(final JSONObject vulnerability, final String tag) {
        return vulnerability.has(IDENTIFIERS_TAG)
                && vulnerability.getJSONObject(IDENTIFIERS_TAG).has(tag);
    }
}
