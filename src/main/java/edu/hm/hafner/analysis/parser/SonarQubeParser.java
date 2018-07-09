package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.ParsingCanceledException;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;

/**
 * Base class for SonarQube parsers.
 *
 * @author Carles Capdevila
 */
public abstract class SonarQubeParser extends AbstractParser {
    private static final long serialVersionUID = 1958805067002376816L;

    //Arrays
    /** The components array. */
    protected static final String COMPONENTS = "components";
    /** The issues array. */
    protected static final String ISSUES = "issues";

    //Issues attributes
    /** issue.component attribute. */
    protected static final String ISSUE_COMPONENT = "component";
    /** issue.message attribute.  */
    protected static final String ISSUE_MESSAGE = "message";
    /** issue.line attribute. */
    protected static final String ISSUE_LINE = "line";
    /** issue.line attribute. */
    protected static final String ISSUE_SEVERITY = "severity";
    /** issue.type attribute. */
    protected static final String ISSUE_TYPE  = "type";

    //Component attributes
    /** component.key attribute. */
    protected static final String COMPONENT_KEY = "key";
    /** component.path attribute. */
    protected static final String COMPONENT_PATH = "path";

    // Severity values
    /** severity value: BLOCKER */
    protected static final String SEVERITY_BLOCKER = "BLOCKER";
    /** severity value: CRITICAL */
    protected static final String SEVERITY_CRITICAL = "CRITICAL";
    // Severity MAJOR is omitted as it corresponds with default Priority: NORMAL
    /** severity value: MINOR */
    protected static final String SEVERITY_MINOR = "MINOR";
    /** severity value: INFO */
    protected static final String SEVERITY_INFO = "INFO";
    /** Fixed category: SonarQube */
    protected static final String CATEGORY_SONARQUBE = "SonarQube";

    /** The issues array. */
    protected JSONArray issues;
    /** The components array. */
    protected JSONArray components;

    @Override
    public Report parse(Reader reader, Function<String, String> preProcessor)
    		throws ParsingCanceledException, ParsingException {
    	Report warnings = new Report();

        JSONObject jsonReport = (JSONObject)new JSONTokener(reader).nextValue();

        //Get the components part to get the file paths on each issue (the component objects contain the most concise path)
        components = new JSONArray();
        if (jsonReport.has(COMPONENTS)) {
            components = jsonReport.optJSONArray(COMPONENTS);
        }

        issues = new JSONArray();
        if (jsonReport.has(ISSUES)) {
            issues = jsonReport.optJSONArray(ISSUES);
        }

        //Iterate the issues
        for (Object issue : issues) {
            if (issue instanceof JSONObject) {
                //Filter the issues
                if (issueFilter((JSONObject)issue)) {
                    //Parse each issue
                    warnings.add(parseIssue((JSONObject)issue));
                }
            }
        }

        return warnings;
    }


    /**
     * Decides whether or not to parse and add an issue.
     * @param issue the issue to filter.
     * @return true if the issue is to be parsed and added, otherwise false.
     */
    public boolean issueFilter(final JSONObject issue) {
        return true;//Parse all issues by default
    }

    /**
     * The core of the parsing; this can be further overridden to determine how each issue is parsed and shown.
     * @param issue the issue to parse.
     * @return a FileAnnotation with the data of the issue to show.
     */
    public Issue parseIssue(final JSONObject issue) {
    	
    	IssueBuilder issueBuilder = new IssueBuilder();
    	
        //file
        String filename = parseFilename(issue);

        //line
        int start = parseStart(issue);

        //type
        String type = parseType(issue);

        //category
        String category = parseCategory(issue);

        //message
        String message = parseMessage(issue);

        //priority
        Priority priority = parsePriority(issue);

        return new IssueBuilder()
        			.setFileName(filename) 
        			.setLineStart(start)
        			.setType(type)
        			.setCategory(category)
        			.setMessage(message)
        			.setPriority(priority)
        			.build();
    }

    /**
     * Parse function for filename.
     * @param issue the object to parse.
     * @return the filename.
     */
    public String parseFilename(final JSONObject issue) {
        String componentKey = issue.optString(ISSUE_COMPONENT);
        return componentKey.substring(componentKey.lastIndexOf(':'));
    }

    /**
     * Default parse for start. Override to change the default parsing.
     * @param issue the object to parse.
     * @return the start.
     */
    public int parseStart(final JSONObject issue) {
        return issue.optInt(ISSUE_LINE, -1);
    }

    /**
     * Default parse for type. Override to change the default parsing.
     * @param issue the object to parse.
     * @return the type.
     */
    public String parseType(final JSONObject issue) {
        return issue.optString(ISSUE_TYPE, "");
    }

    /**
     * Default parse for category. Override to change the default parsing.
     * @param issue the object to parse.
     * @return the filename.
     */
    public String parseCategory(final JSONObject issue) {
        return CATEGORY_SONARQUBE;
    }

    /**
     * Default parse for message. Override to change the default parsing.
     * @param issue the object to parse.
     * @return the message.
     */
    public String parseMessage(final JSONObject issue) {
        return issue.optString(ISSUE_MESSAGE, "No message.");
    }

    /**
     * Default parse for priority. Override to change the default parsing.
     * @param issue the object to parse.
     * @return the priority.
     */
    public Priority parsePriority(final JSONObject issue) {
        String severity = issue.optString(ISSUE_SEVERITY, null);
        return severityToPriority(severity);
    }

    //UTILITIES
    /**
     * Find the module path inside the corresponding component.
     * @param moduleKeyObject the object which contains the component key.
     * @param componentKey the component key.
     * @return the module path.
     */
    protected String parseModulePath(final JSONObject moduleKeyObject, final String componentKey) {
        String modulePath = "";
        if (moduleKeyObject.has(componentKey)) {
            String moduleKey = moduleKeyObject.getString(componentKey);
            JSONObject moduleComponent = findComponentByKey(moduleKey);
            if (moduleComponent != null && moduleComponent.has(COMPONENT_PATH)) {
                modulePath = moduleComponent.getString(COMPONENT_PATH) + "/";
            }
        }
        return modulePath;
    }

    /**
     * Find the component in the components array which contains this key.
     * @param key the key of the desired component.
     * @return the desired JSONObject component, or null if it hasn't been found.
     */
    protected JSONObject findComponentByKey (final String key) {
        if (components != null && key != null) {
            for (Object component : components) {
                if (component instanceof JSONObject) {
                    JSONObject jsonComponent = (JSONObject)component;
                    if (key.equals(jsonComponent.optString(COMPONENT_KEY))) {
                        return (JSONObject)component;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Maps issue severity to warning priority:<br>
     * <strong>HIGH:</strong> BLOCKER, CRITICAL<br>
     * <strong>NORMAL:</strong> MAJOR<br>
     * <strong>LOW:</strong> MINOR, INFO
     *
     * @param severity a String containing the SonarQube issue severity.
     * @return a priority object corresponding to the passed severity.
     */
    protected Priority severityToPriority (final String severity) {
        Priority priority = Priority.NORMAL;
        // Severity MAJOR is omitted as it corresponds with default Priority: NORMAL
        if (severity != null) {
            if (SEVERITY_BLOCKER.equals(severity) || SEVERITY_CRITICAL.equals(severity)) {
                priority = Priority.HIGH;
            } else if (SEVERITY_MINOR.equals(severity) || SEVERITY_INFO.equals(severity)) {
                priority = Priority.LOW;
            }
        }
        return priority;
    }

}

