package edu.hm.hafner.analysis.parser.checkstyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Java Bean class for a errors collection of the Checkstyle format.
 *
 * @author Ullrich Hafner
 */
public class CheckStyle {
    /** All files of this violations collection. */
    private final List<File> files = new ArrayList<>();

    /**
     * Adds a new file to this bug collection.
     *
     * @param file the file to add
     */
    public void addFile(final File file) {
        files.add(file);
    }

    /**
     * Returns all files of this violations collection. The returned collection is
     * read-only.
     *
     * @return all files of this bug collection
     */
    public Collection<File> getFiles() {
        return Collections.unmodifiableCollection(files);
    }
}
