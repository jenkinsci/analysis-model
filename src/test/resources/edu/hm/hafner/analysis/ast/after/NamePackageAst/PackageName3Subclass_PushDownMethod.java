package edu.hm.hafner;

import java.util.Date;

/**
 * Document type PackageName3Subclass_PushDownMethod.
 *
 * @author Christian Möstl
 */
public class PackageName3Subclass_PushDownMethod extends PackageName3Superclass_PushDownMethod {
	
	/**
	 * Creates a new instance of {@link PackageName3Subclass_PushDownMethod}.
	 * 
	 * @param date
	 *            date
	 * @param number
	 *            number
	 * @param text
	 *            text
	 */
	public PackageName3Subclass_PushDownMethod(Date date, int number, String text) {
		super(date, number, text);
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