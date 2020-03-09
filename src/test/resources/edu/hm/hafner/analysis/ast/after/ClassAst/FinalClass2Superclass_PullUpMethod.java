package edu.hm.hafner;

/**
 * Document type FinalClass2Superclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class FinalClass2Superclass_PullUpMethod {
	/**
	 * Do sth.
	 * 
	 * @param number
	 *            number
	 */
	public static void doSth(final int number) {
		System.out.println("Hello");
		int a = 3;
		int b = 7;
		int result = a + b;
		System.out.println(number + result);
	}
}