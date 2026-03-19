package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

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
            final var vulnerabilities = jsonReport.getJSONArray(VULNERABILITIES_TAG);
            for (int i = 0; i < vulnerabilities.length(); i++) {
                final var vulnerability = vulnerabilities.getJSONObject(i);
                var issue = createIssue(issueBuilder, vulnerability);
                report.add(issue);
            }
        }
    }

    private Issue createIssue(final IssueBuilder issueBuilder, final JSONObject vulnerability) {
        var packageName = vulnerability.optString(PACKAGE_NAME_TAG, "Unknown");
        var packageVersion = vulnerability.optString(PACKAGE_VERSION_TAG, "");
        if (!packageVersion.isEmpty()) {
            packageName = packageName + "@" + packageVersion;
        }

        var fileName = vulnerability.optString(FILE_PATH_TAG, "");
        if (fileName.isEmpty()) {
            fileName = packageName;
        }

        var message = vulnerability.optString(TITLE_TAG, "");
        var description = vulnerability.optString(DESCRIPTION_TAG, "");

        var severity = Severity.guessFromString(vulnerability.optString(SEVERITY_TAG, "medium"));
        var id = vulnerability.optString(ID_TAG, "-");

        // Build description with hyperlinks and CVE information
        var descriptionHtml = new StringBuilder();
        
        // Add title if not already in message
        if (!message.isEmpty()) {
            descriptionHtml.append("<p><strong>").append(escapeHtml(message)).append("</strong></p>");
        }
        
        // Add description
        if (!description.isEmpty()) {
            descriptionHtml.append("<p>").append(escapeHtml(description)).append("</p>");
        }

        // Add CVE references
        if (vulnerability.has(IDENTIFIERS_TAG)) {
            var identifiers = vulnerability.getJSONObject(IDENTIFIERS_TAG);
            if (identifiers.has(CVE_TAG)) {
                var cves = identifiers.getJSONArray(CVE_TAG);
                if (cves.length() > 0) {
                    descriptionHtml.append("<p><strong>CVE ID(s):</strong> ");
                    for (int i = 0; i < cves.length(); i++) {
                        if (i > 0) {
                            descriptionHtml.append(", ");
                        }
                        var cve = cves.getString(i);
                        descriptionHtml.append("<a href=\"https://nvd.nist.gov/vuln/detail/")
                                .append(cve).append("\">").append(cve).append("</a>");
                    }
                    descriptionHtml.append("</p>");
                }
            }
            if (identifiers.has(CWE_TAG)) {
                var cwes = identifiers.getJSONArray(CWE_TAG);
                if (cwes.length() > 0) {
                    descriptionHtml.append("<p><strong>CWE ID(s):</strong> ");
                    for (int i = 0; i < cwes.length(); i++) {
                        if (i > 0) {
                            descriptionHtml.append(", ");
                        }
                        var cwe = cwes.getString(i);
                        descriptionHtml.append(cwe);
                    }
                    descriptionHtml.append("</p>");
                }
            }
        }

        // Add CVSS information
        if (vulnerability.has(CVSS_V3_TAG)) {
            var cvss = vulnerability.getString(CVSS_V3_TAG);
            descriptionHtml.append("<p><strong>CVSS:</strong> ").append(escapeHtml(cvss)).append("</p>");
        }

        // Add upgrade path if available
        if (vulnerability.has(UPGRADE_PATH_TAG) && vulnerability.optBoolean(IS_UPGRADABLE_TAG, false)) {
            var upgradePath = vulnerability.getJSONArray(UPGRADE_PATH_TAG);
            if (upgradePath.length() > 0) {
                descriptionHtml.append("<p><strong>Suggested Fix:</strong> ");
                for (int i = 0; i < upgradePath.length(); i++) {
                    if (i > 0) {
                        descriptionHtml.append(" → ");
                    }
                    descriptionHtml.append(escapeHtml(upgradePath.getString(i)));
                }
                descriptionHtml.append("</p>");
            }
        }

        return issueBuilder
                .setFileName(fileName)
                .setPackageName(packageName)
                .setCategory(vulnerability.optString(LANGUAGE_TAG, "Unknown"))
                .setSeverity(severity)
                .setType(id)
                .setMessage(message)
                .setDescription(descriptionHtml.toString())
                .build();
    }

    /**
     * Escapes HTML special characters in a string.
     *
     * @param text the text to escape
     * @return the escaped text
     */
    private String escapeHtml(final String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
