package edu.hm.hafner.analysis.parser;

import java.io.Serial;

import org.json.JSONObject;

/**
 * Class which parses SonarQube reports taken from the SonarQube API (api/issues/search).
 *
 * @author Carles Capdevila
 */
public class SonarQubeIssuesParser extends SonarQubeParser {
    @Serial
    private static final long serialVersionUID = -8213765181968340929L;

    private static final String ISSUE_SUB_PROJECT = "subProject";
    private static final String ISSUE_TEXT_RANGE = "textRange";
    private static final String ISSUE_TEXT_RANGE_START_LINE = "startLine";
    private static final String ISSUE_TEXT_RANGE_END_LINE = "endLine";
    private static final String ISSUE_LINE = "line";

    @Override
    boolean accepts(final JSONObject object) {
        return object.has("total");
    }

    @Override
    String getModulePath(final JSONObject component, final JSONObject issue) {
        return parseModulePath(issue, ISSUE_SUB_PROJECT);
    }

    @Override
    int parseStart(final JSONObject issue) {
        if (issue.has(ISSUE_TEXT_RANGE)) {
            return issue.optJSONObject(ISSUE_TEXT_RANGE).optInt(ISSUE_TEXT_RANGE_START_LINE);
        }
        return issue.optInt(ISSUE_LINE);
    }

    @Override
    int parseEnd(final JSONObject issue) {
        if (issue.has(ISSUE_TEXT_RANGE)) {
            return issue.optJSONObject(ISSUE_TEXT_RANGE).optInt(ISSUE_TEXT_RANGE_END_LINE);
        }
        return issue.optInt(ISSUE_LINE);
    }
}
