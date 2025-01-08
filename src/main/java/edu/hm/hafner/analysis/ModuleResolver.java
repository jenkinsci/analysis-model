package edu.hm.hafner.analysis;

import java.util.List;

/**
 * Resolves module names by reading and mapping module definitions (build.xml, pom.xml, or Manifest.mf files).
 *
 * @author Ullrich Hafner
 */
public class ModuleResolver {
    private final ModuleDetectorRunner runner;

    /**
     * Creates a new instance of {@link ModuleResolver}.
     *
     * @param runner the module runner to use
     */
    public ModuleResolver(final ModuleDetectorRunner runner) {
        this.runner = runner;
    }

    /**
     * Resolves the absolute paths of all affected files referenced by the specified report.
     *
     * @param report
     *         the issues to resolve the paths for
     */
    public void run(final Report report) {
        List<Issue> issuesWithoutModule = report.stream()
                .filter(issue -> !issue.hasModuleName())
                .toList();

        if (issuesWithoutModule.isEmpty()) {
            report.logInfo("-> all issues already have a valid module name");

            return;
        }

        issuesWithoutModule.forEach(issue -> issue.setModuleName(runner.guessModuleName(issue.getAbsolutePath())));
        report.logInfo("-> resolved module names for %d issues", issuesWithoutModule.size());
    }
}
