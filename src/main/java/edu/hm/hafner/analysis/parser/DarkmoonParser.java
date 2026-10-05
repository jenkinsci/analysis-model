package edu.hm.hafner.analysis.parser;

import static j2html.TagCreator.a;
import static j2html.TagCreator.code;
import static j2html.TagCreator.join;
import static j2html.TagCreator.p;
import static j2html.TagCreator.strong;
import static j2html.TagCreator.text;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import j2html.tags.DomContent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A parser for the JSON findings reports of Darkmoon, an open source autonomous AI penetration testing platform. The
 * report is the findings export of the Darkmoon GitHub Action ({@code report-format: json}): a JSON array of finding
 * objects. A JSON object that wraps the same array in a {@code findings} property is accepted as well, and every
 * property is read from its {@code camelCase} name or its {@code snake_case} name.
 *
 * @see <a href="https://github.com/ASCIT31/Dark-Moon">Darkmoon</a>
 * @see <a href="https://github.com/ASCIT31/darkmoon-action">Darkmoon GitHub Action</a>
 */
public class DarkmoonParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 2783421955611180724L;

    private static final String FINDINGS_KEY = "findings";
    private static final String TITLE_KEY = "title";
    private static final String SEVERITY_KEY = "severity";
    private static final String STATUS_KEY = "status";
    private static final String CATEGORY_KEY = "category";
    private static final String ENDPOINT_KEY = "endpoint";
    private static final String DESCRIPTION_KEY = "description";
    private static final String REMEDIATION_KEY = "remediation";
    private static final String CVE_KEY = "cve";
    private static final String CVSS_SCORE_KEY = "cvssScore";
    private static final String CVSS_SCORE_SNAKE_KEY = "cvss_score";
    private static final String CVSS_VECTOR_KEY = "cvssVector";
    private static final String CVSS_VECTOR_SNAKE_KEY = "cvss_vector";
    private static final String MITRE_ID_KEY = "mitreAttackId";
    private static final String MITRE_ID_SNAKE_KEY = "mitre_attack_id";
    private static final String MITRE_NAME_KEY = "mitreAttackName";
    private static final String MITRE_NAME_SNAKE_KEY = "mitre_attack_name";
    private static final String AGENT_KEY = "discoveredByAgent";
    private static final String AGENT_SNAKE_KEY = "discovered_by_agent";
    private static final String EVIDENCE_KEY = "evidence";
    private static final String EXPLANATION_KEY = "explanation";
    private static final String EVIDENCE_EXPLANATION_SNAKE_KEY = "evidence_explanation";

    private static final String VALUE_NOT_SET = "-";
    private static final String DEFAULT_SEVERITY = "medium";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var findings = jsonReport.optJSONArray(FINDINGS_KEY);
        if (findings != null) {
            parseJsonArray(report, findings, issueBuilder);
        }
    }

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (int i = 0; i < jsonReport.length(); i++) {
            var finding = jsonReport.optJSONObject(i);
            if (finding != null) {
                report.add(convertToIssue(finding, issueBuilder));
            }
        }
    }

    private Issue convertToIssue(final JSONObject finding, final IssueBuilder issueBuilder) {
        var status = finding.optString(STATUS_KEY, "");
        var severity = firstNonBlank(finding, SEVERITY_KEY);

        return issueBuilder
                .setFileName(getStringOrDefaultIfBlank(finding, ENDPOINT_KEY, VALUE_NOT_SET))
                .setType(getStringOrDefaultIfBlank(finding, CATEGORY_KEY, VALUE_NOT_SET))
                .setCategory(status.isBlank() ? VALUE_NOT_SET : status)
                .setPackageName(firstNonBlankOrDefault(finding, VALUE_NOT_SET, AGENT_KEY, AGENT_SNAKE_KEY))
                .setMessage(getStringOrDefaultIfBlank(finding, TITLE_KEY, VALUE_NOT_SET))
                .guessSeverity(severity.isBlank() ? DEFAULT_SEVERITY : severity)
                .setDescription(buildDescription(finding, status))
                .buildAndClean();
    }

    private String firstNonBlankOrDefault(final JSONObject finding, final String defaultValue, final String... keys) {
        var value = firstNonBlank(finding, keys);
        return value.isBlank() ? defaultValue : value;
    }

    private String buildDescription(final JSONObject finding, final String status) {
        List<DomContent> tags = new ArrayList<>();

        var description = finding.optString(DESCRIPTION_KEY, "");
        if (!description.isBlank()) {
            tags.add(p(description));
        }
        if (!status.isBlank()) {
            tags.add(p(strong("Status:"), text(" " + status)));
        }
        appendCvss(finding, tags);
        appendCve(finding, tags);
        appendMitre(finding, tags);

        var explanation = findExplanation(finding);
        if (!explanation.isBlank()) {
            tags.add(p(strong("Evidence:"), text(" " + explanation)));
        }
        var remediation = finding.optString(REMEDIATION_KEY, "");
        if (!remediation.isBlank()) {
            tags.add(p(strong("Remediation:"), text(" " + remediation)));
        }

        return join((Object[]) tags.toArray(new DomContent[0])).render();
    }

    private void appendCvss(final JSONObject finding, final List<DomContent> tags) {
        var score = finding.has(CVSS_SCORE_KEY)
                ? finding.optDouble(CVSS_SCORE_KEY, -1)
                : finding.optDouble(CVSS_SCORE_SNAKE_KEY, -1);
        if (score >= 0) {
            tags.add(p(strong("CVSS Score:"), text(" " + score)));
        }
        var vector = firstNonBlank(finding, CVSS_VECTOR_KEY, CVSS_VECTOR_SNAKE_KEY);
        if (!vector.isBlank()) {
            tags.add(p(strong("CVSS Vector:"), text(" "), code(vector)));
        }
    }

    private void appendCve(final JSONObject finding, final List<DomContent> tags) {
        var cve = finding.optString(CVE_KEY, "").trim();
        if (cve.startsWith("CVE-")) {
            tags.add(p(strong("CVE:"), text(" "), a(cve).withHref("https://nvd.nist.gov/vuln/detail/" + cve)));
        } else if (!cve.isEmpty()) {
            tags.add(p(strong("CVE:"), text(" " + cve)));
        }
    }

    private void appendMitre(final JSONObject finding, final List<DomContent> tags) {
        var id = firstNonBlank(finding, MITRE_ID_KEY, MITRE_ID_SNAKE_KEY);
        if (id.isBlank()) {
            return;
        }
        var name = firstNonBlank(finding, MITRE_NAME_KEY, MITRE_NAME_SNAKE_KEY);
        var label = name.isBlank() ? id : id + " (" + name + ")";
        tags.add(p(strong("MITRE ATT&CK:"), text(" " + label)));
    }

    private String findExplanation(final JSONObject finding) {
        var explanation = firstNonBlank(finding.optJSONObject(EVIDENCE_KEY), EXPLANATION_KEY);
        return explanation.isBlank()
                ? finding.optString(EVIDENCE_EXPLANATION_SNAKE_KEY, "").trim()
                : explanation;
    }
}
