package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static j2html.TagCreator.*;

/**
 * <p>
 * Parser for reports of pnpm audit scans.
 * </p>
 * <p>
 * <strong>Usage: </strong>pnpm audit --json > pnpm-audit.json
 * </p>
 *
 * @author Fabian Kaupp - kauppfbi@gmail.com
 */
public class PnpmAuditParser extends JsonIssueParser {

    private static final String VALUE_NOT_SET = "-";

    private static final String PNPM_VULNERABILITY_SEVERITY_INFO = "info";
    private static final String PNPM_VULNERABILITY_SEVERITY_LOW = "low";
    private static final String PNPM_VULNERABILITY_SEVERITY_MODERATE = "moderate";
    private static final String PNPM_VULNERABILITY_SEVERITY_HIGH = "high";
    private static final String PNPM_VULNERABILITY_SEVERITY_CRITICAL = "critical";

    private static final long serialVersionUID = 4140706319863200922L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        JSONObject results = jsonReport.optJSONObject("advisories");
        if (results != null) {
            parseResults(report, results, issueBuilder);
        }
    }

    private void parseResults(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        for (String key : jsonReport.keySet()) {
            JSONObject vulnerability = (JSONObject) jsonReport.get(key);

            if (!vulnerability.isEmpty()) {
                report.add(convertToIssue(vulnerability, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        return issueBuilder.setModuleName(vulnerability.optString("module_name", VALUE_NOT_SET))
                .setCategory(formatCategory(vulnerability))
                .setSeverity(mapSeverity(vulnerability.optString("severity", "UNKNOWN")))
                .setType(mapType(vulnerability))
                .setMessage(vulnerability.optString("overview", "UNKNOWN"))
                .setDescription(formatDescription(vulnerability))
                .buildAndClean();
    }

    private String mapType(final JSONObject vulnerability) {
       final String cve = (String) vulnerability.optJSONArray("cves").opt(0);

       return cve != null ? cve : VALUE_NOT_SET;
    }

    private String formatCategory(final JSONObject vulnerability) {
        String moduleName = vulnerability.optString("module_name");
        String title = vulnerability.optString("title");

        if (moduleName != null && title != null) {
            return title.replace(moduleName, "").replace(" in ", "");
        }

        return VALUE_NOT_SET;
    }

    private Severity mapSeverity(final String string) {
        if (PNPM_VULNERABILITY_SEVERITY_INFO.equalsIgnoreCase(string)) {
            return Severity.WARNING_LOW;
        }
        else if (PNPM_VULNERABILITY_SEVERITY_LOW.equalsIgnoreCase(string)) {
            return Severity.WARNING_LOW;
        }
        else if (PNPM_VULNERABILITY_SEVERITY_MODERATE.equalsIgnoreCase(string)) {
            return Severity.WARNING_NORMAL;
        }
        else if (PNPM_VULNERABILITY_SEVERITY_HIGH.equalsIgnoreCase(string)) {
            return Severity.WARNING_HIGH;
        }
        else if (PNPM_VULNERABILITY_SEVERITY_CRITICAL.equalsIgnoreCase(string)) {
            return Severity.ERROR;
        }
        else {
            return Severity.ERROR;
        }
    }

    private String formatDescription(final JSONObject vulnerability) {
        final String moduleName = vulnerability.optString("module_name", VALUE_NOT_SET);
        final JSONArray findings = vulnerability.optJSONArray("findings");
        final JSONObject firstFinding = (JSONObject) findings.opt(0);
        final String installedVersion = firstFinding.optString("version");
        final String vulnerableVersions = vulnerability.optString("vulnerable_versions", VALUE_NOT_SET);
        final String patchedVersions = vulnerability.optString("patched_versions", VALUE_NOT_SET);
        final String severity = vulnerability.optString("severity", VALUE_NOT_SET);
        final String overview = vulnerability.optString("overview", VALUE_NOT_SET);
        final String references = vulnerability.optString("references", VALUE_NOT_SET);

        return join(p(
                div(b("Module: "), text(moduleName)),
                div(b("Installed Version: "), text(installedVersion)),
                div(b("Vulnerable Versions: "), text(vulnerableVersions)),
                div(b("Patched Versions: "), text(patchedVersions)),
                div(b("Severity: "), text(severity)),
                p(text(overview)),
                div(b("References: "), text(references)))
        ).render();
    }
}