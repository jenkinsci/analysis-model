package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

/**
 * Class which parses SonarQube reports taken from SonarQube differential scan report (preview).
 *
 * @author Carles Capdevila
 */
public class SonarQubeDiffParser extends SonarQubeParser {
    private static final long serialVersionUID = -47634856667313368L;

    private static final String ISSUE_IS_NEW = "isNew";
    private static final String COMPONENT_MODULE_KEY = "moduleKey";
    private static final String ISSUE_START_LINE = "startLine";
    private static final String ISSUE_END_LINE = "endLine";

    @Override
    protected boolean accepts(final JSONObject object) {
        return !object.has("total");
    }

    @Override
    public boolean filterIssue(final JSONObject issue) {
        return issue.optBoolean(ISSUE_IS_NEW, false);
    }

    @Override
    protected String getModulePath(final JSONObject component, final JSONObject issue) {
        return parseModulePath(component, COMPONENT_MODULE_KEY);
    }

    @Override
    protected int parseStart(final JSONObject issue) {
        return issue.optInt(ISSUE_START_LINE);
    }

    @Override
    protected int parseEnd(final JSONObject issue) {
        return issue.optInt(ISSUE_END_LINE);
    }
}
