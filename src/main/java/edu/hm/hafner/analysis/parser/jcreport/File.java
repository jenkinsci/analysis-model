package edu.hm.hafner.analysis.parser.jcreport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * File-Class. Stores field to create a warning. It represents the File-Tags within the report.xml. The
 * Java-Bean-Conformity was chosen due to the digesters style of assigning.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
@SuppressWarnings("PMD.DataClass")
public class File {
    @Nullable
    private String name;
    @Nullable
    private String packageName;
    @Nullable
    private String srcdir;
    private final List<Item> items = new ArrayList<>();

    /**
     * These properties are not used to create Warnings. It was decided to keep them available when Jenkins is modified
     * and needs access to these fields;
     */
    @Nullable
    private String level;
    @Nullable
    private String loc;
    @Nullable
    private String classname;

    /**
     * Getter for the Item-Collection.
     *
     * @return unmodifiable collection of Item-Objects
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Adds an Item-Object to the collection items.
     *
     * @param item add this item.
     */
    public void addItem(final Item item) {
        items.add(item);
    }


    /**
     * Getter for className-Field.
     *
     * @return String className.
     */
    @Nullable
    public String getClassname() {
        return classname;
    }

    /**
     * Setter for className-Field.
     *
     * @param classname lassNamesetter
     */
    public void setClassname(final String classname) {
        this.classname = classname;
    }

    /**
     * Getter for level-Field.
     *
     * @return level
     */
    @Nullable
    public String getLevel() {
        return level;
    }


    /**
     * Setter for level-Field.
     *
     * @param level set level
     */
    public void setLevel(final String level) {
        this.level = level;
    }


    /**
     * Getter for loc-Field.
     *
     * @return loc loc
     */
    @Nullable
    public String getLoc() {
        return loc;
    }

    /**
     * Setter for loc-Field.
     *
     * @param loc locsetter
     */
    public void setLoc(final String loc) {
        this.loc = loc;
    }


    /**
     * Getter for name-Field.
     *
     * @return name name
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Setter for Name-Field.
     *
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }


    /**
     * Getter for packageName-Field.
     *
     * @return packageName packageName.
     */
    @Nullable
    public String getPackageName() {
        return packageName;
    }

    /**
     * Setter for packageName-Field.
     *
     * @param packageName packageName Setter
     */
    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    /**
     * Getter for srcdir-Field.
     *
     * @return srcdir srcdir.
     */
    @Nullable
    public String getSrcdir() {
        return srcdir;
    }

    /**
     * Setter for srcdir-Field.
     *
     * @param srcdir srcdir
     */
    public void setSrcdir(final String srcdir) {
        this.srcdir = srcdir;
    }

}
