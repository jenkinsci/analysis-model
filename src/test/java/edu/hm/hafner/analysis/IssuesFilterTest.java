package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.assertj.IssuesAssert;
import static org.junit.jupiter.api.Assertions.*;


class IssuesFilterTest {


    Issues generateIssues() {
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").setPackageName("edu.hm.balz.p1").setType("Error").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").setPackageName("edu.hm.balz.p1").setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").setPackageName("edu.hm.balz.p2").setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").setPackageName("edu.hm.balz.p2").setType("42").setCategory("needle the Programmer").build();
        Issues out = new Issues();
        out.add(issue1);
        out.add(issue2);
        out.add(issue3);
        out.add(issue4);
        return out;
    }

    @Test
    void filterWithNoArgumentsShouldNotChangeIssues() {
        Issues issuesBefor = new Issues();
        IssuesFilter filter = new IssuesFilter();
        Issue issue1 = new IssueBuilder().setCategory("c1").build();
        Issue issue2 = new IssueBuilder().setCategory("c2").build();
        Issue issue3 = new IssueBuilder().setCategory("c1").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesBefor).containsExactly(issue1, issue2, issue3);
    }

    @Test
    void filterWitWildcardShouldNotChangeIssues() {
        Issues issuesBefor = new Issues();
        IssuesFilter filter = new IssuesFilterBuilder().build();
        Issue issue1 = new IssueBuilder().setCategory("c1").build();
        Issue issue2 = new IssueBuilder().setCategory("c2").build();
        Issue issue3 = new IssueBuilder().setCategory("c1").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesBefor).containsExactly(issue1, issue2, issue3);
    }

    @Test
    void filterWithFileNameFilter() {
        Issues issuesBefor = new Issues();
        IssuesFilter filter = new IssuesFilterBuilder().addIncludFileName("abc.*").build();
        Issue issue1 = new IssueBuilder().setFileName("abc1").build();
        Issue issue2 = new IssueBuilder().setFileName("abc2").build();
        Issue issue3 = new IssueBuilder().setFileName("aaa1").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);

        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue2);
        filter = new IssuesFilterBuilder().addIncludFileName(".*1").build();
        issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).containsExactly(issue1, issue3);
    }

    @Test
    void filterWithMultibleFileNameFilter() {
        Issues issuesBefor = new Issues();
        // all with digit at the end  a upp char at the begin and just 2x b
        IssuesFilter filter = new IssuesFilterBuilder().addIncludFileName(".*\\d \\p{Upper}.*,bb").build();
        Issue issue1 = new IssueBuilder().setFileName("abc1").build();
        Issue issue2 = new IssueBuilder().setFileName("Aaa").build();
        Issue issue3 = new IssueBuilder().setFileName("bb").build();
        Issue issue4 = new IssueBuilder().setFileName("aaaXX").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);
        issuesBefor.add(issue4);

        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).as("check, that multible regexe are separated")
                .containsExactly(issue1, issue2, issue3);
    }

    @Test
    void checkWithJustOneExcludeFilter() {
        Issues issuesBefor = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").
                setPackageName("edu.hm.balz.p1").setType("Error").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").
                setPackageName("edu.hm.balz.p1").setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").
                setPackageName("edu.hm.balz.p2").setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").
                setPackageName("edu.hm.balz.p2").setType("42").setCategory("needle the Programmer").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);
        issuesBefor.add(issue4);
        IssuesFilter filter = new IssuesFilterBuilder().addExcluCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).as("if there are just exclude filter all should be included").containsExactly(issue1, issue3, issue4);

    }

    @Test
    void checkWithTwiceTheSameInclude() {
        Issues issuesBefor = new Issues();
        Issue issue1 = new IssueBuilder().setFileName("f1.c").setModuleName("private").setPackageName("edu.hm.balz.p1").
                setType("Error").setCategory("Warning").build();
        Issue issue2 = new IssueBuilder().setFileName("f2.c").setModuleName("homework").setPackageName("edu.hm.balz.p1").
                setType("Critic").setCategory("Error").build();
        Issue issue3 = new IssueBuilder().setFileName("f3.java").setModuleName("private").setPackageName("edu.hm.balz.p2").
                setType("Blub").setCategory("Critic Error").build();
        Issue issue4 = new IssueBuilder().setFileName("f4.java").setModuleName("homework").setPackageName("edu.hm.balz.p2").
                setType("42").setCategory("needle the Programmer").build();
        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);
        issuesBefor.add(issue4);
        IssuesFilter filter = new IssuesFilterBuilder().addIncludFileName(".*c ").build();
        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).as("twice the same filter should not have a result").containsExactly(issue1, issue2);

    }

    @Test
    void checkWithMultibleIncludeFilter() {
        Issues issuesBefor = new Issues();
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

        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);
        issuesBefor.add(issue4);
        issuesBefor.add(issue5);
        issuesBefor.add(issue6);
        IssuesFilter filter = new IssuesFilterBuilder().addIncludFileName("f4.*").addIncludModuleName("job")
                .addIncludPackageName(".*p2.1").addIncludType("ErrorType").addIncluCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).as("add issue 1-5 but no issue 6").
                containsExactly(issue1, issue2, issue3, issue4, issue5);

    }
    @Test
    void checkWithMultibleExcludeFilter() {
        Issues issuesBefor = new Issues();
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

        issuesBefor.add(issue1);
        issuesBefor.add(issue2);
        issuesBefor.add(issue3);
        issuesBefor.add(issue4);
        issuesBefor.add(issue5);
        issuesBefor.add(issue6);
        IssuesFilter filter = new IssuesFilterBuilder().addExcludFileName("f4.*").addExcludModuleName("job")
                .addExcludPackageName(".*p2.1").addExcludType("ErrorType").addExcluCategory("Error").build();
        Issues issuesAfter = filter.executeFilter(issuesBefor);
        IssuesAssert.assertThat(issuesAfter).as("add issue 1-5 but no issue 6").
                containsExactly( issue6);

    }

}