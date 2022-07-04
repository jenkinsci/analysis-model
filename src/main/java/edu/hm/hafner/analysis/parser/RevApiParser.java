package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;


import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

/**
 *  Parser for Tool (Revapi) Reports
 */
public class RevApiParser extends JsonIssueParser {

    private static final long serialVersionUID = -2452699725595063377L;

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject) {
                report.add(convertToIssue((JSONObject) issue, issueBuilder));
            }
        }
    }


    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        builder.setCategory(jsonIssue.getString("code"));
        builder.setSeverity(evaluteSeverity(jsonIssue.getJSONArray("classification")));
        builder.setDescription(getDescription(jsonIssue));
        addAttachments(jsonIssue.getJSONArray("attachments"), builder);
        return builder.build();
    }

    private void addAttachments(final JSONArray attachments, final IssueBuilder builder) {
        String packageName = attachments.getString(0);
        String classQualifiedName = attachments.getString(1);
        String classSimpleName = attachments.getString(2);
        String elementKind = attachments.getString(3);

        builder.setFileName(classSimpleName);
        builder.setPackageName(packageName);
        builder.setType(elementKind);
    }

    private Severity evaluteSeverity(final JSONArray classification){
        String a = classification.getString(0);
        String b = classification.getString(1);
        return toSeverity(b);
    }

    private String getDescription(final JSONObject jsonIssue) {
        return jsonIssue.getString("description");
    }


    private Severity toSeverity(final String level) {
        switch (level) {
            case "NON_BREAKING":
                return Severity.WARNING_LOW;
            case "BREAKING":
                return Severity.WARNING_HIGH;
            default:
                return Severity.WARNING_NORMAL;
        }
    }
}
