package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.PhpMdParser;

/**
 * A descriptor for PHP Mess Detector (PHPMD).
 *
 * @author Akash Manna
 */
class PhpMdDescriptor extends ParserDescriptor {
    private static final String ID = "php-md";
    private static final String NAME = "PHP Mess Detector";

    PhpMdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.WARNING;
    }

    @Override
    public String getUrl() {
        return "https://phpmd.org/";
    }

    @Override
    public String getIconUrl() {
        return "https://phpmd.org/images/logo.png";
    }

    @Override
    public String getPattern() {
        return "**/phpmd-report.json";
    }

    @Override
    public IssueParser create(final Option... options) {
        return new PhpMdParser();
    }
}
