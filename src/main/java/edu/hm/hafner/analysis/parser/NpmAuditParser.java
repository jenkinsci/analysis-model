package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import j2html.TagCreator;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import org.json.JSONObject;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static j2html.TagCreator.*;

/**
 * Parser for npm audit.
 *
 * @author Ulrich Grave
 */
public class NpmAuditParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String VALUE_NOT_SET = "-";
    private static final String UNCATEGORIZED = "Uncategorized";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        var vulnerabilities = jsonReport.optJSONObject("vulnerabilities");
        if (vulnerabilities != null) {
            for (String name : vulnerabilities.keySet()) {
                parseVulnerability(report, vulnerabilities.getJSONObject(name), issueBuilder);
            }
        }
    }

    private void parseVulnerability(final Report report, final JSONObject vulnerability, final IssueBuilder issueBuilder) {
        var via = vulnerability.optJSONArray("via");
        if (via != null) {
            var tags = collectTags(vulnerability);
            for (Object object : via) {
                if (object instanceof JSONObject dependency) {
                    report.add(convertToIssue(dependency, issueBuilder, tags));
                }
            }
        }
    }

    private boolean hasFixAvailable(final JSONObject vulnerability) {
        var fixAvailable = vulnerability.opt("fixAvailable");
        if (fixAvailable instanceof Boolean fixAvailableValue) {
            return fixAvailableValue;
        }
        else {
            return fixAvailable != null;
        }
    }

    private List<String> collectTags(final JSONObject vulnerability) {
        var tags = new ArrayList<String>();
        if (vulnerability.optBoolean("isDirect", false)) {
            tags.add("Direct");
        }
        else {
            tags.add("Indirect");
        }
        if (hasFixAvailable(vulnerability)) {
            tags.add("Fix Available");
        }
        else {
            tags.add("No Fix");
        }
        return tags;
    }

    private String formatDescription(final JSONObject vulnerability, final List<String> tags, final SortedSet<String> weaknessCategories) {
        DomContent desc = p(b(vulnerability.optString("name", VALUE_NOT_SET)), text(" "), code(vulnerability.optString("range")));
        if (!tags.isEmpty()) {
            desc = join(desc, p(String.join(", ", tags)));
        }
        ContainerTag details = p();
        details.with(b("Score:"), text(" "), text(cvssScore(vulnerability)));
        var url = vulnerability.optString("url", null);
        if (url != null) {
            details.with(br(), b("Reference:"), text(" "), a(url).withHref(url));
        }
        if (!weaknessCategories.isEmpty()) {
            details.with(br(), b("Weakness:"), weaknessCategories.stream().map(TagCreator::li).reduce(ul(), ContainerTag::with));
        }
        return join(desc, details).render();
    }

    private Issue convertToIssue(final JSONObject vulnerability, final IssueBuilder issueBuilder, final List<String> tags) {
        SortedSet<String> weaknessCategories = collectWeaknessCategories(vulnerability);
        return issueBuilder
                .setPackageName(vulnerability.optString("name", VALUE_NOT_SET))
                .setMessage(vulnerability.optString("title", VALUE_NOT_SET))
                .guessSeverity(vulnerability.optString("severity", "UNKNOWN"))
                .setDescription(formatDescription(vulnerability, tags, weaknessCategories))
                .setCategory(weaknessCategories.isEmpty() ? UNCATEGORIZED : weaknessCategories.first())
                .buildAndClean();
    }

    private String cvssScore(final JSONObject vulnerability) {
        var cvss = vulnerability.optJSONObject("cvss");
        if (cvss != null) {
            return cvss.optString("score", VALUE_NOT_SET);
        }
        return VALUE_NOT_SET;
    }

    private SortedSet<String> collectWeaknessCategories(final JSONObject viaObject) {
        var weaknessCategories = new TreeSet<String>();
        var cwes = viaObject.optJSONArray("cwe");
        if (cwes != null) {
            for (Object object : cwes) {
                if (object instanceof String cweNumber) {
                    weaknessCategories.add(cweNumber);
                }
            }
        }
        return weaknessCategories;
    }
}
