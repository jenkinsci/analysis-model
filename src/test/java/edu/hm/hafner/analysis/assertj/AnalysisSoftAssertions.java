package edu.hm.hafner.analysis.assertj;

import org.assertj.core.api.SoftAssertions;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

/**
 * Created by Sarah on 19.10.2017.
 */
public class AnalysisSoftAssertions extends SoftAssertions {

    public IssuesAssert assertThat (Issues actual){
        return proxy(IssuesAssert.class, Issues.class, actual);
    }

    public IssueAssert assertThat (Issue actual){
        return proxy(IssueAssert.class, Issue.class, actual);
    }
}
