package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

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
    @Serial
    private static final long serialVersionUID = -4077698163775928314L;

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        for (String severityGroupName : jsonReport.keySet()) {
            var severityGroup = jsonReport.get(severityGroupName);
            if (severityGroup instanceof final JSONObject object) {
                var data = object.optJSONArray("data");
                for (Object issue : data) {
                    if (issue instanceof final JSONObject jsonObject) {
                        report.add(convertToIssue(jsonObject, issueBuilder));
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
            var refUrl = jsonIssue.get("reference_url");
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
        if (refUrl instanceof final JSONArray array) {
            for (Object part : array) {
                referenceUrl.append(part);
            }
        }
        else {
            referenceUrl.append(refUrl);
        }
        return referenceUrl.toString();
    }

    private Severity toSeverity(final String level) {
        return switch (level) {
            case "warn" -> Severity.WARNING_NORMAL;
            case "error" -> Severity.WARNING_HIGH;
            default -> Severity.WARNING_LOW;
        };
    }
}
