package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Serial;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ReaderFactory;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Base class for SonarQube parsers.
 *
 * @author Carles Capdevila
 */
public abstract class SonarQubeParser extends JsonIssueParser {
    @Serial
    private static final long serialVersionUID = 1958805067002376816L;

    //Arrays
    /** The components array. */
    private static final String COMPONENTS = "components";
    /** The issues array. */
    private static final String ISSUES = "issues";

    //Issues attributes
    /** issue.component attribute. */
    private static final String ISSUE_COMPONENT = "component";
    /** issue.message attribute. */
    private static final String ISSUE_MESSAGE = "message";
    /** issue.line attribute. */
    private static final String ISSUE_SEVERITY = "severity";
    /** issue.type attribute. */
    private static final String ISSUE_TYPE = "type";

    //Component attributes
    /** component.key attribute. */
    private static final String COMPONENT_KEY = "key";
    /** component.path attribute. */
    private static final String COMPONENT_PATH = "path";

    // Severity values
    /** severity value: BLOCKER. */
    private static final String SEVERITY_BLOCKER = "BLOCKER";
    /** severity value: CRITICAL. */
    private static final String SEVERITY_CRITICAL = "CRITICAL";
    // Severity MAJOR is omitted as it corresponds with default Severity: NORMAL
    /** severity value: MINOR. */
    private static final String SEVERITY_MINOR = "MINOR";
    /** severity value: INFO. */
    private static final String SEVERITY_INFO = "INFO";
    /** Fixed category: SonarQube. */
    private static final String CATEGORY_SONAR_QUBE = "SonarQube";

    /** The components array. */
    @CheckForNull
    private transient JSONArray components = new JSONArray();

    @Override
    public boolean accepts(final ReaderFactory readerFactory) {
        try (var reader = readerFactory.create()) {
            return accepts((JSONObject) new JSONTokener(reader).nextValue());
        }
        catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Returns whether this parser accepts the specified JSON object as valid input.
     *
     * @param object
     *         the JSON object to analyse
     *
     * @return {@code true} if this parser accepts this object as valid input, {@code false} otherwise
     */
    abstract boolean accepts(JSONObject object);

    @Override
    protected void parseJsonObject(final Report report, final JSONObject jsonReport, final IssueBuilder issueBuilder) {
        extractComponents(jsonReport);

        if (jsonReport.has(ISSUES)) {
            extractIssues(jsonReport.optJSONArray(ISSUES), report, issueBuilder);
        }
    }

    private void extractIssues(final JSONArray elements, final Report report, final IssueBuilder issueBuilder) {
        for (Object object : elements) {
            if (object instanceof final JSONObject issue && filterIssue(issue)) {
                report.add(createIssueFromJsonObject(issue, issueBuilder));
            }
        }
    }

    /**
     * Get the "components" part to get the file paths on each issue (the component objects contain the most concise
     * path).
     *
     * @param jsonReport
     *         the report to get the components from
     */
    private void extractComponents(final JSONObject jsonReport) {
        if (jsonReport.has(COMPONENTS)) {
            components = jsonReport.optJSONArray(COMPONENTS);
        }
    }

    /**
     * Decides whether or not to parse and add an issue.
     *
     * @param issue
     *         the issue to filter.
     *
     * @return {@code true} if the issue is to be parsed and added, otherwise {@code false}
     */
    boolean filterIssue(final JSONObject issue) {
        return true; // Parse all issues by default
    }

    private Issue createIssueFromJsonObject(final JSONObject issue, final IssueBuilder issueBuilder) {
        return issueBuilder
                .setFileName(parseFilename(issue))
                .setLineStart(parseStart(issue))
                .setLineEnd(parseEnd(issue))
                .setType(parseType(issue))
                .setCategory(CATEGORY_SONAR_QUBE)
                .setMessage(parseMessage(issue))
                .setSeverity(parsePriority(issue))
                .buildAndClean();
    }

    /**
     * Parse function for filename.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the filename.
     */
    private String parseFilename(final JSONObject issue) {
        // Get component
        var componentKey = issue.optString(ISSUE_COMPONENT, null);
        var component = findComponentByKey(componentKey);

        if (component == null) {
            var issueComponentKey = issue.optString(ISSUE_COMPONENT);
            return issueComponentKey.substring(issueComponentKey.lastIndexOf(':'));
        }
        else {
            // Get file path inside module
            var filePath = component.optString(COMPONENT_PATH);

            // Get module file path
            var modulePath = getModulePath(component, issue);
            return modulePath + filePath;
        }
    }

    /**
     * Extracts the module path from the specified JSON objects.
     *
     * @param component
     *         the component
     * @param issue
     *         the issue
     *
     * @return the module path
     */
    abstract String getModulePath(JSONObject component, JSONObject issue);

    /**
     * Default parse for start.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the start.
     */
    abstract int parseStart(JSONObject issue);

    /**
     * Default parse for end.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the end.
     */
    abstract int parseEnd(JSONObject issue);

    /**
     * Default parse for type.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the type.
     */
    private String parseType(final JSONObject issue) {
        return issue.optString(ISSUE_TYPE, "");
    }

    /**
     * Default parse for message.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the message.
     */
    private String parseMessage(final JSONObject issue) {
        return issue.optString(ISSUE_MESSAGE, "No message.");
    }

    /**
     * Default parse for priority.
     *
     * @param issue
     *         the object to parse.
     *
     * @return the priority.
     */
    private Severity parsePriority(final JSONObject issue) {
        return severityToPriority(issue.optString(ISSUE_SEVERITY, SEVERITY_MINOR));
    }

    //UTILITIES

    /**
     * Find the module path inside the corresponding component.
     *
     * @param moduleKeyObject
     *         the object which contains the component key.
     * @param componentKey
     *         the component key.
     *
     * @return the module path.
     */
    String parseModulePath(final JSONObject moduleKeyObject, final String componentKey) {
        var modulePath = "";
        if (moduleKeyObject.has(componentKey)) {
            var moduleKey = moduleKeyObject.getString(componentKey);
            var moduleComponent = findComponentByKey(moduleKey);
            if (moduleComponent != null && moduleComponent.has(COMPONENT_PATH)) {
                modulePath = moduleComponent.getString(COMPONENT_PATH) + "/";
            }
        }
        return modulePath;
    }

    /**
     * Find the component in the components-array which contains this key.
     *
     * @param key
     *         the key of the desired component.
     *
     * @return the desired JSONObject component, or null if it hasn't been found.
     */
    @CheckForNull
    private JSONObject findComponentByKey(final String key) {
        if (components != null && key != null) {
            for (Object component : components) {
                if (component instanceof final JSONObject jsonComponent
                        && key.equals(jsonComponent.optString(COMPONENT_KEY))) {
                    return jsonComponent;
                }
            }
        }

        return null;
    }

    /**
     * Maps issue severity to warning priority.
     *
     * <br>
     * <strong>HIGH:</strong> BLOCKER, CRITICAL
     * <br>
     * <strong>NORMAL:</strong> MAJOR
     * <br>
     * <strong>LOW:</strong> MINOR, INFO
     *
     * @param severity
     *         a String containing the SonarQube issue severity
     *
     * @return a priority object corresponding to the passed severity.
     */
    private Severity severityToPriority(final String severity) {
        // Severity MAJOR is omitted as it corresponds with default Severity: NORMAL
        if (SEVERITY_BLOCKER.equals(severity) || SEVERITY_CRITICAL.equals(severity)) {
            return Severity.WARNING_HIGH;
        }
        else if (SEVERITY_MINOR.equals(severity) || SEVERITY_INFO.equals(severity)) {
            return Severity.WARNING_LOW;
        }
        return Severity.WARNING_NORMAL;
    }
}
