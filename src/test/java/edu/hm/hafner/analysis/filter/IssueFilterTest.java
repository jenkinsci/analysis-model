package edu.hm.hafner.analysis.filter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Streams;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.assertj.IssueAssert;
import edu.hm.hafner.analysis.assertj.IssuesAssert;
import static org.assertj.core.api.Assertions.assertThat;


public class IssueFilterTest {

    private Issues issues = new Issues(){
        {
            add(getMyIssue());
            add(getYourIssue());
            add(getHisIssue());
        }
    };
    private Issue myIssue;
    private Issue getMyIssue(){
        if(myIssue == null)
         myIssue = generateIssueWithPrefix("My1");
        return myIssue;
    }
    private Issue yourIssue;
    private Issue getYourIssue(){
        if(yourIssue == null)
            yourIssue = generateIssueWithPrefix("Your2");
        return yourIssue;
    }
    private Issue hisIssue;
    private Issue getHisIssue(){
        if(hisIssue == null)
            hisIssue = generateIssueWithPrefix("His3");
        return hisIssue;
    }
    private Issue generateIssueWithPrefix(String prefix){
        return new IssueBuilder()
               .setFileName(prefix+"FileName")
               .setPackageName(prefix+"PackageName")
               .setModuleName(prefix+"ModuleName")
               .setCategory(prefix+"CategoryName")
               .setType(prefix+"Type")
               .build();
    }

    @Test
    void shouldNothingChangeWhenNoFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, this.issues);
    }

    @Test
    void shouldPassAllWhenUselessFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .setFilenameIncludeFilter("[a-zA-Z1]*")
                .setFilenameIncludeFilter("[a-zA-Z2]*")
                .setFilenameIncludeFilter("[a-zA-Z3]*")
                .setFilenameIncludeFilter("[a-zA-Z4]*")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, this.issues);
    }
    @Test
    void shouldPassNoWhenMasterFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .setFilenameExcludeFilter("[a-zA-Z_1-3]*")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues);
    }


    @Test
    void shouldFindMyIssueByAOneTooOneFileNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                            .setFilenameIncludeFilter("My1FileName")
                            .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getMyIssue());
    }
    @Test
    void shouldFindMyIssueByAOneTooOneFileNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setFilenameExcludeFilter("My1FileName")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getYourIssue(), getHisIssue());
    }

    @Test
    void shouldFindYourIssueByAOneTooOnePackageNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setPackageNameIncludeFilter("Your2PackageName")
                .createIssueFilter();
        System.out.println(getYourIssue());
        applyFilterAndCheckResult(filter, this.issues, getYourIssue());
    }

    @Test
    void shouldFindYourIssueByAOneTooOnePackageNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setPackageNameExcludeFilter("Your2PackageName")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getMyIssue(), getHisIssue());
    }

    @Test
    void shouldFindHisIssueByAOneTooOneModuleNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setModuleNameIncludeFilter("His3ModuleName")
                .createIssueFilter();
        System.out.println(getYourIssue());
        applyFilterAndCheckResult(filter, this.issues, getHisIssue());
    }

    @Test
    void shouldFindHisIssueByAOneTooOneModuleNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setModuleNameExcludeFilter("His3ModuleName")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getMyIssue(), getYourIssue());
    }

    @Test
    void shouldFindMyIssueByAOneTooOneCategoryIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setCategoryIncludeFilter("My1CategoryName")
                .createIssueFilter();
        System.out.println(getYourIssue());
        applyFilterAndCheckResult(filter, this.issues, getMyIssue());
    }

    @Test
    void shouldFindMyIssueByAOneTooOneCategoryExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setCategoryExcludeFilter("My1CategoryName")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getYourIssue(), getHisIssue());
    }

    @Test
    void shouldFindYourIssueByAOneTooOneTypeIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setTypeIncludeFilter("Your2Type")
                .createIssueFilter();
        System.out.println(getYourIssue());
        applyFilterAndCheckResult(filter, this.issues, getYourIssue());
    }

    @Test
    void shouldFindYourIssueByAOneTooOneCategoryExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setTypeExcludeFilter("Your2Type")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, getMyIssue(), getHisIssue());
    }

    private void applyFilterAndCheckResult(IssueFilter filter, Issues input, Issues expectedOutput){
        applyFilterAndCheckResult(filter, input, Streams.stream(expectedOutput.iterator()).toArray(Issue[]::new));
    }

    private void applyFilterAndCheckResult(IssueFilter filter, Issues input, Issue... expectedOutput){
        Issues result = filter.apply(input);
        assertThat(result.iterator()).containsExactly(expectedOutput);
    }

}
