package edu.hm.hafner.analysis.parser.dry.cpd;

import java.io.Serial;
import java.util.List;

import org.apache.commons.digester3.Digester;

import edu.hm.hafner.analysis.DuplicationGroup;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.parser.dry.AbstractDryParser;

/**
 * A parser for PMD's CPD XML files.
 *
 * @author Ullrich Hafner
 */
public class CpdParser extends AbstractDryParser<Duplication> {
    /** Unique ID of this class. */
    @Serial
    private static final long serialVersionUID = 6507147028628714706L;

    /**
     * Creates a new instance of {@link CpdParser}.
     *
     * @param highThreshold
     *         minimum number of duplicate lines for high priority warnings
     * @param normalThreshold
     *         minimum number of duplicate lines for normal priority warnings
     */
    public CpdParser(final int highThreshold, final int normalThreshold) {
        super(highThreshold, normalThreshold);
    }

    /**
     * Creates a new instance of {@link CpdParser}. The {@code highThreshold} is set to 50, the {@code normalThreshold}
     * is set to 25.
     */
    public CpdParser() {
        super(50, 25);
    }

    @Override
    protected void configureParser(final Digester digester) {
        String duplicationXPath = "*/pmd-cpd/duplication";
        digester.addObjectCreate(duplicationXPath, Duplication.class);
        digester.addSetProperties(duplicationXPath);
        digester.addCallMethod(duplicationXPath + "/codefragment", "setCodeFragment", 0);
        digester.addSetNext(duplicationXPath, "add");

        String fileXPath = duplicationXPath + "/file";
        digester.addObjectCreate(fileXPath, SourceFile.class);
        digester.addSetProperties(fileXPath);
        digester.addSetNext(fileXPath, "addFile", SourceFile.class.getName());
    }

    @Override
    protected Report convertDuplicationsToIssues(final List<Duplication> duplications, final IssueBuilder issueBuilder) {
        var report = new Report();

        for (Duplication duplication : duplications) {
            var group = new DuplicationGroup(duplication.getCodeFragment());
            for (SourceFile file : duplication.getFiles()) {
                issueBuilder.setSeverity(getPriority(duplication.getLines()))
                        .setLineStart(file.getLine())
                        .setLineEnd(file.getLine() + duplication.getLines() - 1)
                        .setFileName(file.getPath())
                        .setType("CPD")
                        .setAdditionalProperties(group);
                Issue issue = issueBuilder.build();
                group.add(issue);
                report.add(issue);
            }
        }
        return report;
    }
}
