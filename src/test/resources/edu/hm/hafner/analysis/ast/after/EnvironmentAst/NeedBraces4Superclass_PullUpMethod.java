package edu.hm.hafner;

/**
 * Document type NeedBraces4Superclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class NeedBraces4Superclass_PullUpMethod {
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