package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import static j2html.TagCreator.*;

/**
 * JSON report parser for grype.
 *
 * @see  <a href="https://plugins.jenkins.io/grypescanner/">Jenkins Plugin GrypeScanner</a>
 * @see  <a href="https://github.com/anchore/grype">grype</a>
 */
public class GrypeParser extends JsonIssueParser {
    @Serial
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
    private static final String NAME_TAG = "name";
    private static final String VERSION_TAG = "version";
    private static final String TYPE_TAG = "type";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        final var matches = jsonReport.getJSONArray(MATCHES_TAG);
        for (int i = 0; i < matches.length(); i++) {
            final var match = matches.getJSONObject(i);
            if (match.has(VULNERABILIY_TAG)) {
                var issue = getIssue(issueBuilder, match);
                report.add(issue);
            }
        }
    }

    private Issue getIssue(final IssueBuilder issueBuilder, final JSONObject match) {
        var vulnerability = match.getJSONObject(VULNERABILIY_TAG);
        var artifact = match.getJSONObject(ARTIFACT_TAG);
        var fileName = artifact.getJSONArray(LOCATIONS_TAG).getJSONObject(0).getString(PATH_TAG);
        var packageName = artifact.optString(NAME_TAG, "Unknown");
        var version = artifact.optString(VERSION_TAG, "");
        if (!version.isEmpty()) {
            packageName = packageName + " " + version;
        }

        return issueBuilder.setFileName(fileName)
                .setPackageName(packageName)
                .setCategory(artifact.optString(TYPE_TAG, "Unknown"))
                .setSeverity(Severity.guessFromString(vulnerability.getString(SEVERITY_TAG)))
                .setType(vulnerability.getString(ID_TAG))
                .setMessage(vulnerability.optString(DESCRIPTION_TAG, "Unknown"))
                .setPathName(fileName)
                .setDescription(p().with(a()
                        .withHref(vulnerability.getString(DATA_SOURCE_TAG))
                        .withText(vulnerability.getString(DATA_SOURCE_TAG))).render())
                .build();
    }
}
