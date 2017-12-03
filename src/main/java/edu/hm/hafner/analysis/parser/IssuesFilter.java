package edu.hm.hafner.analysis.parser;

import java.util.regex.Pattern;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;

public class IssuesFilter {
    private boolean includeFilterSet;
    private boolean excludeFilterSet;
    private Issues filteredIssues;
    private Pattern patternIncludeFilename;
    private Pattern patternIncludeCategory;
    private Pattern patternIncludePriority;
    private Pattern patternIncludeMessage;
    private Pattern patternIncludeDescription;
    private Pattern patternIncludeType;
    private Pattern patternIncludeColumnStart;
    private Pattern patternIncludeColumnEnd;
    private Pattern patternExcludeFilename;
    private Pattern patternExcludeCategory;
    private Pattern patternExcludePriority;
    private Pattern patternExcludeMessage;
    private Pattern patternExcludeDescription;
    private Pattern patternExcludeType;
    private Pattern patternExcludeColumnStart;
    private Pattern patternExcludeColumnEnd;


    public IssuesFilter(Issues issues){
        includeFilterSet = false;
        excludeFilterSet = false;
        filteredIssues = issues;
        patternIncludeFilename = Pattern.compile("^$");
        patternIncludeCategory = Pattern.compile("^$");
        patternIncludePriority = Pattern.compile("^$");
        patternIncludeMessage = Pattern.compile("^$");
        patternIncludeDescription = Pattern.compile("^$");
        patternIncludeType = Pattern.compile("^$");
        patternIncludeColumnStart = Pattern.compile("^$");
        patternIncludeColumnEnd = Pattern.compile("^$");
        patternExcludeFilename = Pattern.compile("^$");
        patternExcludeCategory = Pattern.compile("^$");
        patternExcludePriority = Pattern.compile("^$");
        patternExcludeMessage = Pattern.compile("^$");
        patternExcludeDescription = Pattern.compile("^$");
        patternExcludeType = Pattern.compile("^$");
        patternExcludeColumnStart = Pattern.compile("^$");
        patternExcludeColumnEnd = Pattern.compile("^$");
    }

    public IssuesFilter includeFilename(String filenameRegEx){
        includeFilterSet = true;
        patternIncludeFilename = Pattern.compile(filenameRegEx);
        return this;
    }

    public IssuesFilter includeCategory(String categoryRegEx){
        includeFilterSet = true;
        patternIncludeCategory = Pattern.compile(categoryRegEx);
        return this;
    }

    public IssuesFilter includePriority(String priorityRegEx){
        includeFilterSet = true;
        patternIncludePriority = Pattern.compile(priorityRegEx);
        return this;
    }

    public IssuesFilter includeMessage(String messageRegEx){
        includeFilterSet = true;
        patternIncludeMessage = Pattern.compile(messageRegEx);
        return this;
    }

    public IssuesFilter includeDescription (String descriptionRegEx){
        includeFilterSet = true;
        patternIncludeDescription = Pattern.compile(descriptionRegEx);
        return this;
    }

    public IssuesFilter includeType (String typeRegEx){
        includeFilterSet = true;
        patternIncludeType = Pattern.compile(typeRegEx);
        return this;
    }

    public IssuesFilter includeColumnStart (String columnStartRegEx){
        includeFilterSet = true;
        patternIncludeColumnStart = Pattern.compile(columnStartRegEx);
        return this;
    }

    public IssuesFilter includeColumnEnd (String columnEndRegEx){
        includeFilterSet = true;
        patternIncludeColumnStart = Pattern.compile(columnEndRegEx);
        return this;
    }

    public IssuesFilter excludeFilename(String filenameRegEx){
        excludeFilterSet = true;
        patternExcludeFilename = Pattern.compile(filenameRegEx);
        return this;
    }

    public IssuesFilter excludeCategory(String categoryRegEx){
        excludeFilterSet = true;
        patternExcludeCategory = Pattern.compile(categoryRegEx);
        return this;
    }

    public IssuesFilter excludePriority(String priorityRegEx){
        excludeFilterSet = true;
        patternExcludePriority = Pattern.compile(priorityRegEx);
        return this;
    }

    public IssuesFilter excludeMessage(String messageRegEx){
        excludeFilterSet = true;
        patternExcludeMessage = Pattern.compile(messageRegEx);
        return this;
    }

    public IssuesFilter excludeDescription (String descriptionRegEx){
        excludeFilterSet = true;
        patternExcludeDescription = Pattern.compile(descriptionRegEx);
        return this;
    }

    public IssuesFilter excludeType (String typeRegEx){
        excludeFilterSet = true;
        patternExcludeType = Pattern.compile(typeRegEx);
        return this;
    }

    public IssuesFilter excludeColumnStart (String columnStartRegEx){
        excludeFilterSet = true;
        patternExcludeColumnStart = Pattern.compile(columnStartRegEx);
        return this;
    }

    public IssuesFilter excludeColumnEnd (String columnEndRegEx){
        excludeFilterSet = true;
        patternExcludeColumnStart = Pattern.compile(columnEndRegEx);
        return this;
    }

    public Issues filter(){
        Issues includedIssues = new Issues();
        Issues completeFilteredIssues = new Issues();

        if (includeFilterSet == true) {
            for (Issue issue : filteredIssues) {
                if (patternIncludeFilename.matcher(issue.getFileName()).find() ||
                        patternIncludeCategory.matcher(issue.getCategory()).find() ||
                        patternIncludePriority.matcher(issue.getPriority().toString()).find() ||
                        patternIncludeMessage.matcher(issue.getMessage()).find() ||
                        patternIncludeDescription.matcher(issue.getDescription()).find() ||
                        patternIncludeType.matcher(issue.getType()).find() ||
                        patternIncludeColumnStart.matcher(Integer.toString(issue.getColumnStart())).find() ||
                        patternIncludeColumnEnd.matcher(Integer.toString(issue.getColumnEnd())).find()) {
                    includedIssues.add(issue);
                }
            }
        }
        else {
            includedIssues = filteredIssues;
        }
        if (excludeFilterSet){
            for (Issue issue : includedIssues){
                if (!patternExcludeFilename.matcher(issue.getFileName()).find() &&
                        !patternExcludeCategory.matcher(issue.getCategory()).find() &&
                        !patternExcludePriority.matcher(issue.getPriority().toString()).find() &&
                        !patternExcludeMessage.matcher(issue.getMessage()).find() &&
                        !patternExcludeDescription.matcher(issue.getDescription()).find() &&
                        !patternExcludeType.matcher (issue.getType()).find() &&
                        !patternExcludeColumnStart.matcher (Integer.toString(issue.getColumnStart())).find() &&
                        !patternExcludeColumnEnd.matcher (Integer.toString(issue.getColumnEnd())).find()){
                    completeFilteredIssues.add(issue);
                }
            }
        }
        else{
            completeFilteredIssues = includedIssues;
        }
        return completeFilteredIssues;
    }
}
