package edu.hm.hafner.analysis.parser.pmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Java Bean class for warnings of the PMD format.
 *
 * @author Ullrich Hafner
 */
public class Pmd {
    private final List<File> files = new ArrayList<>();
    private final List<PmdError> errors = new ArrayList<>();

    /**
     * Adds a new file.
     *
     * @param file
     *         the file to add
     */
    public void addFile(final File file) {
        files.add(file);
    }

    /**
     * Adds a new error.
     *
     * @param error
     *         the error to add
     */
    public void addError(final PmdError error) {
        errors.add(error);
    }

    /**
     * Returns all files. The returned collection is read-only.
     *
     * @return all files
     */
    public Collection<File> getFiles() {
        return Collections.unmodifiableCollection(files);
    }

    /**
     * Returns all errors. The returned collection is read-only.
     *
     * @return all errors
     */
    public Collection<PmdError> getErrors() {
        return Collections.unmodifiableCollection(errors);
    }
}
