package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.util.Set;

/**
 * Class contains all filter properties for the {@link Issues}.filter() function.
 *
 * @author Johannes Arzt
 */

public class IssuesFilter {

    private Set<String> fileNames;
    private Set<String> packageNames;
    private Set<String> moduleNames;
    private Set<String> categories;
    private Set<String>  types;

    /**
     * Return a Set with the file names the Filter is looking for.
     *
     * @return Set with FileNames
     */

    public Set<String> getFileNames() {
        return fileNames;
    }

    /**
     * Return a Set with the package names the Filter is looking for.
     *
     * @return Set with packageNames
     */

    public Set<String> getPackageNames() {
        return packageNames;
    }

    /**
     * Return a Set with the module names the Filter is looking for.
     *
     * @return Set with module names
     */

    public Set<String> getModuleNames() {
        return moduleNames;
    }

    /**
     * Return a Set with the categories the Filter is looking for.
     *
     * @return Set with categories
     */

    public Set<String> getCategories() {
        return categories;
    }

    /**
     * Return a Set with the types the Filter is looking for.
     *
     * @return Set with types
     */

    public Set<String> getTypes() {
        return types;
    }

    /**If all Sets with Filter properties are empty the Filter is empty.
     *
     * @return true when the the Filter is empty otherwise false
     */

    public boolean isEmpty() {
        return getCategories().isEmpty() && getFileNames().isEmpty() && getPackageNames().isEmpty()
                && getTypes().isEmpty() && getModuleNames().isEmpty();
    }

    /**
     * CTOR for the {@link IssuesFilterBuilder}.
     *
     * @param fileNames
     *         file names the Filter is looking for.
     * @param packageNames
     *         package names the Filter is looking for.
     * @param moduleNames
     *         module names the Filter is looking for.
     * @param categories
     *         categories the Filter is looking for.
     * @param types
     *         types the Filter is looking for
     */

    public IssuesFilter(@CheckForNull final Set<String> fileNames, @CheckForNull final Set<String> packageNames,
            @CheckForNull final Set<String> moduleNames, @CheckForNull final Set<String> categories,
            @CheckForNull final Set<String> types) {
        this.fileNames = fileNames;
        this.packageNames = packageNames;
        this.moduleNames = moduleNames;
        this.categories = categories;
        this.types = types;
    }
}

