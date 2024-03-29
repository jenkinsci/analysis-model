package edu.hm.hafner.analysis.parser.jcreport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This is the Report-Class. It is mandatory to create Warnings. It represents the outer-most node within the
 * report.xml.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
@SuppressWarnings("PMD.DataClass")
@SuppressFBWarnings("EI")
public class Report {
    private List<File> files = new ArrayList<>();

    /**
     * Returns an unmodifiable Collection.
     *
     * @return files getter
     */
    public List<File> getFiles() {
        return Collections.unmodifiableList(files);
    }

    /**
     * Setter for the List files.
     *
     * @param files a list of files.
     */
    public void setFiles(final List<File> files) {
        this.files = files;
    }

    /**
     * Adds a new File to the Collection.
     *
     * @param file setter
     */
    public void addFile(final File file) {
        files.add(file);
    }
}
