package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.List;

/** Builder class for the {@link IssuesFilter}.
 *
 * @author Johannes Arzt
 */

@SuppressWarnings("JavaDocMethod")
public class IssuesFilterBuilder {

    private List<String> fileNames = new ArrayList<>();
    private List<String> packageNames = new ArrayList<>();
    private List<String> moduleNames = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> types = new ArrayList<>();



    public IssuesFilterBuilder setFileNames(final List<String> fileNames) {
        this.fileNames = fileNames;
        return this;
    }


    public IssuesFilterBuilder setPackageNames(final List<String> packageNames) {
        this.packageNames = packageNames;
        return this;
    }



    public IssuesFilterBuilder setModuleNames(final List<String> moduleNames) {
        this.moduleNames = moduleNames;
        return this;
    }


    public IssuesFilterBuilder setCategories(final List<String> categories) {
        this.categories = categories;
        return this;
    }



    public IssuesFilterBuilder setTypes(final List<String> types) {
        this.types = types;
        return this;
    }


    public IssuesFilter build() {
        return new IssuesFilter(fileNames, packageNames, moduleNames, categories, types);
    }
}
