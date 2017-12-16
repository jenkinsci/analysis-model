package edu.hm.hafner.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Builder class for the {@link IssuesFilter}.
 *
 * @author Johannes Arzt
 */

@SuppressWarnings("JavaDocMethod")
public class IssuesFilterBuilder {

    private Set<String> fileNames = new HashSet<>();
    private Set<String> packageNames = new HashSet<>();
    private Set<String> moduleNames = new HashSet<>();
    private Set<String> categories = new HashSet<>();
    private Set<String> types = new HashSet<>();



    public IssuesFilterBuilder setFileNames(final Set<String> fileNames) {
        this.fileNames = fileNames;
        return this;
    }


    public IssuesFilterBuilder setPackageNames(final Set<String> packageNames) {
        this.packageNames = packageNames;
        return this;
    }



    public IssuesFilterBuilder setModuleNames(final Set<String> moduleNames) {
        this.moduleNames = moduleNames;
        return this;
    }


    public IssuesFilterBuilder setCategories(final Set<String> categories) {
        this.categories = categories;
        return this;
    }



    public IssuesFilterBuilder setTypes(final Set<String> types) {
        this.types = types;
        return this;
    }


    public IssuesFilter build() {
        return new IssuesFilter(fileNames, packageNames, moduleNames, categories, types);
    }
}
