package edu.hm.hafner;

import java.util.Date;

/**
 * Document type NeedBraces10.
 *
 * @author Christian Möstl
 */
public class NeedBraces10 {
	private final Date date;
	private final int number;
	private final String text;
	
	/**
	 * Creates a new instance of {@link NeedBraces10}.
	 *
	 * @param date
	 *            Date
	 * @param number
	 *            number
	 * @param text
	 *            Text
	 */
	public NeedBraces10(final Date date, final int number, final String text) {
		this.date = date;
		this.number = number;
		this.text = text;
	}
	
	/**
	 * Do sth.
	 * 
	 * @param a
	 *            number
	 * @return return sth.
	 */
	public int doSth(final int a) {
		System.out.println("hello");
		int x = 3;
		if (a < 0)
			x = x - a;
		else {
			x = x + a;
		}
		
		return x;
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