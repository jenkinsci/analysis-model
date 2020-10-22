package Edu.hm.hafner;

import java.util.Date;

/**
 * Document type PackageName3Superclass.
 *
 * @author Christian Möstl
 */
public class PackageName3Superclass {
	private Date date;
	private int number;
	private final String text;
	
	/**
	 * Creates a new instance of {@link PackageName3Superclass}.
	 *
	 * @param date
	 *            Date
	 * @param number
	 *            number
	 * @param text
	 *            Text
	 */
	public PackageName3Superclass(final Date date, final int number, final String text) {
		this.date = date;
		this.number = number;
		this.text = text;
	}
	
	/**
	 * Do sth...
	 * 
	 * @param a
	 *            number
	 */
	public void doSth(int a) {
		int b = 0;
		if (a > 0) {
			System.out.println(">0");
			if (a == 1) {
				++b;
				System.out.println("a=1");
				if (a == 2) {
					System.out.println("a=2");
				}
			}
		}
		System.out.println(b);
	}
}