package edu.hm.hafner.analysis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueFilterConfig.IssueFilterConfigBuilder;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static java.util.Arrays.*;

public class IssueFilterTest {

    private static final Issue FILTER_ISSUE1 = new IssueBuilder()
            .setCategory("cat-1")
            .setFileName("file-1")
            .setModuleName("mod-1")
            .setType("type-1")
            .setPackageName("pack-1")
            .build();
    private static final Issue FILTER_ISSUE2 = new IssueBuilder()
            .setCategory("cat-1")
            .setFileName("file-1")
            .setModuleName("mod-2")
            .setType("type-2")
            .setPackageName("pack-1")
            .build();
    private static final Issue FILTER_ISSUE3 = new IssueBuilder()
            .setCategory("cat-2")
            .setFileName("file-1")
            .setModuleName("mod-3")
            .setType("type-1")
            .setPackageName("pack-2")
            .build();
    private static final Issue FILTER_ISSUE4 = new IssueBuilder()
            .setCategory("cat-1")
            .setFileName("file-2")
            .setModuleName("mod-3")
            .setType("type-2")
            .setPackageName("pack-2")
            .build();
    private static final Issue FILTER_ISSUE5 = new IssueBuilder()
            .setFileName("file-3")
            .build();
    private static final Issue FILTER_ISSUE6 = new IssueBuilder()
            .setFileName("file-4")
            .build();

    @Test
    void shouldFiterWithIncludeFileName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("file-1")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3);
    }

    @Test
    void shouldFiterWithIncludeCategory() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeCategory(asList("cat-1")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE4);
    }

    @Test
    void shouldFiterWithIncludeModule() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeModuleName(asList("mod-3")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE3, FILTER_ISSUE4);
    }

    @Test
    void shouldFiterWithIncludeType() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeType(asList("type-1")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE3);
    }

    @Test
    void shouldFilterWithIncludePackageName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includePackageName(asList("pack-1")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2);
    }

    @Test
    void shouldFilterWithExcludeFileName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeFileName(asList("file-2")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3);
    }

    @Test
    void shouldFilterWithExcludePackageName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludePackageName(asList("pack-2")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2);
    }

    @Test
    void shouldFilterWithExcludeCategory() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeCategory(asList("cat-2")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE4);
    }

    @Test
    void shouldFilterWithExcludeModule() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeModuleName(asList("mod-3")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2);
    }

    @Test
    void shouldFilterWithExcludeType() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeType(asList("type-2")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE3);
    }

    @Test
    void shouldFilterOneIssuesWhenOneExistingFileName() {
        Issues sut = new Issues();
        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("file-1")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered.all()).containsOnly(FILTER_ISSUE1);
    }

    @Test
    void shouldFilterZeroIssuesWhenNotExistingFileName() {
        Issues sut = new Issues();
        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("SomeNotExistingFile")).build();
        Issues filtered = sut.filterIssues(config);

        assertThat(filtered).hasSize(0);
    }

    @Test
    void shouldFilterTwoIssuesWhenTwoFileNames() {
        Issues sut = new Issues();
        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("file-1", "file-3")).build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE5);
    }

    @Test
    void shouldNotFilterWhenNoPredicateIsSet() {
        Issues sut = new Issues();
        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5));

        IssueFilterConfig config = new IssueFilterConfigBuilder().build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5);
    }

    @Test
    void shouldMatchAllWhereOneAtributeMatchesWithTwoFiltersSet() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("file-1"))
                .includeCategory(asList("cat-1")).build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4);
    }

    @Test
    void shouldExcludeWithTwoExcludeFiltersForFileNameAndModuleName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeFileName(asList("file-2"))
                .excludeModuleName(asList("mod-2")).includeCategory(asList("cat-1")).build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1);
    }

    @Test
    void shouldExcludeWithNoIncludeFiltersAndTwoExcludeFiltersForFileNameAndModuleName() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE2, FILTER_ISSUE3, FILTER_ISSUE4));

        IssueFilterConfig config = new IssueFilterConfigBuilder().excludeFileName(asList("file-2"))
                .excludeModuleName(asList("mod-2")).build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE3);
    }

    @Test
    void shouldIncludeCorrectlyWithComplexPatternSet() {
        Issues sut = new Issues();

        sut.addAll(asList(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5, FILTER_ISSUE6));

        IssueFilterConfig config = new IssueFilterConfigBuilder().includeFileName(asList("file-1|2", "file-3")).build();
        Issues filtered = sut.filterIssues(config);

        Assertions.assertThat(filtered.all()).containsOnly(FILTER_ISSUE1, FILTER_ISSUE4, FILTER_ISSUE5);
    }
}
