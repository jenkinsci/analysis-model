package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import java.io.Serial;

/**
 * A parser for Kubesec JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://kubesec.io/">Kubesec</a>
 * @see <a href="https://github.com/controlplaneio/kubesec">Kubesec on GitHub</a>
 */
public class KubesecParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1754328680585270133L;

    private static final String NOT_AVAILABLE = "-";

    private static final String OBJECT = "object";
    private static final String VALID = "valid";
    private static final String MESSAGE = "message";
    private static final String SCORE = "score";
    private static final String SCORING = "scoring";
    private static final String CRITICAL = "critical";
    private static final String ADVISE = "advise";

    private static final String SELECTOR = "selector";
    private static final String REASON = "reason";
    private static final String POINTS = "points";
    private static final String KUBESEC_VALIDATION = "kubesec-validation";
    private static final String KUBERNETES_SECURITY = "Kubernetes Security";

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var kubesecResult = jsonReport.optJSONObject(i);
            if (kubesecResult != null) {
                parseKubesecResult(kubesecResult, report, issueBuilder);
            }
        }
    }

    private void parseKubesecResult(final JSONObject kubesecResult, final Report report, final IssueBuilder issueBuilder) {
        var object = kubesecResult.optString(OBJECT, NOT_AVAILABLE);
        var scoring = kubesecResult.optJSONObject(SCORING);
        var totalFindings = 0;

        if (scoring != null) {
            var critical = scoring.optJSONArray(CRITICAL);
            if (critical != null) {
                totalFindings += parseFindings(critical, object, Severity.ERROR, report, issueBuilder);
            }

            var advise = scoring.optJSONArray(ADVISE);
            if (advise != null) {
                totalFindings += parseFindings(advise, object, Severity.WARNING_NORMAL, report, issueBuilder);
            }

            // Note: passed checks are not added as issues since they represent passed security checks.
        }

        if (totalFindings == 0 && !kubesecResult.optBoolean(VALID, true)) {
            report.add(createInvalidResourceIssue(kubesecResult, object, issueBuilder));
        }
    }

    private int parseFindings(final JSONArray findings, final String object, final Severity severity,
                              final Report report, final IssueBuilder issueBuilder) {
        var numberOfFindings = 0;
        for (Object entry : findings) {
            if (entry instanceof JSONObject finding) {
                report.add(convertToIssue(finding, object, severity, issueBuilder));
                numberOfFindings++;
            }
        }
        return numberOfFindings;
    }

    private Issue convertToIssue(final JSONObject finding, final String object, final Severity severity,
                                final IssueBuilder issueBuilder) {
        return issueBuilder
                .setFileName(extractFileNameFromObject(object))
                .setType(finding.optString(SELECTOR, NOT_AVAILABLE))
                .setMessage(finding.optString(REASON, NOT_AVAILABLE))
                .setSeverity(severity)
                .setCategory(KUBERNETES_SECURITY)
                .setDescription(formatDescription(finding, object))
                .buildAndClean();
    }

    private Issue createInvalidResourceIssue(final JSONObject kubesecResult, final String object,
            final IssueBuilder issueBuilder) {
        var score = kubesecResult.optInt(SCORE, 0);
        var description = "Resource: " + object + "\nScore: " + score;

        return issueBuilder
                .setFileName(extractFileNameFromObject(object))
                .setType(KUBESEC_VALIDATION)
                .setMessage(kubesecResult.optString(MESSAGE, "Invalid Kubernetes resource"))
                .setSeverity(Severity.ERROR)
                .setCategory(KUBERNETES_SECURITY)
                .setDescription(description)
                .buildAndClean();
    }

    private String extractFileNameFromObject(@CheckForNull final String object) {
        if (object == null || object.equals(NOT_AVAILABLE)) {
            return NOT_AVAILABLE;
        }

        return object;
    }

    private String formatDescription(final JSONObject finding, final String object) {
        var sb = new StringBuilder();
        sb.append("Resource: ").append(object).append("\n");
        
        var selector = finding.optString(SELECTOR, NOT_AVAILABLE);
        if (!NOT_AVAILABLE.equals(selector)) {
            sb.append("Selector: ").append(selector).append("\n");
        }
        
        var points = finding.optInt(POINTS, 0);
        sb.append("Score Impact: ").append(points > 0 ? "+" : "").append(points).append(" points");
        
        return sb.toString();
    }
}
