package edu.hm.hafner.analysis;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

public class IssueAssert extends AbstractAssert <IssueAssert,Issue>{

  /*  public IssueAssert(final Issue issue, final Class<?> selfType) {
        super(issue, selfType);
    }*/

    public IssueAssert(Issue actual){
        super(actual,IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual){
        return  new IssueAssert(actual);
    }

    public IssueAssert hasFileName(String fileName){
        isNotNull();

        if (!Objects.equals(actual.getFileName(), fileName)) {
            failWithMessage("Expected file Name to be <%s> but was <%s>", fileName, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasCategory(String category){
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected Category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }

}
