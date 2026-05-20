package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import org.apache.commons.lang3.StringUtils;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Kube Hunter JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/aquasecurity/kube-hunter">Kube Hunter on GitHub</a>
 * @see <a href="https://aquasecurity.github.io/kube-hunter/">Kube Hunter Documentation</a>
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

    private void parseVulnerabilities(final Report report, @CheckForNull final JSONArray vulnerabilities,
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
        var fileName = StringUtils.defaultIfBlank(firstNonBlank(vulnerability, LOCATION), NOT_AVAILABLE);
        var category = StringUtils.defaultIfBlank(firstNonBlank(vulnerability, CATEGORY), NOT_AVAILABLE);
        var type = StringUtils.defaultIfBlank(firstNonBlank(vulnerability, VID), NOT_AVAILABLE);
        var message = StringUtils.defaultIfBlank(firstNonBlank(vulnerability, VULNERABILITY, DESCRIPTION), NOT_AVAILABLE);

        return issueBuilder
                .setFileName(fileName)
                .setCategory(category)
                .setType(type)
                .setMessage(message)
                .setDescription(firstNonBlank(vulnerability, DESCRIPTION))
                .guessSeverity(firstNonBlank(vulnerability, SEVERITY))
                .buildAndClean();
    }
}
