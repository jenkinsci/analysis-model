package edu.hm.hafner.analysis;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

class IssueFilterBuilderTest {

    private static final String INCLUDE_FILENAME = "IssueInclude.java";
    private static final String INCLUDE_CATEGORY = "CategoryInclude";
    private static final String INCLUDE_TYPE = "TypeInclude";
    private static final String INCLUDE_PACKAGE = "PackageInclude";
    private static final String INCLUDE_MODULE = "ModuleInclude";

    private static final String EXCLUDE_FILENAME = "IssueExclude.java";
    private static final String EXCLUDE_CATEGORY = "CategoryExclude";
    private static final String EXCLUDE_MODULE = "MooduleExclude";
    private static final String EXCLUDE_PACKAGE = "PackageExclude";
    private static final String EXCLUDE_TYPE = "TypeExclude";

    private static final Issue ISSUE_FILENAME_MATCH = new IssueBuilder()
            .setPackageName(EXCLUDE_PACKAGE)
            .setCategory(EXCLUDE_CATEGORY)
            .setModuleName(EXCLUDE_MODULE)
            .setType(EXCLUDE_TYPE)
            .setFileName(INCLUDE_FILENAME)
            .build();

    private static final Issue ISSUE_CATEGORY_MATCH = new IssueBuilder()
            .setPackageName(EXCLUDE_PACKAGE)
            .setModuleName(EXCLUDE_MODULE)
            .setType(EXCLUDE_TYPE)
            .setFileName(EXCLUDE_FILENAME)
            .setCategory(INCLUDE_CATEGORY)
            .build();

    private static final Issue ISSUE_PACKAGE_MATCH = new IssueBuilder()
            .setModuleName(EXCLUDE_MODULE)
            .setType(EXCLUDE_TYPE)
            .setFileName(EXCLUDE_FILENAME)
            .setCategory(EXCLUDE_CATEGORY)
            .setPackageName(INCLUDE_PACKAGE)
            .build();

    private static final Issue ISSUE_MODULE_MATCH = new IssueBuilder()
            .setType(EXCLUDE_TYPE)
            .setFileName(EXCLUDE_FILENAME)
            .setCategory(EXCLUDE_CATEGORY)
            .setPackageName(EXCLUDE_PACKAGE)
            .setModuleName(INCLUDE_MODULE)
            .build();

    private static final Issue ISSUE_TYPE_MATCH = new IssueBuilder()
            .setFileName(EXCLUDE_FILENAME)
            .setCategory(EXCLUDE_CATEGORY)
            .setModuleName(EXCLUDE_MODULE)
            .setPackageName(EXCLUDE_PACKAGE)
            .setType(INCLUDE_TYPE)
            .build();

    private static final Issue ISSUE_NONE_MATCH = new IssueBuilder()
            .setCategory(EXCLUDE_CATEGORY)
            .setFileName(EXCLUDE_FILENAME)
            .build();

    private static Iterable<Object> testData() {
        String pattern = ".*Include.*";
        return asList(
                Arguments.of(
                        "Should include filename matching pattern",
                        new IncludeIssueFilterBuilder()
                                .addFileFilters(singletonList(pattern))
                                .build(),
                        ISSUE_FILENAME_MATCH,
                        ISSUE_CATEGORY_MATCH
                ),
                Arguments.of(
                        "Should include category matching pattern",
                        new IncludeIssueFilterBuilder()
                                .addCategoryFilters(singletonList(pattern))
                                .build(),
                        ISSUE_CATEGORY_MATCH,
                        ISSUE_MODULE_MATCH
                ),
                Arguments.of(
                        "Should include type matching pattern",
                        new IncludeIssueFilterBuilder()
                                .addTypeFilters(singletonList(pattern))
                                .build(),
                        ISSUE_TYPE_MATCH,
                        ISSUE_MODULE_MATCH
                ),
                Arguments.of("Should include module matching pattern",
                        new IncludeIssueFilterBuilder()
                                .addModuleFilters(singletonList(pattern))
                                .build(),
                        ISSUE_MODULE_MATCH,
                        ISSUE_PACKAGE_MATCH
                ),
                Arguments.of("Should include package matching pattern",
                        new IncludeIssueFilterBuilder()
                                .addPackageFilters(singletonList(pattern))
                                .build(),
                        ISSUE_PACKAGE_MATCH,
                        ISSUE_MODULE_MATCH
                ),
                Arguments.of("Should exclude package matching pattern",
                        new ExcludeIssueFilterBuilder()
                                .addPackageFilters(singletonList(pattern))
                                .build(),
                        ISSUE_CATEGORY_MATCH,
                        ISSUE_PACKAGE_MATCH),
                Arguments.of("Should exclude type matching pattern",
                        new ExcludeIssueFilterBuilder()
                                .addTypeFilters(singletonList(pattern))
                                .build(),
                        ISSUE_NONE_MATCH,
                        ISSUE_TYPE_MATCH
                )
                ,
                Arguments.of("Should exclude category matching pattern",
                        new ExcludeIssueFilterBuilder()
                                .addCategoryFilters(singletonList(pattern))
                                .build(),
                        ISSUE_NONE_MATCH,
                        ISSUE_CATEGORY_MATCH),
                Arguments.of("Should exclude file name matching pattern",
                        new ExcludeIssueFilterBuilder()
                                .addFileFilters(singletonList(pattern))
                                .build(),
                        ISSUE_NONE_MATCH,
                        ISSUE_FILENAME_MATCH
                ),
                Arguments.of("Should exclude module matching pattern",
                        new ExcludeIssueFilterBuilder()
                                .addModuleFilters(singletonList(pattern))
                                .build(),
                        ISSUE_NONE_MATCH,
                        ISSUE_MODULE_MATCH
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("testData")
    void testIsIncluded(String name, IssueFilter filter, Issue ok, Issue rejected) {
        assertThat(filter.isIncluded(ok))
                .isTrue();

        assertThat(filter.isIncluded(rejected))
                .isFalse();
    }


}