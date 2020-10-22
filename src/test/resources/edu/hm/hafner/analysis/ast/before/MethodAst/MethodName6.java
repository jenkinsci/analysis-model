package edu.hm.hafner;

/**
 * Document type MethodName6.
 *
 * @author Christian Möstl
 */
public class MethodName6 {
	private MethodName6() {
		
	}
	
	/**
	 * Do sth...
	 * 
	 * @param a
	 *            number
	 */
	public void DoSth(int a) {
		int b = 0;
		b = extractMethod(a, b);
		System.out.println(b);
	}
	
	private int extractMethod(int a, int b) {
		if (a > 0) {
			System.out.println(">0");
			if (a == 1) {
				++b;
				System.out.println("a=1");
				if (a == 2) {
					System.out.println("a=2");
				}
			}
		}
		return b;
	}
	
	
	/**
	 * Do sth..
	 */
	public void doSth2() {
		int sum = 0;
		for (int i = 0; i < 11; i++) {
			sum += i;
		}
		System.out.println(sum);
	}
}