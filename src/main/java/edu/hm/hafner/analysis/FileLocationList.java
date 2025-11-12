package edu.hm.hafner.analysis;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A list of {@link FileLocation file locations}.
 *
 * @author Akash Manna
 */
public class FileLocationList implements Iterable<FileLocation>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<FileLocation> fileLocations = new ArrayList<>();

    /**
     * Creates a new empty instance of {@link FileLocationList}.
     */
    public FileLocationList() {
        // empty constructor
    }

    /**
     * Creates a new instance of {@link FileLocationList} from the specified
     * iterable.
     *
     * @param fileLocations
     *         the file locations to add
     */
    public FileLocationList(final Iterable<? extends FileLocation> fileLocations) {
        for (FileLocation fileLocation : fileLocations) {
            this.fileLocations.add(fileLocation);
        }
    }

    /**
     * Adds a file location to the list.
     *
     * @param fileLocation
     *         the file location to add
     */
    public void add(final FileLocation fileLocation) {
        fileLocations.add(fileLocation);
    }

    /**
     * Adds all file locations from the collection to the list.
     *
     * @param fileLocations
     *         the file locations to add
     */
    public void addAll(final Iterable<? extends FileLocation> fileLocations) {
        for (FileLocation fileLocation : fileLocations) {
            this.fileLocations.add(fileLocation);
        }
    }

    /**
     * Returns the number of file locations in this list.
     *
     * @return the number of file locations
     */
    public int size() {
        return fileLocations.size();
    }

    /**
     * Returns whether this list is empty.
     *
     * @return {@code true} if this list is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return fileLocations.isEmpty();
    }

    /**
     * Returns the file location at the specified index.
     *
     * @param index
     *         the index
     *
     * @return the file location at the specified index
     */
    public FileLocation get(final int index) {
        return fileLocations.get(index);
    }

    /**
     * Removes the file location at the specified index.
     *
     * @param index
     *         the index
     */
    public void remove(final int index) {
        fileLocations.remove(index);
    }

    @Override
    public Iterator<FileLocation> iterator() {
        return fileLocations.iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (FileLocationList) o;
        return Objects.equals(fileLocations, that.fileLocations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileLocations);
    }

    @Override
    public String toString() {
        return fileLocations.toString();
    }
}
