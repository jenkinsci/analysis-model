package edu.hm.hafner;

import java.util.Date;

/**
 * Document type NeedBraces3Subclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class NeedBraces3Subclass_PullUpMethod extends NeedBraces3Superclass_PullUpMethod {
	private final Date date;
	private final int number;
	private final String text;
	
	/**
	 * Creates a new instance of {@link NeedBraces3Subclass_PullUpMethod}.
	 *
	 * @param date
	 *            Date
	 * @param number
	 *            number
	 * @param text
	 *            Text
	 */
	public NeedBraces3Subclass_PullUpMethod(final Date date, final int number, final String text) {
		this.date = date;
		this.number = number;
		this.text = text;
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