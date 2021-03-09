package edu.hm.hafner.analysis.registry;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.dry.cpd.CpdParser;
import edu.umd.cs.findbugs.annotations.CheckForNull;

import j2html.tags.UnescapedText;

import static j2html.TagCreator.*;

/**
 * A descriptor for the CPD parser.
 *
 * @author Lorenz Munsch
 */
class CpdDescriptor extends ParserDescriptor {
    private static final String ID = "cpd";
    private static final String NAME = "CPD";

    CpdDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser createParser() {
        return new CpdParser();
    }

    @Override
    public String getPattern() {
        return "**/cpd.xml";
    }

    @Override
    public String getUrl() {
        return "https://pmd.github.io/latest/pmd_userdocs_cpd.html";
    }

    @Override
    public String getDescription(final Issue issue) {
        return getDuplicateCode(issue.getAdditionalProperties());
    }

    static String getDuplicateCode(@CheckForNull final Serializable properties) {
        if (properties instanceof DuplicationGroup) {
            return pre().with(new UnescapedText(getCodeFragment((DuplicationGroup) properties))).renderFormatted();
        }
        else {
            return StringUtils.EMPTY;
        }
    }

    private static String getCodeFragment(final DuplicationGroup duplicationGroup) {
        return code(duplicationGroup.getCodeFragment()).renderFormatted();
    }
}
