package edu.hm.hafner;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import org.assertj.core.api.AbstractAssert;


public class IssueAssert extends AbstractAssert<IssueAssert,Issue> {


    /**
     * Test columnEnd.
     */
    IssueAssert hasColumnEnd(int columnEnd){
        if(actual.getColumnEnd() != (columnEnd))
            failWithMessage("actual columnEnd is: "+actual.getColumnStart()+  " buts shout be "+columnEnd);
        return this;
    }

    /**
     * Test columnStart.
     */
    IssueAssert hasColumnStart(int columnStart){
        if(actual.getColumnStart() != (columnStart))
            failWithMessage("actual columnStart is: "+actual.getColumnStart()+  " buts shout be "+columnStart);
        return this;
    }
    /**
     * Test lineEnd.
     */
    IssueAssert hasLineEnd(int lineEnd){
        if(actual.getLineEnd() != (lineEnd))
            failWithMessage("actual lineEnd is: "+actual.getLineEnd()+  " buts shout be "+lineEnd);
        return this;
    }
    /**
     * Test lineStart.
     */
    IssueAssert haslineStart(int lineStart){
        if(actual.getLineStart() != (lineStart))
            failWithMessage("actual lineStart is: "+actual.getLineStart()+  " buts shout be "+lineStart);
        return this;
    }
    /**
     * Test packageName.
     */
    IssueAssert hasPackageName(String packageName){
        isNotNull();
        if(!actual.getPackageName().equals(packageName))
            failWithMessage("actualDescription is: "+actual.getPackageName()+  " buts shout be "+packageName);
        return this;
    }
    /**
     * Test description.
     */
    IssueAssert hasDescription(String description){
        isNotNull();
        if(!actual.getDescription().equals(description))
            failWithMessage("actualDescription is: "+actual.getDescription()+  " buts shout be "+description);
        return this;
    }

    /**
     * Test message.
     */
    IssueAssert hasMessage(String message){
        isNotNull();
        if(!actual.getMessage().equals(message))
            failWithMessage("actual type is: "+actual.getMessage()+  " buts shout be "+message);
        return this;
    }

    /**
     * Test priority.
     */
    IssueAssert hasPriority(Priority priority){
        isNotNull();
        if(!actual.getPriority().equals(priority))
            failWithMessage("actual Priority is: "+actual.getPriority()+  " buts shout be "+priority);
        return this;
    }
    /**
     * Test type.
     */
    IssueAssert hasType(String type){
        isNotNull();
        if(!actual.getType().equals(type))
            failWithMessage("actual type is: "+actual.getType()+  " buts shout be "+type);
        return this;
    }
    /**
     * Test category.
     */
    IssueAssert hasCategory(String category){
        isNotNull();
        if(!actual.getCategory().equals(category))
            failWithMessage("actual category is: "+actual.getCategory()+  " buts shout be "+category);
        return this;
    }

    /**
     * Test FileName.
     */
    IssueAssert hasFileName(String name){
        isNotNull();
        if(!actual.getFileName().equals(name))
            failWithMessage("actual Filename: "+actual.getFileName()+  "shout be "+name);
        return this;
    }
    /**
     * Test FileName.
     */
    IssueAssert containsFileName(String name){
        isNotNull();
        if(!actual.getFileName().contains(name))
            failWithMessage("actual Filename: "+actual.getFileName()+  "shout contain "+name);
        return this;
    }

    /**
     * Test Issue.
     */
    static IssueAssert assertThat(Issue actual){
       IssueAssert i = new IssueAssert(actual,IssueAssert.class);
        return i;
    }

    public IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
    }
    public IssueAssert(final Issue i){super(i,IssueAssert.class);}
}
