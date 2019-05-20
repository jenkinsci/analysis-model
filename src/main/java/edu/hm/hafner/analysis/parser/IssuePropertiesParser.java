package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;

/**
 * Provides the {@link Issue} properties as String constants.
 *
 * @author Ullrich Hafner
 */
abstract class IssuePropertiesParser extends IssueParser {
    private static final long serialVersionUID = -7627882904619620908L;

    static final String ADDITIONAL_PROPERTIES = "additionalProperties";
    static final String CATEGORY = "category";
    static final String COLUMN_END = "columnEnd";
    static final String COLUMN_START = "columnStart";
    static final String DESCRIPTION = "description";
    static final String DIRECTORY = "directory";
    static final String FILE_NAME = "fileName";
    static final String FINGERPRINT = "fingerprint";
    static final String ID = "id";
    static final String LINE_END = "lineEnd";
    static final String LINE_RANGES = "lineRanges";
    static final String LINE_RANGE_END = "end";
    static final String LINE_RANGE_START = "start";
    static final String LINE_START = "lineStart";
    static final String MESSAGE = "message";
    static final String MODULE_NAME = "moduleName";
    static final String ORIGIN = "origin";
    static final String PACKAGE_NAME = "packageName";
    static final String REFERENCE = "reference";
    static final String SEVERITY = "severity";
    static final String TYPE = "type";
}
