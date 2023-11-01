package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.VeraCodePipelineScannerParser;

import static j2html.TagCreator.*;

/**
 * A descriptor for Veracode Pipeline Scanner.
 *
 * @author Juri Duval
 */
public class VeraCodePipelineScannerDescriptor extends ParserDescriptor {
    private static final String ID = "veracode-pipeline-scanner";
    private static final String NAME = "Veracode Pipeline Scanner";

    public VeraCodePipelineScannerDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser(final Option... options) {
        return new VeraCodePipelineScannerParser();
    }

    @Override
    public String getHelp() {
        return join(text("Use commandline"),
                code("java -jar pipeline-scan.jar --json_output=true --json_output_file=results.json"),
                text(", see"),
                a("Veracode Pipeline Scanner").withHref("https://docs.veracode.com/r/c_about_pipeline_scan"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://docs.veracode.com/r/c_about_pipeline_scan";
    }

    @Override
    public String getIconUrl() {
        return "https://raw.githubusercontent.com/jenkinsci/veracode-scan-plugin/master/src/main/webapp/icons/veracode-48x48.png";
    }
}
