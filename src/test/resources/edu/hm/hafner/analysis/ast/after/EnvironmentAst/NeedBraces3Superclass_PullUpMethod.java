package edu.hm.hafner;

/**
 * Document type NeedBraces3Superclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class NeedBraces3Superclass_PullUpMethod {
	/**
	 * Do sth.
	 * 
	 * @param a
	 *            number
	 * @return return sth.
	 */
	public int doSth(final int a) {
		System.out.println(a);
		int x = 3;
		if (a < 0)
			x = x - a;
		else {
			x = x + a;
		}
		
		return x;
	}
}