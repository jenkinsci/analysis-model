package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;

import org.assertj.core.api.SoftAssertions;
/**
 * *****************************************************************
 * Hochschule Muenchen Fakultaet 07 (Informatik).		**
 * Autor: Sebastian Balz
 * Datum 16.10.2017
 *  Software Win 7 JDK8 Win 10 JDK8 Ubuntu 15.4 OpenJDK7	**
 * edu.hm.hafner.analysis
 *
 */
class IssueSoftAssert extends SoftAssertions {

    /**
     * assert.
     * @param actual that
     * @return this
     */
    IssueAssert assertThat(Issue actual) {
        return proxy(IssueAssert.class, Issue.class, actual);
    }
}
