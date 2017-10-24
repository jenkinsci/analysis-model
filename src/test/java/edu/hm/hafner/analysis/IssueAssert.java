package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.Objects;
import java.util.UUID;

/**
 * This class provides custom AssertJ assertion for {@link Issue}
 *
 * @author slausch
 */
public class IssueAssert extends AbstractAssert<IssueAssert,Issue> {

    public IssueAssert(Issue actual){
        super(actual,IssueAssert.class);
    }

    public static IssueAssert assertThat(Issue actual) {
        return new IssueAssert(actual);
    }

    /**
     * Checks whether an Issue has a specific filename.
     * @param filename - String specifying filename.
     * @return this
     */
    public IssueAssert hasFileName(String filename){
        isNotNull();

        if (!Objects.equals(actual.getFileName(), filename)) {
            failWithMessage("Expected filename to be <%s> but was <%s>", filename, actual.getFileName());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific category.
     * @param category - String specifying category.
     * @return this
     */
    public IssueAssert hasCategory(String category){
        isNotNull();

        if (!Objects.equals(actual.getCategory(), category)) {
            failWithMessage("Expected category to be <%s> but was <%s>", category, actual.getCategory());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific type.
     * @param type - String specifying type.
     * @return this
     */
    public IssueAssert hasType(String type){
        isNotNull();

        if(!Objects.equals(actual.getType(),type)) {
            failWithMessage("Expected type to be <%s> but was <%s>", type, actual.getType());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific priority.
     * @param priority - Priority specifying priority.
     * @return this
     */
    public IssueAssert hasPriority(Priority priority){
        isNotNull();

        if (!Objects.equals(actual.getPriority(),priority)){
            failWithMessage("Expected priority to be <%d> but was <%d>", priority, actual.getPriority());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific message.
     * @param message - String specifying message.
     * @return this
     */
    public IssueAssert hasMessage(String message){
        isNotNull();

        if(!Objects.equals(actual.getMessage(),message)){
            failWithMessage("Expected message to be <%s> but was <%s>",message,actual.getMessage());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific description.
     * @param description - String specifying description.
     * @return this
     */
    public IssueAssert hasDescription(String description){
        isNotNull();

        if(!Objects.equals(actual.getDescription(),description)){
            failWithMessage("Expected description to be <%s> but was <%s>",description,actual.getDescription());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific packageName.
     * @param packageName - String specifying packageName.
     * @return this
     */
    public IssueAssert hasPackageName(String packageName){
        isNotNull();

        if(!Objects.equals(actual.getPackageName(),packageName)){
            failWithMessage("Expected packageName to be <%s> but was <%s>",packageName,actual.getPackageName());
        }
        return this;
    }
    /**
     * Checks whether an Issue starts at a specific line.
     * @param lineStart - int specifying lineStart.
     * @return this
     */
    public IssueAssert hasLineStart(int lineStart){
        isNotNull();

        if(actual.getLineStart()!=lineStart){
            failWithMessage("Expected lineStart to be <%d> but was <%d>",lineStart,actual.getLineStart());
        }
        return this;
    }
    /**
     * Checks whether an Issue ends at a specific line.
     * @param lineEnd - int specifying lineEnd.
     * @return this
     */
    public IssueAssert hasLineEnd(int lineEnd){
        isNotNull();

        if(actual.getLineEnd() != lineEnd){
            failWithMessage("Expected lineEnd to be <%d> but was <%d>",lineEnd,actual.getLineEnd());
        }
        return this;
    }
    /**
     * Checks whether an Issue starts at a specific column.
     * @param columnStart - int specifying columnStart.
     * @return this
     */
    public IssueAssert hasColumnStart(int columnStart){
        isNotNull();

        if(actual.getColumnStart()!=columnStart){
            failWithMessage("Expected columnStart to be <%d> but was <%d>",columnStart,actual.getColumnStart());
        }
        return this;
    }
    /**
     * Checks whether an Issue ends at a specific column.
     * @param columnEnd - int specifying columnEnd.
     * @return this
     */
    public IssueAssert hasColumnEnd(int columnEnd){
        isNotNull();

        if(actual.getColumnEnd()!=columnEnd){
            failWithMessage("Expected columnEnd to be <%d> but was <%d>",columnEnd,actual.getColumnEnd());
        }
        return this;
    }
    /**
     * Checks whether an Issue has a specific UUID.
     * @param UUID - UUID specifying UUID.
     * @return this
     */
    public IssueAssert hasUUID(UUID UUID){
        isNotNull();

        if(!Objects.equals(actual.getId(),UUID)){
            failWithMessage("Expected UUID to be <%d> but was <%d>",UUID,actual.getId());
        }

        return this;
    }
    /**
     * Checks whether an Issue has a specific fingerprint.
     * @param fingerprint - String specifying fingerprint.
     * @return this
     */
    public IssueAssert hasFingerprint(String fingerprint){
        isNotNull();

        if(!Objects.equals(actual.getFingerprint(),fingerprint)){
            failWithMessage("Expected fingerprint to be <%s> but was <%s>",fingerprint,actual.getFingerprint());
        }
        return this;
    }
}
