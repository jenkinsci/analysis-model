package edu.hm.hafner.analysis;

import javax.annotation.CheckForNull;
import java.util.List;

/** Class contains all filter properties for the {@link Issues}.filter() function.
 *
 * @author Johannes Arzt
 */

public class IssuesFilter {

    private List<String> fileNames;
    private List<String> packageNames;
    private List<String> moduleNames;
    private List<String> categories;
    private List<String> types;

    /**Return a List with the file names the Filter is looking for.
     *
     * @return List with FileNames
     */

    public List<String> getFileNames() {
        return fileNames;
    }

    /**Return a List with the package names the Filter is looking for.
     *
     * @return List with packageNames
     */

    public List<String> getPackageNames() {
        return packageNames;
    }

    /**Return a List with the module names the Filter is looking for.
     *
     * @return List with module names
     */

    public List<String> getModuleNames() {
        return moduleNames;
    }

    /**Return a List with the categories the Filter is looking for.
     *
     * @return List with categories
     */

    public List<String> getCategories() {
        return categories;
    }

    /**Return a List with the types the Filter is looking for.
     *
     * @return List with types
     */

    public List<String> getTypes() {
        return types;
    }

    /**CTOR for the {@link IssuesFilterBuilder}.
     *
     * @param fileNames  file names the Filter is looking for.
     * @param packageNames package names the Filter is looking for.
     * @param moduleNames module names the Filter is looking for.
     * @param categories categories the Filter is looking for.
     * @param types types the Filter is looking for
     */

    public IssuesFilter(@CheckForNull final List<String> fileNames, @CheckForNull final List<String> packageNames,
            @CheckForNull final List<String> moduleNames, @CheckForNull final List<String> categories,
            @CheckForNull final List<String> types) {
        this.fileNames = fileNames;
        this.packageNames = packageNames;
        this.moduleNames = moduleNames;
        this.categories = categories;
        this.types = types;
    }
}

