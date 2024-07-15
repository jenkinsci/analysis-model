package edu.hm.hafner.analysis.registry;

import static j2html.TagCreator.a;
import static j2html.TagCreator.code;
import static j2html.TagCreator.join;
import static j2html.TagCreator.text;

import java.util.Collection;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.ProtoLintJsonParser;
import edu.hm.hafner.analysis.parser.ProtoLintParser;

/**
 * A descriptor for ProtoLint.
 *
 * <p>We use a composite parser for supporting JSON (preferred) and plaintext (fallback) format.
 *
 * @author Lorenz Munsch
 * @author github@profhenry.de
 */
class ProtoLintDescriptor extends CompositeParserDescriptor {
    private static final String ID = "protolint";
    private static final String NAME = "ProtoLint";

    ProtoLintDescriptor() {
        super(ID, NAME);
    }

    @Override
    protected Collection<? extends IssueParser> createParsers() {
        return asList(new ProtoLintJsonParser(), new ProtoLintParser());
    }

    @Override
    public String getHelp() {
        return join(text("Use protolint with options"),
                code("-reporter=json -output_file=protolint-report.json"),
                text(", see"),
                a("protoLint CLI options").withHref("https://github.com/yoheimuta/protolint?tab=readme-ov-file#usage"),
                text("for usage details.")).render();
    }

    @Override
    public String getUrl() {
        return "https://github.com/yoheimuta/protolint";
    }
}
