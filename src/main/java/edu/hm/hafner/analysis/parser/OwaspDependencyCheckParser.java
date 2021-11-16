package edu.hm.hafner.analysis.parser;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * OWASP dependency check JSON report parser.
 */
public class OwaspDependencyCheckParser extends JsonIssueParser {
    private static final long serialVersionUID = -1369431674771459756L;

    private static final String SUPPORTED_SCHEMA_VERSION = "1.1";

    private static final String SCHEMA_VERSION = "reportSchema";
    private static final String DEPENDENCIES = "dependencies";
    private static final String VULNERABILITIES = "vulnerabilities";
    private static final String SEVERITY = "severity";
    private static final String NAME = "name";
    private static final String CVSSV2 = "cvssv2";
    private static final String CVSSV3 = "cvssv3";
    private static final String ACCESS_VECTOR = "accessVector";
    private static final String ATTACK_VECTOR = "attackVector";
    private static final String DESCRIPTION = "description";

    private final Map<String, Severity> severityMap = new HashMap<>();

    /**
     * Create instance.
     */
    public OwaspDependencyCheckParser() {
        super();
        severityMap.put("LOW", Severity.WARNING_LOW);
        severityMap.put("MEDIUM", Severity.WARNING_NORMAL);
        severityMap.put("HIGH", Severity.WARNING_HIGH);
        severityMap.put("CRITICAL", Severity.ERROR);
    }

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        final String schemaVersion = jsonReport.getString(SCHEMA_VERSION);
        if (!SUPPORTED_SCHEMA_VERSION.equals(schemaVersion)) {
            throw new ParsingException("unsupported report schema version: %s", schemaVersion);
        }
        if (jsonReport.has(DEPENDENCIES)) {
            extractIssues(report, jsonReport.getJSONArray(DEPENDENCIES), issueBuilder);
        }
    }

    private void extractIssues(final Report report, final JSONArray dependencies, final IssueBuilder issueBuilder) {
        for (int i = 0; i < dependencies.length(); i++) {
            final JSONObject dependency = dependencies.getJSONObject(i);
            if (!dependency.has(VULNERABILITIES)) {
                continue;
            }
            issueBuilder.setPackageName(dependency.getString("fileName"));
            final JSONArray vulnerabilities = dependency.getJSONArray(VULNERABILITIES);
            for (int j = 0; j < vulnerabilities.length(); j++) {
                final JSONObject vulnerability = vulnerabilities.getJSONObject(j);
                report.add(createIssueFromVulnerability(vulnerability, issueBuilder));
            }
        }
    }

    private Issue createIssueFromVulnerability(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setSeverity(severityMap.getOrDefault(vulnerability.getString(SEVERITY), Severity.WARNING_HIGH))
                .setCategory(determineCategory(vulnerability))
                .setMessage(vulnerability.getString(NAME))
                .setDescription(vulnerability.getString(DESCRIPTION))
                .build();
    }

    @CheckForNull
    private String determineCategory(final JSONObject vulnerability) {
        final JSONObject cvssv3 = vulnerability.optJSONObject(CVSSV3);
        if (cvssv3 != null) {
            final String category = cvssv3.optString(ATTACK_VECTOR, null);
            if (category != null) {
                return category;
            }
        }
        final JSONObject cvssv2 = vulnerability.optJSONObject(CVSSV2);
        if (cvssv2 != null) {
            return cvssv2.optString(ACCESS_VECTOR, null);
        }
        return null;
    }
}
