package edu.hm.hafner.analysis.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Text;
import j2html.tags.UnescapedText;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static j2html.TagCreator.*;

/**
 * Parser for Snyk security vulnerability reports in JSON format.
 *
 * @author Akash Manna
 * @see <a href="https://snyk.io/">Snyk</a>
 * @see <a href="https://github.com/snyk">Snyk on GitHub</a>
 */
public class SnykParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -4522651029150852849L;

    private static final String VULNERABILITIES_TAG = "vulnerabilities";
    private static final String ID_TAG = "id";
    private static final String TITLE_TAG = "title";
    private static final String DESCRIPTION_TAG = "description";
    private static final String SEVERITY_TAG = "severity";
    private static final String PACKAGE_NAME_TAG = "packageName";
    private static final String PACKAGE_VERSION_TAG = "packageVersion";
    private static final String FILE_PATH_TAG = "filePath";
    private static final String IDENTIFIERS_TAG = "identifiers";
    private static final String CVE_TAG = "CVE";
    private static final String CWE_TAG = "CWE";
    private static final String UPGRADE_PATH_TAG = "upgradePath";
    private static final String IS_UPGRADABLE_TAG = "isUpgradable";
    private static final String CVSS_V3_TAG = "CVSSv3";
    private static final String LANGUAGE_TAG = "language";
    private static final UnescapedText NBSP = new UnescapedText("&nbsp;");
    private static final Text COMMA = text(", ");
    private static final Text ARROW = text(" → ");

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(VULNERABILITIES_TAG)) {
            var vulnerabilities = jsonReport.getJSONArray(VULNERABILITIES_TAG);
            for (int i = 0; i < vulnerabilities.length(); i++) {
                var vulnerability = vulnerabilities.getJSONObject(i);
                var issue = createIssue(issueBuilder, vulnerability);
                report.add(issue);
            }
        }
    }

    private Issue createIssue(final IssueBuilder issueBuilder, final JSONObject vulnerability) {
        var message = vulnerability.optString(TITLE_TAG, "");
        var description = vulnerability.optString(DESCRIPTION_TAG, "");
        var descriptionHtml = buildDescription(message, description, vulnerability);

        var packageName = formatPackageName(vulnerability);
        return issueBuilder
                .setFileName(getFileName(vulnerability, packageName))
                .setPackageName(packageName)
                .setCategory(vulnerability.optString(LANGUAGE_TAG, "Unknown"))
                .guessSeverity(vulnerability.optString(SEVERITY_TAG, "medium"))
                .setType(vulnerability.optString(ID_TAG, "-"))
                .setMessage(message)
                .setDescription(descriptionHtml)
                .build();
    }

    private String formatPackageName(final JSONObject vulnerability) {
        var packageName = vulnerability.optString(PACKAGE_NAME_TAG, "Unknown");
        var packageVersion = vulnerability.optString(PACKAGE_VERSION_TAG, "");
        return packageVersion.isEmpty() ? packageName : packageName + "@" + packageVersion;
    }

    private String getFileName(final JSONObject vulnerability, final String packageName) {
        var fileName = vulnerability.optString(FILE_PATH_TAG, "");
        return fileName.isEmpty() ? packageName : fileName;
    }

    private String buildDescription(final String message, final String description, final JSONObject vulnerability) {
        var tags = new ArrayList<DomContent>();
        if (!message.isEmpty()) {
            tags.add(p(strong(message)));
        }
        if (!description.isEmpty()) {
            tags.add(p(description));
        }
        appendSection(vulnerability, CVE_TAG).ifPresent(tags::add);
        appendSection(vulnerability, CWE_TAG).ifPresent(tags::add);
        if (vulnerability.has(CVSS_V3_TAG)) {
            tags.add(p(strong("CVSS:"), NBSP, new Text(vulnerability.getString(CVSS_V3_TAG))));
        }
        appendUpgradeSection(vulnerability).ifPresent(tags::add);

        return join(tags.toArray(new DomContent[0])).render();
    }

    private Optional<ContainerTag> appendSection(final JSONObject vulnerability, final String tag) {
        if (hasNoTag(vulnerability, tag)) {
            return Optional.empty();
        }
        JSONArray cves = getTag(vulnerability, tag);
        if (cves.isEmpty()) {
            return Optional.empty();
        }
        var doms = arrayAsElements(cves, this::appendCveLink);
        return Optional.of(p(strong(tag + " ID(s):"), NBSP, joinWithSeparator(doms, COMMA)));
    }

    private JSONArray getTag(final JSONObject vulnerability, final String tag) {
        return vulnerability.getJSONObject(IDENTIFIERS_TAG).getJSONArray(tag);
    }

    private boolean hasNoTag(final JSONObject vulnerability, final String tag) {
        return !vulnerability.has(IDENTIFIERS_TAG)
                || !vulnerability.getJSONObject(IDENTIFIERS_TAG).has(tag);
    }

    private List<DomContent> arrayAsElements(final JSONArray cves, final Function<String, DomContent> converter) {
        var content = new ArrayList<DomContent>();
        cves.forEach(cve -> {
            if (cve instanceof String string) {
                content.add(converter.apply(string));
            }
        });
        return content;
    }

    private DomContent appendCwe(final String value) {
        return new Text(value.trim());
    }

    private DomContent appendCveLink(final String cve) {
        var trimmedCve = cve.trim();
        if (trimmedCve.matches("CVE-\\d{4}-\\d+")) {
            return a().withHref("https://nvd.nist.gov/vuln/detail/" + trimmedCve).withText(trimmedCve);
        }
        else {
            return text(trimmedCve);
        }
    }

    /**
     * Joins the specified {@link DomContent} elements with the given separator. The separator is inserted only between
     * elements (never as trailing content).
     *
     * @param contents
     *         the contents to join
     * @param separator
     *         the separator to insert between contents
     *
     * @return the joined {@link DomContent}
     */
    private DomContent joinWithSeparator(final List<DomContent> contents, final Text separator) {
        var joined = new ArrayList<DomContent>();
        var it = contents.iterator();
        while (it.hasNext()) {
            joined.add(it.next());
            if (it.hasNext()) {
                joined.add(separator);
            }
        }
        return join(joined.toArray());
    }

    private Optional<DomContent> appendUpgradeSection(final JSONObject vulnerability) {
        if (!vulnerability.has(UPGRADE_PATH_TAG)
                || !vulnerability.optBoolean(IS_UPGRADABLE_TAG, false)) {
            return Optional.empty();
        }
        var upgradePath = vulnerability.getJSONArray(UPGRADE_PATH_TAG);
        if (upgradePath.isEmpty()) {
            return Optional.empty();
        }

        var doms = arrayAsElements(upgradePath, this::appendCwe);
        return Optional.of(p(strong("Suggested Fix:"), NBSP, joinWithSeparator(doms, ARROW)));
    }
}
