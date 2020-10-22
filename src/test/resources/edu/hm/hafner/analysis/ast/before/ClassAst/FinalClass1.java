package edu.hm.hafner;

/**
 * Document type FinalClass1.
 *
 * @author Christian Möstl
 */
public class FinalClass1 {
	private int x;
	
	private FinalClass1() {
		
	}
	
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