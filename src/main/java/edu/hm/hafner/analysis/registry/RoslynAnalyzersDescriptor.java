package edu.hm.hafner.analysis.registry;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.parser.violations.RoslynAnalyzersAdapter;

/**
 * A descriptor for Roslyn Analyzers SARIF reports.
 *
 * @author Akash Manna
 */
class RoslynAnalyzersDescriptor extends ParserDescriptor {
    private static final String ID = "roslyn-analyzers";
    private static final String NAME = "Roslyn Analyzers";

    RoslynAnalyzersDescriptor() {
        super(ID, NAME);
    }

    @Override
    public IssueParser create(final Option... options) {
        return new RoslynAnalyzersAdapter();
    }

    @Override
    public String getPattern() {
        return "**/roslyn-analyzers-report.sarif";
    }

    @Override
    public String getHelp() {
        return "Use command line option <code>/errorlog:roslyn-analyzers-report.sarif</code> "
                + "with <code>dotnet build</code> to generate a Roslyn analyzer SARIF report.";
    }

    @Override
    public String getUrl() {
        return "https://learn.microsoft.com/dotnet/csharp/language-reference/compiler-options/errors-warnings#errorlog";
    }
}
