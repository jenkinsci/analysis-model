package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.parser.DependencyTrackParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for the OWASP Dependency-Track vulnerability findings parser.
 *
 * @author Akash Manna
 * @see <a href="https://dependencytrack.org/">OWASP Dependency-Track</a>
 */
class DependencyTrackDescriptor extends ParserDescriptor {
    private static final String ID = "dependency-track";
    private static final String NAME = "Dependency-Track";

    DependencyTrackDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueType getType() {
        return IssueType.VULNERABILITY;
    }

    @Override
    protected IssueParser create(final Option... options) {
        return new DependencyTrackParser();
    }

    @Override
    public String getPattern() {
        return "**/dependency-track-findings.json";
    }

    @Override
    public String getHelp() {
        return join(
                text("Export findings from the Dependency-Track REST API using:"),
                br(),
                code("GET /api/v1/finding/project/{uuid}/export"),
                text(", then save the response to a file."),
                text("See"),
                a("Dependency-Track File Formats")
                        .withHref("https://docs.dependencytrack.org/integrations/file-formats/"),
                text("for details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://dependencytrack.org/";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/DependencyTrack/branding/main/dt-icon.svg";
    }
}