package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import j2html.tags.DomContent;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

/**
 * A parser for Kyverno policy validation JSON reports (OpenReports format).
 *
 * @author Akash Manna
 * @see <a href="https://kyverno.io/">Kyverno</a>
 * @see <a href="https://github.com/kyverno/kyverno">Kyverno on GitHub</a>
 */
public class KyvernoParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1523847234679L;

    private static final String RESULTS = "results";
    private static final String RESOURCE = "resource";
    private static final String RULES = "rules";
    private static final String METADATA = "metadata";
    private static final String KIND = "kind";
    private static final String NAME = "name";
    private static final String NAMESPACE = "namespace";
    private static final String API_VERSION = "apiVersion";
    private static final String RULE_NAME = "name";
    private static final String RULE_TYPE = "type";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String FAIL_STATUS = "fail";
    private static final String ERROR_STATUS = "error";

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        if (jsonReport.has(RESULTS)) {
            var results = jsonReport.getJSONArray(RESULTS);
            for (int i = 0; i < results.length(); i++) {
                var result = results.getJSONObject(i);
                parseResult(report, result, issueBuilder);
            }
        }
    }

    private void parseResult(final Report report, final JSONObject result, final IssueBuilder issueBuilder) {
        var resource = result.optJSONObject(RESOURCE);
        var resourceName = extractResourceName(resource);
        var rules = result.optJSONArray(RULES);

        if (rules != null) {
            for (int i = 0; i < rules.length(); i++) {
                var rule = rules.getJSONObject(i);
                var status = rule.optString(STATUS, "");
                if (FAIL_STATUS.equals(status) || ERROR_STATUS.equals(status)) {
                    report.add(convertToIssue(rule, resourceName, resource, issueBuilder));
                }
            }
        }
    }

    private Issue convertToIssue(@CheckForNull final JSONObject rule, final String resourceName,
                                  @CheckForNull final JSONObject resource, final IssueBuilder issueBuilder) {
        var ruleName = rule.optString(RULE_NAME, "-");
        var ruleType = rule.optString(RULE_TYPE, "-");
        var message = rule.optString(MESSAGE, "Policy validation failed");
        var status = rule.optString(STATUS, "fail");

        var description = buildDescription(ruleName, ruleType, resource);

        issueBuilder
                .setFileName(resourceName)
                .setCategory(ruleType)
                .setType(ruleName)
                .setMessage(message)
                .setDescription(description)
                .setSeverity(mapSeverity(status));

        return issueBuilder.buildAndClean();
    }

    private String extractResourceName(@CheckForNull final JSONObject resource) {
        if (resource == null) {
            return "-";
        }

        var metadata = resource.optJSONObject(METADATA);
        var name = getMetadataString(metadata, NAME);
        var namespace = getMetadataString(metadata, NAMESPACE);

        if (!name.isEmpty()) {
            return namespace.isEmpty() ? name : namespace + "/" + name;
        }
        return "-";
    }

    private String getMetadataString(@CheckForNull final JSONObject metadata, final String key) {
        return metadata == null ? "" : metadata.optString(key, "");
    }

    private String buildDescription(final String ruleName, final String ruleType,
                                        @CheckForNull final JSONObject resource) {
        var parts = new ArrayList<DomContent>();
        parts.add(b("Rule: "));
        parts.add(text(ruleName));
        parts.add(br());
        parts.add(b("Type: "));
        parts.add(text(ruleType));

        if (resource != null) {
            addResourceDetails(parts, resource);
        }

        return join((Object[]) parts.toArray(new DomContent[0])).render();
    }

    private void addResourceDetails(final List<DomContent> parts, final JSONObject resource) {
        parts.add(br());
        parts.add(b("Resource Kind: "));
        parts.add(text(resource.optString(KIND, "-")));

        parts.add(br());
        parts.add(b("API Version: "));
        parts.add(text(resource.optString(API_VERSION, "-")));

        var metadata = resource.optJSONObject(METADATA);
        if (metadata != null) {
            addMetadataDetails(parts, metadata);
        }
    }

    private void addMetadataDetails(final List<DomContent> parts, final JSONObject metadata) {
        var name = metadata.optString(NAME);
        if (!name.isEmpty()) {
            parts.add(br());
            parts.add(b("Resource Name: "));
            parts.add(text(name));
        }

        var namespace = metadata.optString(NAMESPACE);
        if (!namespace.isEmpty()) {
            parts.add(br());
            parts.add(b("Namespace: "));
            parts.add(text(namespace));
        }
    }

    private Severity mapSeverity(final String status) {
        if (ERROR_STATUS.equals(status)) {
            return Severity.ERROR;
        }
        return Severity.WARNING_NORMAL;
    }
}
