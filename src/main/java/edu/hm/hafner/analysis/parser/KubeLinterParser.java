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
 * A parser for KubeLinter JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://github.com/stackrox/kube-linter">KubeLinter on GitHub</a>
 */
public class KubeLinterParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1754328680585270132L;

    private static final String NOT_AVAILABLE = "-";

    private static final String REPORTS = "Reports";
    private static final String DIAGNOSTIC = "Diagnostic";
    private static final String CHECK = "Check";
    private static final String REMEDIATION = "Remediation";
    private static final String OBJECT = "Object";

    private static final String MESSAGE = "Message";

    private static final String METADATA = "Metadata";
    private static final String FILE_PATH = "FilePath";

    private static final String K8S_OBJECT = "K8sObject";
    private static final String NAMESPACE = "Namespace";
    private static final String NAME = "Name";
    private static final String GROUP_VERSION_KIND = "GroupVersionKind";
    private static final String KIND = "Kind";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var reports = jsonReport.optJSONArray(REPORTS);
        if (reports != null) {
            parseReports(report, reports, issueBuilder);
        }
    }

    private void parseReports(final Report report, final JSONArray reports, final IssueBuilder issueBuilder) {
        for (Object entry : reports) {
            if (entry instanceof JSONObject issueReport) {
                report.add(convertToIssue(issueReport, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject issueReport, final IssueBuilder issueBuilder) {
        issueBuilder.setSeverity(Severity.WARNING_NORMAL)
                .setType(issueReport.optString(CHECK, NOT_AVAILABLE));

        applyDiagnostic(issueReport.optJSONObject(DIAGNOSTIC), issueBuilder);

        var object = issueReport.optJSONObject(OBJECT);
        return issueBuilder.setFileName(findFilePath(object))
                .setCategory(findKind(object))
                .setDescription(issueReport.optString(REMEDIATION))
                .buildAndClean();
    }

    private void applyDiagnostic(@CheckForNull final JSONObject diagnostic, final IssueBuilder issueBuilder) {
        if (diagnostic == null) {
            issueBuilder.setMessage(NOT_AVAILABLE);
            return;
        }

        issueBuilder.setMessage(diagnostic.optString(MESSAGE, NOT_AVAILABLE));
    }

    private String findFilePath(@CheckForNull final JSONObject object) {
        if (object == null) {
            return NOT_AVAILABLE;
        }

        var metadata = object.optJSONObject(METADATA);
        if (metadata != null) {
            return metadata.optString(FILE_PATH, NOT_AVAILABLE);
        }
        return NOT_AVAILABLE;
    }

    private String findKind(@CheckForNull final JSONObject object) {
        if (object == null) {
            return NOT_AVAILABLE;
        }

        var k8sObject = object.optJSONObject(K8S_OBJECT);
        if (k8sObject == null) {
            return NOT_AVAILABLE;
        }

        var groupVersionKind = k8sObject.optJSONObject(GROUP_VERSION_KIND);
        var kind = groupVersionKind == null ? NOT_AVAILABLE : groupVersionKind.optString(KIND, NOT_AVAILABLE);

        var name = k8sObject.optString(NAME, NOT_AVAILABLE);
        var namespace = k8sObject.optString(NAMESPACE, "");
        if (!namespace.isBlank() && !NOT_AVAILABLE.equals(namespace)) {
            return "%s/%s (%s)".formatted(kind, name, namespace);
        }
        return "%s/%s".formatted(kind, name);
    }
}
