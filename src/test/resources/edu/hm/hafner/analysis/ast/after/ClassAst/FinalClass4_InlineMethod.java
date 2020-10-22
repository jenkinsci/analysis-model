package edu.hm.hafner;

/**
 * Document type FinalClass4_InlineMethod.
 *
 * @author Christian Möstl
 */
public class FinalClass4_InlineMethod {
	private int x;
	
	private FinalClass4_InlineMethod() {
		
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