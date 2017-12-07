package edu.hm.hafner.analysis;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IssuesFilterTest {

    @Test
    void shouldRemoveAllIssuesWhenExcludingAllFilenames() {
        IssuesFilter filter = new IssuesFilter();

        Issues issues = new Issues();
        issues.add(new IssueBuilder().setFileName("file-1").build());
        issues.add(new IssueBuilder().setFileName("file-2").build());

        filter.addFilenameFilter(new ExcludeFilterRegex(".*"));

        assertThat(filter.apply(issues).size()).as("Should be empty").isEqualTo(0);
    }

    @Test
    void shouldIncludeAllIssuesWhenIncludingAllFilenames() {

        Issues issues = new Issues();
        issues.add(new IssueBuilder().setFileName("file-1").build());
        issues.add(new IssueBuilder().setFileName("file-2").build());

        IssuesFilter filter = new IssuesFilter();
        filter.addFilenameFilter(new IncludeFilterRegex(".*"));

        assertThat(filter.apply(issues).size()).as("Should be 2 issues").isEqualTo(2);
    }

    @Test
    void shouldExcludeAndIncludeCorrectlyWhenChained() {
        Issues issues = new Issues();
        issues.add(new IssueBuilder().setFileName("file-1").build());
        issues.add(new IssueBuilder().setFileName("file-2").build());

        IssuesFilter filter = new IssuesFilter();
        // Any characters followed by a zero or one "1"
        filter.addFilenameFilter(new IncludeFilterRegex(".*[1]?"));
        // Any characters followed by one ore more "2"
        filter.addFilenameFilter(new ExcludeFilterRegex(".*[2]+"));

        assertThat(filter.apply(issues).size()).as("Should be 1 issues").isEqualTo(1);
    }

    @Test
    void shouldWorkIncludeExcludeOnAllFieldsOfContract() {
        Issues issues = new Issues();
        issues.add(new IssueBuilder()
                .setFileName("SAFE")
                .setPackageName("SAFE")
                .setModuleName("SAFE")
                .setCategory("SAFE")
                .setType("SAFE")
                .build());
        issues.add(new IssueBuilder()
                .setFileName("DEATH")
                .setPackageName("SAFE")
                .setModuleName("SAFE")
                .setCategory("SAFE")
                .setType("SAFE")
                .build());
        issues.add(new IssueBuilder()
                .setFileName("SAFE")
                .setPackageName("DEATH")
                .setModuleName("SAFE")
                .setCategory("SAFE")
                .setType("SAFE")
                .build());
        issues.add(new IssueBuilder()
                .setFileName("SAFE")
                .setPackageName("SAFE")
                .setModuleName("DEATH")
                .setCategory("SAFE")
                .setType("SAFE")
                .build());
        issues.add(new IssueBuilder()
                .setFileName("SAFE")
                .setPackageName("SAFE")
                .setModuleName("SAFE")
                .setCategory("DEATH")
                .setType("SAFE")
                .build());
        issues.add(new IssueBuilder()
                .setFileName("SAFE")
                .setPackageName("SAFE")
                .setModuleName("SAFE")
                .setCategory("SAFE")
                .setType("DEATH")
                .build());

        IssuesFilter filter = new IssuesFilter();

        filter.addFilenameFilter(new IncludeFilterRegex("SAFE"));
        filter.addFilenameFilter(new ExcludeFilterRegex("DEATH"));
        filter.addPackageNameFilter(new IncludeFilterRegex("SAFE"));
        filter.addPackageNameFilter(new ExcludeFilterRegex("DEATH"));
        filter.addModuleNameFilter(new IncludeFilterRegex("SAFE"));
        filter.addModuleNameFilter(new ExcludeFilterRegex("DEATH"));
        filter.addCategoryFilter(new IncludeFilterRegex("SAFE"));
        filter.addCategoryFilter(new ExcludeFilterRegex("DEATH"));
        filter.addTypeFilter(new IncludeFilterRegex("SAFE"));
        filter.addTypeFilter(new ExcludeFilterRegex("DEATH"));

        assertThat(issues.size()).isEqualTo(6);
        assertThat(filter.apply(issues).size()).as("Should be 1 issues").isEqualTo(1);
    }
}