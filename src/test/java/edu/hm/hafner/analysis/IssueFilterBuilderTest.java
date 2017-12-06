package edu.hm.hafner.analysis;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.IssueTest.*;
import static org.assertj.core.api.Assertions.*;


public class IssueFilterBuilderTest {

    @Test
    void shouldCreateFilterAll() {
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add(FILE_NAME);
        ArrayList<String> packageNames = new ArrayList<>();
        packageNames.add(PACKAGE_NAME);
        ArrayList<String> moduleNames = new ArrayList<>();
        moduleNames.add(MODULE_NAME);
        ArrayList<String> categories = new ArrayList<>();
        categories.add(CATEGORY);
        ArrayList<String> types = new ArrayList<>();
        types.add(TYPE);

        IssueFilter issueFilter = new IssueFilterBuilder()
                .setFileNames(fileNames)
                .setPackageNames(packageNames)
                .setModuleNames(moduleNames)
                .setCategories(categories)
                .setTypes(types)
                .build();

        assertThat(issueFilter.getFileNames()).isEqualTo(fileNames);
        assertThat(issueFilter.getPackageNames()).isEqualTo(packageNames);
        assertThat(issueFilter.getModuleNames()).isEqualTo(moduleNames);
        assertThat(issueFilter.getCategories()).isEqualTo(categories);
        assertThat(issueFilter.getTypes()).isEqualTo(types);
    }

    @Test
    void shouldCreateEmptyFilter() {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> packageNames = new ArrayList<>();
        ArrayList<String> moduleNames = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();

        IssueFilter issueFilter = new IssueFilterBuilder().build();

        assertThat(issueFilter.getFileNames()).isEqualTo(fileNames);
        assertThat(issueFilter.getPackageNames()).isEqualTo(packageNames);
        assertThat(issueFilter.getModuleNames()).isEqualTo(moduleNames);
        assertThat(issueFilter.getCategories()).isEqualTo(categories);
        assertThat(issueFilter.getTypes()).isEqualTo(types);
    }


}
