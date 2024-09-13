package edu.hm.hafner.analysis.parser.dry.simian;

import java.util.List;

import org.apache.commons.digester3.Digester;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.parser.dry.AbstractDryParser;

/**
 * A parser for Simian XML files.
 *
 * @author Ullrich Hafner
 */
public class SimianParser extends AbstractDryParser<Set> {
    /** Unique ID of this class. */
    private static final long serialVersionUID = 6507147028628714706L;

    /**
     * Creates a new instance of {@link SimianParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    public SimianParser(final int highThreshold, final int normalThreshold) {
        super(highThreshold, normalThreshold);
    }

    /**
     * Creates a new instance of {@link SimianParser}. The {@code highThreshold} is set to 50, the {@code normalThreshold}
     * is set to 25.
     */
    public SimianParser() {
        super(50, 25);
    }

    @Override
    protected void configureParser(final Digester digester) {
        String duplicationXPath = "*/simian/check/set";
        digester.addObjectCreate(duplicationXPath, Set.class);
        digester.addSetProperties(duplicationXPath);
        digester.addSetNext(duplicationXPath, "add");

        String fileXPath = duplicationXPath + "/block";
        digester.addObjectCreate(fileXPath, Block.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addBlock", Block.class.getName());
    }

    @Override
    protected Report convertDuplicationsToIssues(final List<Set> duplications, final IssueBuilder issueBuilder) {
        var report = new Report();

        for (Set duplication : duplications) {
            var group = new DuplicationGroup();
            for (Block file : duplication.getBlocks()) {
                issueBuilder.setSeverity(getPriority(duplication.getLineCount()))
                        .setLineStart(file.getStartLineNumber())
                        .setLineEnd(file.getEndLineNumber())
                        .setFileName(file.getSourceFile())
                        .setAdditionalProperties(group)
                        .setType("Simian");
                Issue issue = issueBuilder.build();
                group.add(issue);
                report.add(issue);
            }
        }
        return report;
    }
}
