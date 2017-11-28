package edu.hm.hafner.analysis.filter;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Streams;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import static org.assertj.core.api.Assertions.assertThat;


public class IssueFilterTest {

    private final Issues issues = new Issues(){
        {
            add(issue1);
            add(issue2);
            add(issue3);
        }
    };
    private final static Issue issue1 = new IssueBuilder()
            .setFileName("FileName1")
            .setPackageName("PackageName1")
            .setModuleName("ModuleName1")
            .setCategory("CategoryName1")
            .setType("Type1")
            .build();
    private final static Issue issue2 = new IssueBuilder()
            .setFileName("FileName2")
            .setPackageName("PackageName2")
            .setModuleName("ModuleName2")
            .setCategory("CategoryName2")
            .setType("Type2")
            .build();

    private final static Issue issue3 = new IssueBuilder()
            .setFileName("FileName3")
            .setPackageName("PackageName3")
            .setModuleName("ModuleName3")
            .setCategory("CategoryName3")
            .setType("Type3")
            .build();


    @Test
    void shouldNothingChangeWhenNoFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, this.issues);
    }
    @Test
    void shouldPassAllWhenUselessFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("[a-zA-Z1]*")
                .setIncludeFilenameFilter("[a-zA-Z2]*")
                .setIncludeFilenameFilter("[a-zA-Z3]*")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, this.issues);
    }
    @Test
    void shouldPassAllWhenUselessFilterIsAddedAsList(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*" )
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, this.issues);
    }
    @Test
    void shouldPassNoWhenMasterFilterIsAdded(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("[a-zA-Z_1-3]*")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues);
    }
    @Test
    void shouldPassNoWhenMasterFilterIsAddedAsList(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("[a-zA-Z1]*", "[a-zA-Z2]*", "[a-zA-Z3]*" )
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues);
    }
    @Test
    void shouldFindissue1ByAFileNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                            .setIncludeFilenameFilter("FileName1")
                            .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1);
    }
    @Test
    void shouldFindissue1ByAFileNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeFilenameFilter("FileName1")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue2, issue3);
    }
    @Test
    void shouldFindissue2ByAPackageNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludePackageNameFilter("PackageName2")
                .createIssueFilter();
        System.out.println(issue2);
        applyFilterAndCheckResult(filter, this.issues, issue2);
    }
    @Test
    void shouldFindissue2ByAPackageNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludePackageNameFilter("PackageName2")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1, issue3);
    }
    @Test
    void shouldFindissue3ByAModuleNameIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeModuleNameFilter("ModuleName3")
                .createIssueFilter();
        System.out.println(issue2);
        applyFilterAndCheckResult(filter, this.issues, issue3);
    }
    @Test
    void shouldFindissue3ByAModuleNameExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeModuleNameFilter("ModuleName3")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1, issue2);
    }
    @Test
    void shouldFindissue1ByACategoryIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeCategoryFilter("CategoryName1")
                .createIssueFilter();
        System.out.println(issue2);
        applyFilterAndCheckResult(filter, this.issues, issue1);
    }
    @Test
    void shouldFindissue1ByACategoryExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeCategoryFilter("CategoryName1")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue2, issue3);
    }
    @Test
    void shouldFindissue2ByATypeIncludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeTypeFilter("Type2")
                .createIssueFilter();
        System.out.println(issue2);
        applyFilterAndCheckResult(filter, this.issues, issue2);
    }
    @Test
    void shouldFindissue2ByACategoryExcludeMatch(){
        IssueFilter filter = new IssueFilterBuilder()
                .setExcludeTypeFilter("Type2")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1, issue3);
    }
    @Test
    void shouldFindIntersectionFromIncludeAndExcludeBySameProperty(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileName1")
                .setIncludeFilenameFilter("FileName2")
                .setExcludeFilenameFilter("FileName2")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1);
    }
    @Test
    void shouldFindIntersectionFromIncludeAndExcludeByOtherProperty(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileName1")
                .setIncludeFilenameFilter("FileName2")
                .setExcludeTypeFilter("Type2")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues, issue1);
    }
    @Test
    void shouldFindNoIntersectionFromEmptyIncludeAndExclude(){
        IssueFilter filter = new IssueFilterBuilder()
                .setIncludeFilenameFilter("FileNameNotExisting")
                .setExcludeTypeFilter("Type2")
                .createIssueFilter();
        applyFilterAndCheckResult(filter, this.issues);
    }







    private void applyFilterAndCheckResult(IssueFilter filter, Issues input, Issues expectedOutput){
        applyFilterAndCheckResult(filter, input, Streams.stream(expectedOutput.iterator()).toArray(Issue[]::new));
    }

    private void applyFilterAndCheckResult(IssueFilter filter, Issues input, Issue... expectedOutput){
        Issues result = filter.apply(input);
        assertThat(result.iterator()).containsExactly(expectedOutput);
    }

}
