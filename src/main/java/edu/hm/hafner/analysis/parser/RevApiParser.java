package edu.hm.hafner.analysis.parser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;


import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.RevapiInfoExtension;
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
        builder.setSeverity(evaluateSeverity(jsonIssue.getJSONArray("classification")));
        builder.setDescription(getDescription(jsonIssue));
        addAttachments(jsonIssue.getJSONArray("attachments"), builder);
        builder.setAdditionalProperties(convertToGroup(jsonIssue));
        return builder.build();
    }


    private RevapiInfoExtension convertToGroup(final JSONObject jsonIssue) {
        RevapiInfoExtension group = new RevapiInfoExtension(jsonIssue.getString("name"));
        Object newChange = jsonIssue.get("new");
        Object oldChange = jsonIssue.get("old");
        Map<String, String> allSeverities = new HashMap<>();
        JSONArray severities = jsonIssue.getJSONArray("classification");
        for (Object severity : severities) {
            if (severity instanceof JSONObject) {
                allSeverities.put(((JSONObject) severity).getString("compatibility"), ((JSONObject) severity).getString("severity"));
            }
        }
        group.setSeverities(allSeverities);
        if (newChange instanceof JSONObject && !((JSONObject) newChange).isNull("new")) {
            group.setNewFile(StringUtils.defaultString(((JSONObject) newChange).getString("new")));
        }
        if (oldChange instanceof JSONObject && !((JSONObject) oldChange).isNull("old")) {
            group.setOldFile(StringUtils.defaultString(((JSONObject) oldChange).getString("old")));
        }
        return group;
    }

    private void addAttachments(final JSONArray attachments, final IssueBuilder builder) {
        String packageName = attachments.getJSONObject(0).getString("value");
        //String classQualifiedName = attachments.getJSONObject(1).getString("value");
        String classSimpleName = attachments.getJSONObject(2).getString("value");
        String elementKind = attachments.getJSONObject(3).getString("value");

        builder.setFileName(classSimpleName);
        builder.setPackageName(packageName);
        builder.setType(elementKind);
    }

    private Severity evaluateSeverity(final JSONArray classification) {
        ArrayList<Severity> allSeverities = new ArrayList<>();
        for  (Object severity : classification) {
            if (severity instanceof JSONObject) {
                allSeverities.add(toSeverity(((JSONObject) severity).getString("severity")));
            }
        }
        if (allSeverities.contains(Severity.WARNING_HIGH)) {
            return Severity.WARNING_HIGH;
        } else if (allSeverities.contains(Severity.WARNING_LOW)){
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL;
    }

    private Severity toSeverity(final String level) {
        switch (level) {
            case "NON_BREAKING":
                return Severity.WARNING_LOW;
            case "BREAKING":
                return Severity.WARNING_HIGH;
            case "POTENTIALLY_BREAKING":
            default:
                return Severity.WARNING_NORMAL;
        }
    }


    private String getDescription(final JSONObject jsonIssue) {
        StringBuilder severityDescription = new StringBuilder();
        for  (Object severity :  jsonIssue.getJSONArray("classification")) {
            if (severity instanceof JSONObject) {
                severityDescription.append("<p>Compatibility: ")
                        .append(((JSONObject) severity).getString("compatibility"))
                        .append(" Severity: ")
                        .append(((JSONObject) severity).getString("severity"))
                        .append("</p>");
            }
        }

        return MessageFormat.format(
                "<p><div><b>File</b>: {0}</div><div><b>Description:</b> {1} {2}</div><div><b>Change type:</b> {3}",
                jsonIssue.getJSONArray("attachments").getJSONObject(1).getString("value"),
                jsonIssue.getString("description"),
                jsonIssue.getString("name"),
                jsonIssue.getString("code")) + severityDescription;

    }
}
