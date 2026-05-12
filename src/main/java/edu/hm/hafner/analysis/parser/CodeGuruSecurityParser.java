package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * A parser for AWS CodeGuru Security JSON output.
 *
 * @author Akash Manna
 * @see <a href="https://docs.aws.amazon.com/cli/latest/reference/codeguru-security/get-findings.html">AWS CodeGuru
 * Security get-findings</a>
 */
public class CodeGuruSecurityParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1187547319271125446L;

    private static final String FINDINGS = "findings";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String SEVERITY = "severity";
    private static final String TYPE = "type";
    private static final String RULE_ID = "ruleId";
    private static final String DETECTOR_ID = "detectorId";
    private static final String DETECTOR_NAME = "detectorName";
    private static final String VULNERABILITY = "vulnerability";
    private static final String FILE_PATH = "filePath";
    private static final String PATH = "path";
    private static final String NAME = "name";
    private static final String START_LINE = "startLine";
    private static final String END_LINE = "endLine";
    private static final String RECOMMENDATION = "recommendation";
    private static final String TEXT = "text";
    private static final String URL = "url";
    private static final String REFERENCE_URLS = "referenceUrls";
    private static final String SUGGESTED_FIXES = "suggestedFixes";
    private static final String FIX_TITLE = "title";
    private static final String FIX_DESCRIPTION = "description";
    private static final String CODE_SNIPPET = "codeSnippet";
    private static final String CONTENT = "content";
    private static final String NUMBER = "number";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var findings = jsonReport.optJSONArray(FINDINGS);
        if (findings == null) {
            return;
        }

        for (int i = 0; i < findings.length(); i++) {
            var finding = findings.optJSONObject(i);
            if (finding != null) {
                report.add(convertToIssue(finding, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject finding, final IssueBuilder issueBuilder) {
        var vulnerability = finding.optJSONObject(VULNERABILITY);
        var filePath = vulnerability == null ? null : vulnerability.optJSONObject(FILE_PATH);

        issueBuilder
                .setFileName(firstNonBlank(filePath, PATH, NAME))
                .setType(firstNonBlank(finding, RULE_ID, DETECTOR_ID, DETECTOR_NAME, TYPE))
                .setMessage(finding.optString(TITLE, ""))
                .guessSeverity(finding.optString(SEVERITY, "Info"))
                .setDescription(buildDescription(finding, vulnerability));

        if (filePath != null) {
            issueBuilder.setLineStart(filePath.optInt(START_LINE, 0))
                    .setLineEnd(filePath.optInt(END_LINE, 0));
        }

        return issueBuilder.buildAndClean();
    }

    private String buildDescription(final JSONObject finding, final JSONObject vulnerability) {
        var sections = new ArrayList<String>();

        appendIfNotBlank(sections, finding.optString(DESCRIPTION, ""));

        var recommendation = finding.optJSONObject(RECOMMENDATION);
        if (recommendation != null) {
            appendIfNotBlank(sections, recommendation.optString(TEXT, ""));
            appendIfNotBlank(sections, recommendation.optString(URL, ""));
        }

        appendReferenceUrls(sections, vulnerability);
        appendSuggestedFixes(sections, finding.optJSONArray(SUGGESTED_FIXES));
        appendCodeSnippets(sections, vulnerability == null ? null : vulnerability.optJSONArray(CODE_SNIPPET));

        return String.join("\n\n", sections);
    }

    private void appendReferenceUrls(final List<String> sections, final JSONObject vulnerability) {
        if (vulnerability == null) {
            return;
        }

        var referenceUrls = vulnerability.optJSONArray(REFERENCE_URLS);
        if (referenceUrls == null || referenceUrls.isEmpty()) {
            return;
        }

        var urls = new ArrayList<String>();
        for (int i = 0; i < referenceUrls.length(); i++) {
            var url = referenceUrls.optString(i, "").trim();
            if (!url.isBlank()) {
                urls.add(url);
            }
        }

        if (!urls.isEmpty()) {
            sections.add("References: " + String.join(", ", urls));
        }
    }

    private void appendSuggestedFixes(final List<String> sections, final JSONArray suggestedFixes) {
        if (suggestedFixes == null || suggestedFixes.isEmpty()) {
            return;
        }

        var fixes = new ArrayList<String>();
        for (int i = 0; i < suggestedFixes.length(); i++) {
            var suggestedFix = suggestedFixes.optJSONObject(i);
            if (suggestedFix == null) {
                continue;
            }

            var title = suggestedFix.optString(FIX_TITLE, "").trim();
            var description = suggestedFix.optString(FIX_DESCRIPTION, "").trim();
            var fix = new StringBuilder();
            if (!title.isBlank()) {
                fix.append(title);
            }
            if (!description.isBlank()) {
                if (fix.length() > 0) {
                    fix.append(": ");
                }
                fix.append(description);
            }

            if (fix.length() > 0) {
                fixes.add(fix.toString());
            }
        }

        if (!fixes.isEmpty()) {
            sections.add("Suggested fixes: " + String.join(" | ", fixes));
        }
    }

    private void appendCodeSnippets(final List<String> sections, final JSONArray codeSnippet) {
        if (codeSnippet == null || codeSnippet.isEmpty()) {
            return;
        }

        var lines = new ArrayList<String>();
        for (int i = 0; i < codeSnippet.length(); i++) {
            var codeLine = codeSnippet.optJSONObject(i);
            if (codeLine == null) {
                continue;
            }

            var number = codeLine.optInt(NUMBER, 0);
            var content = codeLine.optString(CONTENT, "").trim();
            if (!content.isBlank()) {
                lines.add(number > 0 ? number + ": " + content : content);
            }
        }

        if (!lines.isEmpty()) {
            sections.add("Code snippet:\n" + String.join("\n", lines));
        }
    }

    private void appendIfNotBlank(final List<String> sections, final String value) {
        var trimmed = value.trim();
        if (!trimmed.isBlank()) {
            sections.add(trimmed);
        }
    }
}