package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.Type;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * OWASP dependency check JSON report parser.
 */
public class OwaspDependencyCheckParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -1369431674771459756L;

    private static final String NDIST_NVD_DETAIL_URL_TEMPLATE = "https://nvd.nist.gov/vuln/detail/%1$s";
    private static final String LINK_TEMPLATE = "<a href=\"%1$s\">%1$s</a>".formatted(NDIST_NVD_DETAIL_URL_TEMPLATE);

    private static final String DEPENDENCIES = "dependencies";
    private static final String VULNERABILITIES = "vulnerabilities";
    private static final String SEVERITY = "severity";
    private static final String NAME = "name";
    private static final String CVSSV2 = "cvssv2";
    private static final String CVSSV3 = "cvssv3";
    private static final String ACCESS_VECTOR = "accessVector";
    private static final String ATTACK_VECTOR = "attackVector";
    private static final String DESCRIPTION = "description";

    @Override
    public Type getType() {
        return Type.VULNERABILITY;
    }

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        final var dependencies = jsonReport.getJSONArray(DEPENDENCIES);
        for (int i = 0; i < dependencies.length(); i++) {
            final var dependency = dependencies.getJSONObject(i);
            if (!dependency.has(VULNERABILITIES)) {
                continue;
            }
            issueBuilder.setFileName(dependency.getString("fileName"));
            final var vulnerabilities = dependency.getJSONArray(VULNERABILITIES);
            for (int j = 0; j < vulnerabilities.length(); j++) {
                final var vulnerability = vulnerabilities.getJSONObject(j);
                report.add(createIssueFromVulnerability(vulnerability, issueBuilder));
            }
        }
    }

    private Issue createIssueFromVulnerability(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        final var name = vulnerability.getString(NAME);
        return issueBuilder
                .setSeverity(mapSeverity(vulnerability.getString(SEVERITY)))
                .setCategory(determineCategory(vulnerability))
                .setType(name)
                .setMessage(vulnerability.getString(DESCRIPTION))
                .setDescription(LINK_TEMPLATE.formatted(name))
                .build();
    }

    @CheckForNull
    private String determineCategory(final JSONObject vulnerability) {
        final var cvssv3 = vulnerability.optJSONObject(CVSSV3);
        if (cvssv3 != null) {
            final var category = cvssv3.optString(ATTACK_VECTOR, null);
            if (category != null) {
                return category;
            }
        }
        final var cvssv2 = vulnerability.optJSONObject(CVSSV2);
        if (cvssv2 != null) {
            return cvssv2.optString(ACCESS_VECTOR, null);
        }
        return null;
    }

    private Severity mapSeverity(final String severity) {
        switch (severity) {
            case "LOW":
                return Severity.WARNING_LOW;
            case "MEDIUM":
                return Severity.WARNING_NORMAL;
            case "CRITICAL":
                return Severity.ERROR;
            case "HIGH":
            default:
                return Severity.WARNING_HIGH;
        }
    }
}
