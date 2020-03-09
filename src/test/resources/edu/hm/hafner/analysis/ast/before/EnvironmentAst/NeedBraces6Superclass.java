package edu.hm.hafner;

/**
 * Document type NeedBraces6Superclass.
 *
 * @author Christian Möstl
 */
public class NeedBraces6Superclass {
	/**
	 * Do sth.
	 * 
	 * @param a
	 *            number
	 * @param x
	 *            number
	 * @return sth
	 */
	public int extractMethod(final int a, int x) {
		if (a < 0)
			x = x - a;
		else {
			x = x + a;
		}
		return x;
	}
}