package edu.hm.hafner.analysis.assertj;

import java.util.UUID;

import org.assertj.core.api.AbstractAssert;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;

public class IssueAssert extends AbstractAssert<IssueAssert, Issue> {
    public IssueAssert(Issue actual) {
        super(actual, IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    public IssueAssert hasFileName(String filename){
        isNotNull();
        if(actual.getFileName() != filename){
            failWithMessage("Expected message to be <%s> but was <%s>",
                    filename, actual.getFileName());
        }
        return this;
    }

    public IssueAssert hasMessage(String message) {
        isNotNull();
        if(actual.getMessage() != message){
            failWithMessage("Expected message to be <%s> but was <%s>",
                    message, actual.getMessage());
        }
        return this;
    }

    public IssueAssert hasCategory(String category){
        isNotNull();
        if(actual.getCategory() != category){
            failWithMessage("Expected category to be <%s> but was <%s>",
                    category, actual.getCategory());
        }
        return this;
    }

    public IssueAssert hasPriority(Priority priority){
        isNotNull();
        if(actual.getPriority() != priority){
            failWithMessage("Expected priority to be <%s> but was <%s>",
                    priority.toString(), actual.getPriority().toString());
        }
        return this;
    }

    public IssueAssert hasDescription(String description){
        isNotNull();
        if(actual.getDescription() != description){
            failWithMessage("Expected description to be <%s> but was <%s>",
                    description, actual.getDescription());
        }
        return this;
    }

    public IssueAssert hasPackageName (String packageName){
        isNotNull();
        if(actual.getPackageName() != packageName){
            failWithMessage("Expected package name to be <%s> but was <%s>",
                    packageName, actual.getPackageName());
        }
        return this;
    }

    public IssueAssert hasLineStart(int lineNumber){
        isNotNull();
        if(actual.getLineStart()!=lineNumber){
            failWithMessage("Expected first line number to be <%d> but was <%d>",
                    lineNumber, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasLineEnd(int lineNumber){
        isNotNull();
        if(actual.getLineEnd() != lineNumber){
            failWithMessage("Expected last line number to be <%d> but was <%d>",
                    lineNumber, actual.getLineEnd());
        }
        return this;
    }

    public IssueAssert hasColumnStart(int columnStart){
        isNotNull();
        if(actual.getColumnStart()!= columnStart){
            failWithMessage("Expected columnStart to be <%d> but was <%d>",
                    columnStart, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasColumnEnd(int columnEnd){
        isNotNull();
        if(actual.getLineEnd() != columnEnd){
            failWithMessage("Expected columnEnd to be <%d> but was <%d>",
                    columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    public IssueAssert hasUuid (UUID uuid){
        isNotNull();
        if(actual.getId() != uuid){
            failWithMessage("Expected id to be <%d> but was <%d>",
                    uuid, actual.getId());
        }
        return this;
    }

    public IssueAssert hasFingerprint(String fingerprint){
        isNotNull();
        if(actual.getFingerprint() != fingerprint){
            failWithMessage("Expected fingerprint to be <%s> but was <%s>",
                    fingerprint, actual.getFingerprint());
        }
        return this;
    }
}
