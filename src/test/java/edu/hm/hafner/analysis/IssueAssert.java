package edu.hm.hafner.analysis;

import org.assertj.core.api.AbstractAssert;

import java.util.UUID;


public class IssueAssert extends AbstractAssert<IssueAssert,Issue> {


    IssueAssert hasColumnEndt(int columnEnd){
        if(actual.getColumnEnd() != (columnEnd))
            failWithMessage("actual columnEnd is: "+actual.getColumnStart()+  " buts shout be "+columnEnd);
        return this;
    }


    IssueAssert hasColumnStart(int columnStart){
        if(actual.getColumnStart() != (columnStart))
            failWithMessage("actual columnStart is: "+actual.getColumnStart()+  " buts shout be "+columnStart);
        return this;
    }

    IssueAssert hasLineEnd(int lineEnd){
        if(actual.getLineEnd() != (lineEnd))
            failWithMessage("actual lineEnd is: "+actual.getLineStart()+  " buts shout be "+lineEnd);
        return this;
    }

    IssueAssert haslineStart(int lineStart){
        if(actual.getLineStart() != (lineStart))
            failWithMessage("actual lineStart is: "+actual.getLineStart()+  " buts shout be "+lineStart);
        return this;
    }

    IssueAssert hasPackageName(String packageName){
        isNotNull();
        if(!actual.getPackageName().equals(packageName))
            failWithMessage("actualDescription is: "+actual.getPackageName()+  " buts shout be "+packageName);
        return this;
    }

    IssueAssert hasDescription(String description){
        isNotNull();
        if(!actual.getDescription().equals(description))
            failWithMessage("actualDescription is: "+actual.getDescription()+  " buts shout be "+description);
        return this;
    }


    IssueAssert hasMessage(String message){
        isNotNull();
        if(!actual.getMessage().equals(message))
            failWithMessage("actual type is: "+actual.getMessage()+  " buts shout be "+message);
        return this;
    }


    IssueAssert hasPriority(Priority priority){
        isNotNull();
        if(!actual.getPriority().equals(priority))
            failWithMessage("actual Priority is: "+actual.getPriority()+  " buts shout be "+priority);
        return this;
    }

    IssueAssert hasType(String type){
        isNotNull();
        if(!actual.getType().equals(type))
            failWithMessage("actual type is: "+actual.getType()+  " buts shout be "+type);
        return this;
    }

    IssueAssert hasCategory(String category){
        isNotNull();
        if(!actual.getCategory().equals(category))
            failWithMessage("actual category is: "+actual.getCategory()+  " buts shout be "+category);
        return this;
    }


    IssueAssert hasFileName(String name){
        isNotNull();
        if(!actual.getFileName().equals(name))
            failWithMessage("actual Filename: "+actual.getFileName()+  "shout be "+name);
        return this;
    }
    IssueAssert containsFileName(String name){
        isNotNull();
        if(!actual.getFileName().contains(name))
            failWithMessage("actual Filename: "+actual.getFileName()+  "shout contain "+name);
        return this;
    }


    static IssueAssert assertThat(Issue actual){
       IssueAssert i = new IssueAssert(actual,IssueAssert.class);
        return i;
    }

    public IssueAssert(Issue issue, Class<?> selfType) {
        super(issue, selfType);
    }
}
