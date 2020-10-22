package edu.hm.hafner;

/**
 * Document type ExplicitInitialization3Superclass.
 *
 * @author Christian Möstl
 */
public class ExplicitInitialization3Superclass {
	/**
	 * Do sth...
	 * 
	 * @param a
	 *            number
	 */
	public void doSth(final int a) {
		int b = 0;
		if (a < 0) {
			System.out.println("a<0");
		}
		if (a > 0) {
			System.out.println(">0");
			if (a == 1) {
				++b;
				System.out.println("a=1");
			}
		}
		System.out.println(b);
	}
}