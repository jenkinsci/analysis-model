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
            failWithMessage("\nExpecting fileName of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, filename, actual.getFileName());

        }
        return this;
    }

    public IssueAssert hasMessage(String message) {
        isNotNull();
        if(actual.getMessage() != message){
            failWithMessage("\nExpected message of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, message, actual.getMessage());
        }
        return this;
    }

    public IssueAssert hasCategory(String category){
        isNotNull();
        if(actual.getCategory() != category){
            failWithMessage("\nExpected category of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, category, actual.getCategory());
        }
        return this;
    }

    public IssueAssert hasPriority(Priority priority){
        isNotNull();
        if(actual.getPriority() != priority){
            failWithMessage("\nExpected priority of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, priority.toString(), actual.getPriority().toString());
        }
        return this;
    }

    public IssueAssert hasDescription(String description){
        isNotNull();
        if(actual.getDescription() != description){
            failWithMessage("\nExpected description of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, description, actual.getDescription());
        }
        return this;
    }

    public IssueAssert hasPackageName (String packageName){
        isNotNull();
        if(actual.getPackageName() != packageName){
            failWithMessage("\nExpected package name of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, packageName, actual.getPackageName());
        }
        return this;
    }

    public IssueAssert hasLineStart(int lineNumber){
        isNotNull();
        if(actual.getLineStart()!=lineNumber){
            failWithMessage("\nExpected first line number of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, lineNumber, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasLineEnd(int lineNumber){
        isNotNull();
        if(actual.getLineEnd() != lineNumber){
            failWithMessage("\nExpected last line number of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, lineNumber, actual.getLineEnd());
        }
        return this;
    }

    public IssueAssert hasColumnStart(int columnStart){
        isNotNull();
        if(actual.getColumnStart()!= columnStart){
            failWithMessage("\nExpected columnStart of:\n <%s>\nto be:\n <%d>\nbut was: \n<%d>",
                    actual, columnStart, actual.getLineStart());
        }
        return this;
    }

    public IssueAssert hasColumnEnd(int columnEnd){
        isNotNull();
        if(actual.getLineEnd() != columnEnd){
            failWithMessage("\nExpected columnEnd of:\n <%s>\nto be: \n<%d>\nbut was: \n<%d>",
                    actual, columnEnd, actual.getColumnEnd());
        }
        return this;
    }

    public IssueAssert hasUuid (UUID uuid){
        isNotNull();
        if(actual.getId() != uuid){
            failWithMessage("\nExpected id of:\n <%s>\nto be:\n <%d>\nbut was:\n <%d>",
                    actual, uuid, actual.getId());
        }
        return this;
    }

    public IssueAssert hasFingerprint(String fingerprint){
        isNotNull();
        if(actual.getFingerprint() != fingerprint){
            failWithMessage("\nExpected fingerprint of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, fingerprint, actual.getFingerprint());
        }
        return this;
    }

    public IssueAssert hasType(String type){
        isNotNull();
        if(actual.getType() != type){
            failWithMessage("\nExpected type of:\n <%s>\nto be:\n <%s>\nbut was:\n <%s>",
                    actual, type, actual.getType());
        }
        return this;
    }
}
