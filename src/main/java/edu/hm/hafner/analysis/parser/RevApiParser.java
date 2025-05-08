package edu.hm.hafner.analysis.parser;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.RevApiInfoExtension;
import edu.hm.hafner.analysis.Severity;

import j2html.tags.DomContent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static j2html.TagCreator.*;

/**
 * Parser for Revapi reports.
 */
public class RevApiParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = -2452699725595063377L;
    private static final String UNDEFINED = "-";

    private static final String FQN = "classQualifiedName";
    private static final String PACKAGE = "package";
    private static final String CODE = "code";
    private static final String ELEMENT_KIND = "elementKind";
    private static final char DOT = '.';
    private static final char SLASH = '/';
    private static final String JAVA_EXTENSION = ".java";
    private static final String CLASS_SIMPLE_NAME = "classSimpleName";

    @Override
    protected void parseJsonArray(final Report report, final JSONArray jsonReport, final IssueBuilder issueBuilder) {
        for (Object issue : jsonReport) {
            if (issue instanceof JSONObject object) {
                report.add(convertToIssue(object, issueBuilder));
            }
            else {
                report.logError("RevApi element is not a valid JSON object: %s", issue);
            }
        }
    }

    private Issue convertToIssue(final JSONObject jsonIssue, final IssueBuilder builder) {
        builder.setSeverity(evaluateSeverity(jsonIssue.getJSONArray("classification")));
        builder.setMessage(jsonIssue.getString("description"));

        var attachments = getAttachments(jsonIssue);
        builder.setDescription(getDescription(jsonIssue, attachments));

        builder.setFileName(getFileName(attachments));
        builder.setPackageName(attachments.get(PACKAGE));
        builder.setCategory(StringUtils.capitalize(attachments.get(ELEMENT_KIND)));
        builder.setType(attachments.get(CODE));
        builder.setAdditionalProperties(convertToGroup(jsonIssue));

        return builder.build();
    }

    @SuppressWarnings("NullAway")
    private String getFileName(final Map<String, String> attachments) {
        return attachments.get(PACKAGE).replace(DOT, SLASH) + SLASH
                + attachments.get(CLASS_SIMPLE_NAME)
                + JAVA_EXTENSION;
    }

    private RevApiInfoExtension convertToGroup(final JSONObject jsonIssue) {
        return new RevApiInfoExtension(
                jsonIssue.getString(CODE),
                extractChange(jsonIssue, "old"),
                extractChange(jsonIssue, "new"),
                extractSeverities(jsonIssue));
    }

    private static Map<String, String> extractSeverities(final JSONObject jsonIssue) {
        Map<String, String> allSeverities = new HashMap<>();
        for (Object severity : jsonIssue.getJSONArray("classification")) {
            if (severity instanceof JSONObject object) {
                allSeverities.put(object.getString("compatibility"), object.getString("severity"));
            }
        }
        return allSeverities;
    }

    private static String extractChange(final JSONObject jsonIssue, final String key) {
        var value = jsonIssue.get(key).toString();
        return "null".equals(value) ? "-" : value;
    }

    private Severity evaluateSeverity(final JSONArray classification) {
        Set<Severity> allSeverities = new HashSet<>();
        for (Object severity : classification) {
            if (severity instanceof JSONObject object) {
                allSeverities.add(toSeverity(object.getString("severity")));
            }
        }
        if (allSeverities.contains(Severity.WARNING_HIGH)) {
            return Severity.WARNING_HIGH;
        }
        if (allSeverities.contains(Severity.WARNING_NORMAL)) {
            return Severity.WARNING_NORMAL;
        }
        return Severity.WARNING_LOW;
    }

    private Severity toSeverity(final String level) {
        return switch (level) {
            case "BREAKING" -> Severity.WARNING_HIGH;
            case "POTENTIALLY_BREAKING" -> Severity.WARNING_NORMAL;
            default -> Severity.WARNING_LOW;
        };
    }

    private String getDescription(final JSONObject jsonIssue, final Map<String, String> attachments) {
        var html = table(
                tr(td("Class:"), td(attachments.getOrDefault(FQN, UNDEFINED))),
                tr(td("Code:"), td(attachments.getOrDefault(CODE, UNDEFINED))),
                tr(td("Name:"), td(attachments.getOrDefault("name", UNDEFINED))),
                tr(td("New Element:"), td(attachments.getOrDefault("new", UNDEFINED))),
                tr(td("Old Element:"), td(attachments.getOrDefault("old", UNDEFINED))),
                tr(td("Justification:"), td(attachments.getOrDefault("justification", UNDEFINED))),
                tr(td("Classification:"), td(createClassification(jsonIssue))));
        return html.renderFormatted();
    }

    private Map<String, String> getAttachments(final JSONObject jsonIssue) {
        var attachments = new HashMap<String, String>();
        for (Object attachment : jsonIssue.getJSONArray("attachments")) {
            if (attachment instanceof JSONObject object) {
                var key = object.getString("name");
                if (object.has("name")) {
                    attachments.put(key, object.getString("value"));
                }
            }
        }
        addValue(CODE, jsonIssue, attachments);
        addValue("old", jsonIssue, attachments);
        addValue("new", jsonIssue, attachments);
        addValue("name", jsonIssue, attachments);
        addValue("description", jsonIssue, attachments);
        addValue("criticality", jsonIssue, attachments);
        addValue("justification", jsonIssue, attachments);
        return attachments;
    }

    private void addValue(final String key, final JSONObject jsonIssue, final Map<String, String> attachments) {
        if (jsonIssue.has(key) && jsonIssue.get(key) instanceof String) {
            var code = jsonIssue.getString(key);
            if (code != null && !code.isEmpty()) {
                attachments.put(key, code);
            }
        }
    }

    private DomContent createClassification(final JSONObject jsonIssue) {
        var elements = new ArrayList<DomContent>();
        for (Object severity : jsonIssue.getJSONArray("classification")) {
            if (severity instanceof JSONObject object) {
                elements.add(join(dt(object.getString("compatibility")),
                        dd(object.getString("severity"))));
            }
        }
        if (elements.isEmpty()) {
            return text("-");
        }
        return dl(elements.toArray(new DomContent[0]));
    }
}
