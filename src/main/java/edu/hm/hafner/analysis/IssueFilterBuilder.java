package edu.hm.hafner.analysis;

import java.util.ArrayList;

public class IssueFilterBuilder {

    private ArrayList<String> fileNames;
    private ArrayList<String> packageNames;
    private ArrayList<String> moduleNames;
    private ArrayList<String> categories;
    private ArrayList<String> types;

    public IssueFilterBuilder() {
        fileNames = new ArrayList<>();
        packageNames = new ArrayList<>();
        moduleNames = new ArrayList<>();
        categories = new ArrayList<>();
        types = new ArrayList<>();
    }

    public IssueFilterBuilder setFileNames(final ArrayList<String> fileNames) {
        this.fileNames = fileNames;
        return this;
    }

    public IssueFilterBuilder setPackageNames(final ArrayList<String> packageNames) {
        this.packageNames = packageNames;
        return this;
    }

    public IssueFilterBuilder setModuleNames(final ArrayList<String> moduleNames) {
        this.moduleNames = moduleNames;
        return this;
    }

    public IssueFilterBuilder setCategories(final ArrayList<String> categories) {
        this.categories = categories;
        return this;
    }

    public IssueFilterBuilder setTypes(final ArrayList<String> types) {
        this.types = types;
        return this;
    }


    public IssueFilter build() {

        return new IssueFilter(fileNames, packageNames, moduleNames, categories, types);
    }
}
