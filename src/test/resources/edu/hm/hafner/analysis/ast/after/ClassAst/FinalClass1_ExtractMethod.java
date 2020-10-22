package edu.hm.hafner;

/**
 * Document type FinalClass1_ExtractMethod.
 *
 * @author Christian Möstl
 */
public class FinalClass1_ExtractMethod {
	private int x;
	
	private FinalClass1_ExtractMethod() {
		
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