package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.GradleLintParser;

/**
 * A descriptor for Gradle Lint JSON reports.
 *
 * @author Akash Manna
 */
class GradleLintDescriptor extends ParserDescriptor {
    private static final String ID = "gradle-lint";
    private static final String NAME = "Gradle Lint";

    GradleLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    public IssueParser create(final Option... options) {
        return new GradleLintParser();
    }

    @Override
    public String getPattern() {
        return "**/gradle-lint.json";
    }

    @Override
    public String getHelp() {
        return "Use a custom Gradle lint plugin to generate a JSON report.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/nebula-plugins/gradle-lint-plugin";
    }
}
