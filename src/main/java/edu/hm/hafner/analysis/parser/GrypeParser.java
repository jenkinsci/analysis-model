package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON report parser for grype (https://plugins.jenkins.io/grypescanner/ /
 * https://github.com/anchore/grype).
 */
public class GrypeParser extends JsonIssueParser {
    private static final long serialVersionUID = -1369431674771459756L;

    private static final String MATCHES_TAG = "matches";
    private static final String VULNERABILIY_TAG = "vulnerability";
    private static final String ARTIFACT_TAG = "artifact";
    private static final String LOCATIONS_TAG = "locations";
    private static final String PATH_TAG = "path";
    private static final String DATA_SOURCE_TAG = "dataSource";
    private static final String SEVERITY_TAG = "severity";
    private static final String ID_TAG = "id";
    private static final String DESCRIPTION_TAG = "description";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        final JSONArray matches = jsonReport.getJSONArray(MATCHES_TAG);
        for (int i = 0; i < matches.length(); i++) {
            final JSONObject match = matches.getJSONObject(i);
            if (!match.has(VULNERABILIY_TAG)) {
                continue;
            }
            JSONObject vuln = match.getJSONObject(VULNERABILIY_TAG);
            String fileName = match.getJSONObject(ARTIFACT_TAG).getJSONArray(LOCATIONS_TAG).getJSONObject(0)
                    .getString(PATH_TAG);
            Issue issue = issueBuilder.setFileName(fileName)
                    .setCategory(vuln.getString(SEVERITY_TAG))
                    .setSeverity(mapSeverity(vuln.getString(SEVERITY_TAG)))
                    .setType(vuln.getString(ID_TAG))
                    .setMessage(vuln.getString(DESCRIPTION_TAG))
                    .setOriginName("Grype")
                    .setPathName(fileName)
                    .setDescription(vuln.getString(DATA_SOURCE_TAG)).build();
            report.add(issue);
        }
    }

    private Severity mapSeverity(final String severity) {
        switch (severity.toUpperCase(Locale.ENGLISH)) {
            case "LOW":
                return Severity.WARNING_LOW;
            case "MEDIUM":
                return Severity.WARNING_NORMAL;
            case "CRITICAL":
                return Severity.ERROR;
            case "HIGH":
                return Severity.WARNING_HIGH;
            default:
                return new Severity(severity);
        }
    }
}
