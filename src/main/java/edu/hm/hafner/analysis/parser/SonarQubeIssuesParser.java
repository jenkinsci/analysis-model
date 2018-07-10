package edu.hm.hafner.analysis.parser;

import org.json.JSONObject;

/**
 * Class which parses SonarQube reports taken from the SonarQube API (api/issues/search).
 *
 * @author Carles Capdevila
 */
public class SonarQubeIssuesParser extends SonarQubeParser {
    private static final long serialVersionUID = -8213765181968340929L;
    
    private static final String ISSUE_SUB_PROJECT = "subProject";

    @Override
    protected String getModulePath(final JSONObject component, final JSONObject issue) {
        return parseModulePath(issue, ISSUE_SUB_PROJECT);
    }
}
