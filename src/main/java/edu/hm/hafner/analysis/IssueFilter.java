package edu.hm.hafner.analysis;

import java.util.ArrayList;

/**
 * Filter for {@link Issues issues}. Finds and filters issues based on different properties.
 *
 * @author Tom Maier
 */
public class IssueFilter {

    private ArrayList<String> fileNames;
    private ArrayList<String> packageNames;
    private ArrayList<String> moduleNames;
    private ArrayList<String> categories;
    private ArrayList<String> types;

    public IssueFilter(final ArrayList<String> fileNames, final ArrayList<String> packageNames, final ArrayList<String> moduleNames, final ArrayList<String> categories, final ArrayList<String> types) {
        this.fileNames = fileNames;
        this.packageNames = packageNames;
        this.moduleNames = moduleNames;
        this.categories = categories;
        this.types = types;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public ArrayList<String> getPackageNames() {
        return packageNames;
    }

    public ArrayList<String> getModuleNames() {
        return moduleNames;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}
