package edu.hm.hafner.analysis.parser;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;

import j2html.tags.DomContent;
import j2html.tags.Text;

import static j2html.TagCreator.*;

/**
 * Parser for Detectify vulnerability reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://developer.detectify.com/v2">Detectify API</a>
 * @see <a href="https://docs.detectify.com/web-application-security-testing/results">Detectify Results</a>
 */
public class DetectifyParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -4081198330941882273L;

    private static final String VULNERABILITIES_TAG = "vulnerabilities";
    private static final String VULNERABILITY_TAG = "vulnerability";
    private static final String UUID_TAG = "uuid";
    private static final String TITLE_TAG = "title";
    private static final String SEVERITY_TAG = "severity";
    private static final String HOST_TAG = "host";
    private static final String LOCATION_TAG = "location";
    private static final String SCAN_SOURCE_TAG = "scan_source";
    private static final String STATUS_TAG = "status";
    private static final String DETAILS_TAG = "details";
    private static final String DESCRIPTION_TAG = "description";
    private static final String REMEDIATION_TAG = "remediation";
    private static final String PROOF_OF_CONCEPT_TAG = "proof_of_concept";
    private static final String REFERENCES_TAG = "references";
    private static final String LINKS_TAG = "links";
    private static final String OWASP_TAG = "owasp";
    private static final String CWE_TAG = "cwe";
    private static final String DEFINITION_TAG = "definition";
    private static final String IDENTIFIER_TAG = "identifier";
    private static final String ID_TAG = "id";
    private static final String SLUG_TAG = "slug";
    private static final String TYPE_TAG = "type";

    private static final Text COMMA = text(", ");
    private static final Text NBSP = text("\u00a0");

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var vulnerabilities = jsonReport.optJSONArray(VULNERABILITIES_TAG);
        if (vulnerabilities != null) {
            parseVulnerabilities(report, vulnerabilities, issueBuilder);
        }

        var vulnerability = jsonReport.optJSONObject(VULNERABILITY_TAG);
        if (vulnerability != null) {
            report.add(createIssue(vulnerability, issueBuilder));
        }
    }

    private void parseVulnerabilities(final Report report, final JSONArray vulnerabilities,
            final IssueBuilder issueBuilder) {
        for (int i = 0; i < vulnerabilities.length(); i++) {
            var vulnerability = vulnerabilities.getJSONObject(i);
            report.add(createIssue(vulnerability, issueBuilder));
        }
    }

    private Issue createIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        var location = firstNonBlank(vulnerability, LOCATION_TAG, HOST_TAG);

        return issueBuilder
                .setFileName(location)
                .setCategory(firstNonBlank(vulnerability, SCAN_SOURCE_TAG, STATUS_TAG))
                .guessSeverity(vulnerability.optString(SEVERITY_TAG, "low"))
                .setType(getIssueType(vulnerability))
                .setMessage(vulnerability.optString(TITLE_TAG, "Unknown"))
                .setDescription(buildDescription(vulnerability))
                .buildAndClean();
    }

    private String getIssueType(final JSONObject vulnerability) {
        var definition = vulnerability.optJSONObject(DEFINITION_TAG);
        var issueType = firstNonBlank(definition, IDENTIFIER_TAG, ID_TAG, SLUG_TAG, TYPE_TAG);
        if (issueType.isBlank()) {
            issueType = firstNonBlank(vulnerability, UUID_TAG, TITLE_TAG);
        }
        return issueType;
    }

    private String buildDescription(final JSONObject vulnerability) {
        var sections = new ArrayList<DomContent>();
        var details = vulnerability.optJSONObject(DETAILS_TAG);

        appendTextSection(sections, "Description", firstNonBlank(details, DESCRIPTION_TAG));
        appendTextSection(sections, "Remediation", firstNonBlank(details, REMEDIATION_TAG));
        appendTextSection(sections, "Proof of Concept", firstNonBlank(details, PROOF_OF_CONCEPT_TAG));
        appendCweSection(sections, vulnerability);
        appendOwaspSection(sections, vulnerability);
        appendReferencesSection(sections, vulnerability);
        appendLinksSection(sections, vulnerability);

        if (sections.isEmpty()) {
            return "";
        }
        return join((Object[]) sections.toArray(new DomContent[0])).render();
    }

    private void appendTextSection(final List<DomContent> sections, final String label, final String value) {
        if (!value.isBlank()) {
            sections.add(p(strong(label + ":"), NBSP, new Text(value)));
        }
    }

    private void appendCweSection(final List<DomContent> sections, final JSONObject vulnerability) {
        if (!vulnerability.has(CWE_TAG)) {
            return;
        }

        var cwe = vulnerability.optInt(CWE_TAG, 0);
        if (cwe > 0) {
            sections.add(p(strong("CWE:"), NBSP, text("CWE-" + cwe)));
        }
    }

    private void appendOwaspSection(final List<DomContent> sections, final JSONObject vulnerability) {
        var owasp = vulnerability.optJSONArray(OWASP_TAG);
        if (owasp == null || owasp.isEmpty()) {
            return;
        }

        var items = arrayAsContents(owasp);
        if (items.isEmpty()) {
            return;
        }
        sections.add(p(strong("OWASP:"), NBSP, joinWithSeparator(items, COMMA)));
    }

    private void appendReferencesSection(final List<DomContent> sections, final JSONObject vulnerability) {
        var references = vulnerability.optJSONArray(REFERENCES_TAG);
        if (references == null || references.isEmpty()) {
            return;
        }

        var items = new ArrayList<DomContent>();
        for (int i = 0; i < references.length(); i++) {
            var reference = references.get(i);
            if (reference instanceof String url) {
                items.add(createLinkOrText(url, url));
            }
            else if (reference instanceof JSONObject object) {
                var url = firstNonBlank(object, "url", "href", "link");
                var label = firstNonBlank(object, "title", "name", "text");
                if (!url.isBlank()) {
                    items.add(createLinkOrText(url, label.isBlank() ? url : label));
                }
                else if (!label.isBlank()) {
                    items.add(text(label));
                }
            }
        }

        if (!items.isEmpty()) {
            sections.add(p(strong("References:"), NBSP, joinWithSeparator(items, COMMA)));
        }
    }

    private void appendLinksSection(final List<DomContent> sections, final JSONObject vulnerability) {
        var links = vulnerability.optJSONObject(LINKS_TAG);
        if (links == null || links.length() == 0) {
            return;
        }

        var items = new ArrayList<DomContent>();
        for (String key : links.keySet()) {
            var value = links.optString(key, "");
            if (!value.isBlank()) {
                items.add(createLinkOrText(value, key));
            }
        }

        if (!items.isEmpty()) {
            sections.add(p(strong("Links:"), NBSP, joinWithSeparator(items, COMMA)));
        }
    }

    private DomContent createLinkOrText(final String url, final String label) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return a().withHref(url).withText(label);
        }
        return text(label);
    }

    private DomContent joinWithSeparator(final List<DomContent> contents, final Text separator) {
        var joined = new ArrayList<DomContent>();
        var iterator = contents.iterator();
        while (iterator.hasNext()) {
            joined.add(iterator.next());
            if (iterator.hasNext()) {
                joined.add(separator);
            }
        }
        return join((Object[]) joined.toArray(new DomContent[0]));
    }

    private List<DomContent> arrayAsContents(final JSONArray array) {
        var content = new ArrayList<DomContent>();
        for (int i = 0; i < array.length(); i++) {
            var value = array.optString(i, "");
            if (!value.isBlank()) {
                content.add(text(value));
            }
        }
        return content;
    }
}