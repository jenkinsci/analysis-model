package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

public class IssueAssert extends AbstractAssert <IssueAssert,Issue>{
    public IssueAssert(final Issue issue, final Class<?> selfType) {
        super(issue, selfType);
    }
}
