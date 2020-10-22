package edu.hm.hafner;

/**
 * Document type FinalClass3Subclass_PushDownMethod.
 *
 * @author Christian Möstl
 */
public class FinalClass3Subclass_PushDownMethod extends FinalClass3Superclass_PushDownMethod {
	private int x;
	
	private FinalClass3Subclass_PushDownMethod() {
		
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