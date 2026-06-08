package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import java.io.Serial;

/**
 * Parser for detect-secrets JSON baseline reports.
 *
 * <p>detect-secrets produces a {@code .secrets.baseline} JSON file where
 * the {@code results} field is an object whose keys are file paths and whose
 * values are arrays of detected secret entries.</p>
 *
 * <p>Example invocation to produce the baseline file:</p>
 * <pre>detect-secrets scan &gt; .secrets.baseline</pre>
 *
 * @author Akash Manna
 * @see <a href="https://github.com/Yelp/detect-secrets">detect-secrets on GitHub</a>
 */
public class DetectSecretsParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 3816235924573216195L;

    private static final String RESULTS = "results";
    private static final String TYPE = "type";
    private static final String LINE_NUMBER = "line_number";
    private static final String IS_VERIFIED = "is_verified";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (!jsonReport.has(RESULTS)) {
            return;
        }

        var results = jsonReport.getJSONObject(RESULTS);
        for (var filePath : results.keySet()) {
            var secrets = results.getJSONArray(filePath);
            parseSecretsForFile(report, filePath, secrets, issueBuilder);
        }
    }

    private void parseSecretsForFile(final Report report, final String filePath,
            final JSONArray secrets, final IssueBuilder issueBuilder) {
        for (int i = 0; i < secrets.length(); i++) {
            var secret = secrets.optJSONObject(i);
            if (secret != null) {
                report.add(convertToIssue(filePath, secret, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final String filePath, final JSONObject secret,
            final IssueBuilder issueBuilder) {
        var type = secret.optString(TYPE, "");
        var lineNumber = secret.optInt(LINE_NUMBER, 0);
        var isVerified = secret.optBoolean(IS_VERIFIED, false);
        var severity = isVerified ? Severity.ERROR : Severity.WARNING_NORMAL;

        return issueBuilder
                .setFileName(filePath)
                .setType(StringUtils.defaultIfBlank(type, "-"))
                .setMessage(buildMessage(type))
                .setLineStart(lineNumber)
                .setSeverity(severity)
                .buildAndClean();
    }

    private String buildMessage(final String type) {
        if (StringUtils.isBlank(type)) {
            return "Secret detected";
        }
        return type + " detected";
    }
}
