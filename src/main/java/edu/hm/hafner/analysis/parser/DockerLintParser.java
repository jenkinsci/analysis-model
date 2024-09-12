package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 * A parser for dockerlint json output.
 *
 * <p>
 * Possible usage via docker is:
 * </p>
 * {@code
 * <pre>
 *     docker run -it --rm -v $PWD:/root/ \
 *              projectatomic/dockerfile-lint \
 *              dockerfile_lint -j -f Dockerfile.
 * </pre>}
 *
 * @author Andreas Mandel
 * @see <a href="https://github.com/projectatomic/dockerfile_lint">dockerlint</a>
 */
public class DockerLintParser extends JsonIssueParser {
    private static final long serialVersionUID = -4077698163775928314L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        for (String severityGroupName : jsonReport.keySet()) {
            Object severityGroup = jsonReport.get(severityGroupName);
            if (severityGroup instanceof JSONObject) {
                JSONArray data = ((JSONObject) severityGroup).optJSONArray("data");
                for (Object issue : data) {
                    if (issue instanceof JSONObject) {
                        report.add(convertToIssue((JSONObject) issue, issueBuilder));
                    }
                }
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        var message = new StringBuilder();
        message.append(jsonIssue.optString("message"));
        if (jsonIssue.has("description")) {
            message.append(" - ");
            message.append(jsonIssue.getString("description"));
        }
        if (jsonIssue.has("reference_url")) {
            Object refUrl = jsonIssue.get("reference_url");
            message.append(" See ");
            message.append(collapseReferenceUrl(refUrl));
        }
        builder.setMessage(message.toString());

        builder.setSeverity(toSeverity(jsonIssue.optString("level")));
        builder.setLineStart(jsonIssue.optInt("line", -1));
        builder.setCategory(jsonIssue.optString("label", null));
        builder.setFileName("Dockerfile"); // simpler than reading "lineContent" & "instruction" & "count" & "regex"
        return builder.buildAndClean();
    }

    private String collapseReferenceUrl(final Object refUrl) {
        var referenceUrl = new StringBuilder();
        if (refUrl instanceof JSONArray) {
            for (Object part : (JSONArray) refUrl) {
                referenceUrl.append(part);
            }
        }
        else {
            referenceUrl.append(refUrl);
        }
        return referenceUrl.toString();
    }

    private Severity toSeverity(final String level) {
        switch (level) {
            case "warn":
                return Severity.WARNING_NORMAL;
            case "error":
                return Severity.WARNING_HIGH;
            default:
                return Severity.WARNING_LOW;
        }
    }
}
