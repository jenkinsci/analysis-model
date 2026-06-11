package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for OWASP Dependency-Track findings exported in the Finding Packaging Format (FPF).
 *
 * @author Akash Manna
 * @see <a href="https://dependencytrack.org/">OWASP Dependency-Track</a>
 * @see <a href="https://docs.dependencytrack.org/integrations/file-formats/">Finding Packaging Format</a>
 */
public class DependencyTrackParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -1440102275016217642L;

    private static final String FINDINGS_KEY = "findings";

    private static final String COMPONENT_KEY = "component";
    private static final String COMPONENT_NAME = "name";
    private static final String COMPONENT_VERSION = "version";
    private static final String COMPONENT_PURL = "purl";

    private static final String VULNERABILITY_KEY = "vulnerability";
    private static final String VULN_ID = "vulnId";
    private static final String VULN_SOURCE = "source";
    private static final String VULN_TITLE = "title";
    private static final String VULN_DESCRIPTION = "description";
    private static final String VULN_SEVERITY = "severity";
    private static final String VULN_CWES = "cwes";
    private static final String VULN_CWE_ID = "cweId";
    private static final String VULN_CWE_NAME = "name";
    private static final String VULN_RECOMMENDATION = "recommendation";
    private static final String VULN_ALIASES = "aliases";
    private static final String ALIAS_CVE_ID = "cveId";

    private static final String ANALYSIS_KEY = "analysis";
    private static final String ANALYSIS_IS_SUPPRESSED = "isSuppressed";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var findings = jsonReport.optJSONArray(FINDINGS_KEY);
        if (findings == null) {
            return;
        }
        for (int i = 0; i < findings.length(); i++) {
            var finding = findings.optJSONObject(i);
            if (finding != null && !isSuppressed(finding)) {
                report.add(convertToIssue(finding, issueBuilder));
            }
        }
    }

    private boolean isSuppressed(final JSONObject finding) {
        var analysis = finding.optJSONObject(ANALYSIS_KEY);
        if (analysis == null) {
            return false;
        }
        return analysis.optBoolean(ANALYSIS_IS_SUPPRESSED, false);
    }

    private edu.hm.hafner.analysis.Issue convertToIssue(final JSONObject finding, final IssueBuilder issueBuilder) {
        var component = finding.optJSONObject(COMPONENT_KEY);
        var vulnerability = finding.optJSONObject(VULNERABILITY_KEY);

        var packageName = buildPackageName(component);
        var fileName = resolveFileName(component, packageName);
        var type = buildType(vulnerability);
        var message = buildMessage(vulnerability);
        var category = buildCategory(vulnerability);
        var description = buildDescription(vulnerability, component);

        issueBuilder
                .setFileName(fileName)
                .setPackageName(packageName)
                .setType(type)
                .setMessage(message)
                .setCategory(category)
                .setDescription(description);

        applySeverity(vulnerability, issueBuilder);

        return issueBuilder.buildAndClean();
    }

    private String buildPackageName(@CheckForNull final JSONObject component) {
        if (component == null) {
            return "-";
        }
        var name = component.optString(COMPONENT_NAME, "");
        var version = component.optString(COMPONENT_VERSION, "");
        if (name.isBlank()) {
            return "-";
        }
        return version.isBlank() ? name : name + "@" + version;
    }

    private String resolveFileName(@CheckForNull final JSONObject component, final String packageName) {
        if (component == null) {
            return packageName;
        }
        var purl = component.optString(COMPONENT_PURL, "").trim();
        return purl.isBlank() ? packageName : purl;
    }

    private String buildType(@CheckForNull final JSONObject vulnerability) {
        if (vulnerability == null) {
            return "-";
        }
        var source = vulnerability.optString(VULN_SOURCE, "").trim();
        var vulnId = vulnerability.optString(VULN_ID, "").trim();
        if (vulnId.isBlank()) {
            return "-";
        }
        return source.isBlank() ? vulnId : source + ":" + vulnId;
    }

    private String buildMessage(@CheckForNull final JSONObject vulnerability) {
        if (vulnerability == null) {
            return "";
        }
        return vulnerability.optString(VULN_TITLE, "").trim();
    }

    private String buildCategory(@CheckForNull final JSONObject vulnerability) {
        if (vulnerability == null) {
            return "";
        }
        return vulnerability.optString(VULN_SOURCE, "").trim();
    }

    private void applySeverity(@CheckForNull final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        if (vulnerability == null) {
            issueBuilder.setSeverity(edu.hm.hafner.analysis.Severity.WARNING_NORMAL);
            return;
        }
        issueBuilder.guessSeverity(vulnerability.optString(VULN_SEVERITY, "MEDIUM"));
    }

    private String buildDescription(@CheckForNull final JSONObject vulnerability,
            @CheckForNull final JSONObject component) {
        if (vulnerability == null) {
            return "";
        }

        var sb = new StringBuilder();

        appendIfNotBlank(sb, "Vulnerability ID", buildType(vulnerability));
        appendIfNotBlank(sb, "Source", vulnerability.optString(VULN_SOURCE, ""));
        appendIfNotBlank(sb, "Description", vulnerability.optString(VULN_DESCRIPTION, ""));
        appendIfNotBlank(sb, "Recommendation", vulnerability.optString(VULN_RECOMMENDATION, ""));
        appendCwes(sb, vulnerability.optJSONArray(VULN_CWES));
        appendAliases(sb, vulnerability.optJSONArray(VULN_ALIASES));

        if (component != null) {
            appendIfNotBlank(sb, "Component", component.optString(COMPONENT_NAME, ""));
            appendIfNotBlank(sb, "Version", component.optString(COMPONENT_VERSION, ""));
            appendIfNotBlank(sb, "Package URL", component.optString(COMPONENT_PURL, ""));
        }

        return sb.toString().trim();
    }

    private void appendCwes(final StringBuilder sb, @CheckForNull final JSONArray cwes) {
        if (cwes == null || cwes.isEmpty()) {
            return;
        }
        var cweList = new StringBuilder();
        for (int i = 0; i < cwes.length(); i++) {
            var cweObj = cwes.optJSONObject(i);
            if (cweObj != null) {
                var cweId = cweObj.optInt(VULN_CWE_ID, -1);
                var cweName = cweObj.optString(VULN_CWE_NAME, "").trim();
                if (cweId > 0) {
                    if (!cweList.isEmpty()) {
                        cweList.append(", ");
                    }
                    cweList.append("CWE-").append(cweId);
                    if (!cweName.isBlank()) {
                        cweList.append(" (").append(cweName).append(")");
                    }
                }
            }
        }
        if (!cweList.isEmpty()) {
            appendIfNotBlank(sb, "CWE(s)", cweList.toString());
        }
    }

    private void appendAliases(final StringBuilder sb, @CheckForNull final JSONArray aliases) {
        if (aliases == null || aliases.isEmpty()) {
            return;
        }
        var cveList = new StringBuilder();
        for (int i = 0; i < aliases.length(); i++) {
            var alias = aliases.optJSONObject(i);
            if (alias != null) {
                var cveId = alias.optString(ALIAS_CVE_ID, "").trim();
                if (!cveId.isBlank()) {
                    if (!cveList.isEmpty()) {
                        cveList.append(", ");
                    }
                    cveList.append(cveId);
                }
            }
        }
        if (!cveList.isEmpty()) {
            appendIfNotBlank(sb, "CVE Alias(es)", cveList.toString());
        }
    }

    private void appendIfNotBlank(final StringBuilder sb, final String label, final String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        sb.append(label).append(": ").append(value.trim()).append("\n\n");
    }
}