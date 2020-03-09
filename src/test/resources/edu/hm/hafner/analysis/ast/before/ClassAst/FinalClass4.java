package edu.hm.hafner;

/**
 * Document type FinalClass4.
 *
 * @author Christian Möstl
 */
public class FinalClass4 {
	private int x;
	
	private FinalClass4() {
		
	}
	
	/**
	 * Do sth.
	 * 
	 * @param number
	 *            number
	 */
	public static void doSth(final int number) {
		System.out.println("Hello");
		int result = extractMethod();
		System.out.println(number + result);
	}

	private static int extractMethod() {
		int a = 3;
		int b = 7;
		int result = a + b;
		return result;
	}
}