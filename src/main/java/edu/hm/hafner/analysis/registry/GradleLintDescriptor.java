package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.GradleLintParser;

/**
 * A descriptor for Gradle Lint (nebula.lint) JSON reports.
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
    public IssueParser create(final Option... options) {
        return new GradleLintParser();
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }
  
    @Override
    public String getPattern() {
        return "**/gradle-lint-report.json";
    }

    @Override
    public String getHelp() {
        return "Configure <code>gradleLint { reportFormat = 'json' }</code> in your <code>build.gradle</code>"
                + " and run <code>./gradlew generateGradleLintReport</code> to generate a JSON report.<br/>"
                + "See <a href='https://github.com/nebula-plugins/gradle-lint-plugin'>"
                + "Gradle Lint Plugin</a> for usage details.";
    }

    @Override
    public String getUrl() {
        return "https://github.com/nebula-plugins/gradle-lint-plugin";
    }
}
