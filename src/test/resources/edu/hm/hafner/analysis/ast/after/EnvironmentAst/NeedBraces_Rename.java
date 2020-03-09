package edu.hm.hafner;

import java.util.Date;

/**
 * Useless class - only for test-cases.
 *
 * @author Christian Möstl
 */
public class NeedBraces_Rename {
    private final Date date;
    private final int number;
    private final String text;

    /**
     * Creates a new instance of {@link NeedBraces_Rename}.
     *
     * @param date
     *            Date
     * @param number
     *            number
     * @param text
     *            Text
     */
    public NeedBraces_Rename(final Date date, final int number, final String text) {
        this.date = date;
        this.number = number;
        this.text = text;
    }

    /**
     * Calcs the absolute value.
     *
     * @param a
     *            number
     * @return a positive number
     */
    public int calcSth(final int abc) {
        if (abc >= 0) 
            return abc;
        else {
            return -abc;
        }
    }

    /**
     * Returns the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }
}