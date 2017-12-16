package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder class for the {@link IssuesFilter}.
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


    public IssuesFilterBuilder setFileNames(@CheckForNull final Set<String> fileNames) {
        if (fileNames != null) {
            this.fileNames = fileNames;
        }
        return this;
    }


    public IssuesFilterBuilder setPackageNames(@CheckForNull final Set<String> packageNames) {
        if (packageNames != null) {
            this.packageNames = packageNames;
        }
        return this;
    }


    public IssuesFilterBuilder setModuleNames(@CheckForNull final Set<String> moduleNames) {
        if (moduleNames != null) {
            this.moduleNames = moduleNames;
        }
        return this;
    }


    public IssuesFilterBuilder setCategories(@CheckForNull final Set<String> categories) {
        if (categories != null) {
            this.categories = categories;
        }
        return this;
    }


    public IssuesFilterBuilder setTypes(@CheckForNull final Set<String> types) {
        if (types != null) {
            this.types = types;
        }
        return this;
    }


    public IssuesFilter build() {
        return new IssuesFilter(fileNames, packageNames, moduleNames, categories, types);
    }
}
