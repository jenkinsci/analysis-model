package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.DetectifyParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Detectify vulnerability reports.
 *
 * @author Akash Manna
 */
class DetectifyDescriptor extends ParserDescriptor {
    private static final String ID = "detectify";
    private static final String NAME = "Detectify";

    DetectifyDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    public String getHelp() {
        return join(
                text("Use the Detectify API to export vulnerabilities in JSON format from "),
                code("GET /vulnerabilities/"),
                text(". See the Detectify API documentation for details."),
                ul().with(
                        li(a("Detectify API").withHref("https://developer.detectify.com/v2")),
                        li(a("Results").withHref("https://docs.detectify.com/web-application-security-testing/results"))))
                .render();
    }

    @Override
    public String getUrl() {
        return "https://developer.detectify.com/v2";
    }

    @Override
    public String getPattern() {
        return "**/detectify-report.json";
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new DetectifyParser();
    }
}