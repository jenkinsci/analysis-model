package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;

import j2html.tags.DomContent;
import j2html.tags.Text;
import j2html.tags.UnescapedText;

import static j2html.TagCreator.*;

/**
 * Parser for FOSSA issue reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://fossa.com/">FOSSA</a>
 * @see <a href="https://docs.fossa.com/">FOSSA documentation</a>
 */
public class FossaParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -6351187128713072623L;

    private static final String ISSUES_TAG = "issues";
    private static final String PRIORITY_STRING_TAG = "priorityString";
    private static final String RESOLVED_TAG = "resolved";
    private static final String REVISION_ID_TAG = "revisionId";
    private static final String RULE_TAG = "rule";
    private static final String RULE_ID_TAG = "ruleId";
    private static final String ISSUE_DASH_URL_TAG = "issueDashURL";
    private static final String CVE_TAG = "cve";
    private static final String FIXED_IN_TAG = "fixedIn";
    private static final String TYPE_TAG = "type";

    private static final Text NBSP = text("\u00a0");

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var issues = jsonReport.optJSONArray(ISSUES_TAG);
        if (issues == null) {
            return;
        }

        for (int i = 0; i < issues.length(); i++) {
            var issue = issues.optJSONObject(i);
            if (issue != null) {
                report.add(createIssue(issue, issueBuilder));
            }
        }
    }

    private edu.hm.hafner.analysis.Issue createIssue(final JSONObject issue, final IssueBuilder issueBuilder) {
        var issueType = issue.optString(TYPE_TAG, "-");
        var priority = issue.optString(PRIORITY_STRING_TAG, "");
        var revisionId = issue.optString(REVISION_ID_TAG, "-");

        issueBuilder.setPackageName(revisionId)
                .setType(issueType)
                .setCategory(mapCategory(issueType))
                .setMessage(mapMessage(issueType))
                .setDescription(buildDescription(issue))
                .setSeverity(priority.isBlank() ? Severity.WARNING_NORMAL : Severity.guessFromString(priority));

        return issueBuilder.buildAndClean();
    }

    private String buildDescription(final JSONObject issue) {
        var sections = new ArrayList<DomContent>();

        appendTextSection(sections, "Priority", issue.optString(PRIORITY_STRING_TAG, ""));
        appendTextSection(sections, "Resolved", Boolean.toString(issue.optBoolean(RESOLVED_TAG, false)));
        appendTextSection(sections, "Revision ID", issue.optString(REVISION_ID_TAG, ""));

        var rule = issue.optJSONObject(RULE_TAG);
        if (rule != null) {
            appendTextSection(sections, "Rule ID", rule.optString(RULE_ID_TAG, ""));
        }

        var cve = issue.optString(CVE_TAG, "");
        if (!cve.isBlank()) {
            sections.add(p(strong("CVE:"), NBSP, createCveLink(cve)));
        }

        appendTextSection(sections, "Fixed In", issue.optString(FIXED_IN_TAG, ""));

        var issueUrl = issue.optString(ISSUE_DASH_URL_TAG, "");
        if (!issueUrl.isBlank()) {
            sections.add(p(strong("Issue URL:"), NBSP, a().withHref(issueUrl).withText(issueUrl)));
        }

        return sections.isEmpty() ? "" : join((Object[]) sections.toArray(new DomContent[0])).render();
    }

    private void appendTextSection(final List<DomContent> sections, final String label, final String value) {
        if (!value.isBlank()) {
            sections.add(p(strong(label + ":"), NBSP, new Text(value)));
        }
    }

    private DomContent createCveLink(final String cve) {
        var trimmed = cve.trim();
        if (trimmed.matches("CVE-\\d{4}-\\d+")) {
            return a().withHref("https://nvd.nist.gov/vuln/detail/" + trimmed).withText(trimmed);
        }
        return new UnescapedText(trimmed);
    }

    private String mapCategory(final String issueType) {
        return switch (issueType) {
            case "policy_conflict", "policy_flag", "unlicensed_dependency", "unlicensed_and_public" -> "Compliance";
            case "vulnerability", "risk_abandonware", "risk_empty_package", "risk_native_code",
                    "blacklisted_dependency", "outdated_dependency" -> "Security";
            default -> "Other";
        };
    }

    private String mapMessage(final String issueType) {
        return switch (issueType) {
            case "policy_conflict" -> "Denied by Policy";
            case "policy_flag" -> "Flagged by Policy";
            case "vulnerability" -> "Vulnerability";
            case "unlicensed_dependency" -> "Unlicensed Dependency";
            case "unlicensed_and_public" -> "Unlicensed and Public Dependency";
            case "outdated_dependency" -> "Outdated Dependency";
            case "risk_abandonware" -> "Abandoned Dependencies";
            case "risk_empty_package" -> "Empty Package";
            case "risk_native_code" -> "Native Code Dependency";
            case "blacklisted_dependency" -> "Denylisted Dependency";
            default -> issueType;
        };
    }
}