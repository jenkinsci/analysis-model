package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.IssuesAssert;

/**
 * Test class for IssueFilter.
 */
class IssuesFilterTest {

    @Test
    void filterWithNoArgumentsShouldNotChangeIssues() {
        Issues issuesBefore = new Issues();
        IssuesFilter filter = new IssuesFilter();
        Issue issue1 = new IssueBuilder().setCategory("c1").build();
        Issue issue2 = new IssueBuilder().setCategory("c2").build();
        Issue issue3 = new IssueBuilder().setCategory("c1").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue2, issue3);
    }

    @Test
    void filterWitWildcardShouldNotChangeIssues() {
        Issues issuesBefore = new Issues();
        IssuesFilter filter = new IssuesFilterBuilder().build();
        Issue issue1 = new IssueBuilder().setCategory("c1").build();
        Issue issue2 = new IssueBuilder().setCategory("c2").build();
        Issue issue3 = new IssueBuilder().setCategory("c1").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue2, issue3);
    }

    @Test
    void filterWithFileNameFilter() {
        Issues issuesBefore = new Issues();
        IssuesFilter filter = new IssuesFilterBuilder().addIncludeFileName("abc.*").build();
        Issue issue1 = new IssueBuilder().setFileName("abc1").build();
        Issue issue2 = new IssueBuilder().setFileName("abc2").build();
        Issue issue3 = new IssueBuilder().setFileName("aaa1").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue2);
        filter = new IssuesFilterBuilder().addIncludeFileName(".*1").build();
        issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue3);
    }

    @Test
    void filterWithMultibleFileNameFilter() {
        Issues issuesBefore = new Issues();
        // all with digit at the end  a upp char at the begin and just 2x b
        IssuesFilter filter = new IssuesFilterBuilder().addIncludeFileName(".*\\d \\p{Upper}.*,bb").build();
        Issue issue1 = new IssueBuilder().setFileName("abc1").build();
        Issue issue2 = new IssueBuilder().setFileName("Aaa").build();
        Issue issue3 = new IssueBuilder().setFileName("bb").build();
        Issue issue4 = new IssueBuilder().setFileName("aaaXX").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);
        issuesBefore.add(issue4);

        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).as("check, that multible regexe are separated")
                .containsExactly(issue1, issue2, issue3);
    }

    @Test
    void checkWithJustOneExcludeFilter() {
        Issues issuesBefore = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").
                setPackageName("edu.hm.balz.p1").setType("Error").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").
                setPackageName("edu.hm.balz.p1").setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").
                setPackageName("edu.hm.balz.p2").setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").
                setPackageName("edu.hm.balz.p2").setType("42").setCategory("needle the Programmer").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);
        issuesBefore.add(issue4);
        IssuesFilter filter = new IssuesFilterBuilder().addExcludeCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).as("if there are just exclude filter all should be included").containsExactly(issue1, issue3, issue4);

    }

    @Test
    void checkWithTwiceTheSameInclude() {
        Issues issuesBefore = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").setPackageName("edu.hm.balz.p1").
                setType("Error").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").setPackageName("edu.hm.balz.p1").
                setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").setPackageName("edu.hm.balz.p2").
                setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").setPackageName("edu.hm.balz.p2").
                setType("42").setCategory("needle the Programmer").build();
        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);
        issuesBefore.add(issue4);
        IssuesFilter filter = new IssuesFilterBuilder().addIncludeFileName(".*c ").build();
        Issues issuesAfter = filter.executeFilter(issuesBefore);
        issuesAfter = filter.executeFilter(issuesAfter);
        IssuesAssert.assertThat(issuesAfter).as("twice the same filter should not have a result").containsExactly(issue1, issue2);

    }

    @Test
    void checkWithMultibleIncludeFilter() {
        Issues issuesBefore = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").setPackageName("edu.hm.balz.p1")
                .setType("ErrorType").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").setPackageName("edu.hm.balz.p1")
                .setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").setPackageName("edu.hm.balz.p2.1")
                .setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").setPackageName("edu.hm.balz.p2")
                .setType("42").setCategory("needle the Programmer").build();
        Issue issue5 = new IssueBuilder().setFileName("f5.c++").setModuleName("job").setPackageName("edu.hm.balz.job")
                .setType("secretCalc").setCategory("secret").build();
        Issue issue6 = new IssueBuilder().setFileName("sad.issue").setModuleName("because").setPackageName("no.one.has.included")
                .setType("this").setCategory("Issue").build();

        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);
        issuesBefore.add(issue4);
        issuesBefore.add(issue5);
        issuesBefore.add(issue6);
        IssuesFilter filter = new IssuesFilterBuilder().addIncludeFileName("f4.*").addIncludeModuleName("job")
                .addIncludePackageName(".*p2.1").addIncludeType("ErrorType").addIncludeCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).as("add issue 1-5 but no issue 6").
                containsExactly(issue1, issue2, issue3, issue4, issue5);

    }

    @Test
    void checkWithMultibleExcludeFilter() {
        Issues issuesBefore = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").setPackageName("edu.hm.balz.p1")
                .setType("ErrorType").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").setPackageName("edu.hm.balz.p1")
                .setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").setPackageName("edu.hm.balz.p2.1")
                .setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").setPackageName("edu.hm.balz.p2")
                .setType("42").setCategory("needle the Programmer").build();
        Issue issue5 = new IssueBuilder().setFileName("f5.c++").setModuleName("job").setPackageName("edu.hm.balz.job")
                .setType("secretCalc").setCategory("secret").build();
        Issue issue6 = new IssueBuilder().setFileName("happy.issue").setModuleName("because").setPackageName("no.one.has.excluded")
                .setType("this").setCategory("Issue").build();

        issuesBefore.add(issue1);
        issuesBefore.add(issue2);
        issuesBefore.add(issue3);
        issuesBefore.add(issue4);
        issuesBefore.add(issue5);
        issuesBefore.add(issue6);
        IssuesFilter filter = new IssuesFilterBuilder().addExcludeFileName("f4.*").addExcludeModuleName("job")
                .addExcludePackageName(".*p2.1").addExcludeType("ErrorType").addExcludeCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefore);
        IssuesAssert.assertThat(issuesAfter).as("add issue 1-5 but no issue 6").
                containsExactly(issue6);

    }

}