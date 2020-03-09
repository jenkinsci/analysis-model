package edu.hm.hafner;

/**
 * Document type MethodName3_ExtractMethod.
 *
 * @author Christian Möstl
 */
public class MethodName3_ExtractMethod {
	private MethodName3_ExtractMethod() {
		
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
		sum = extractMethod(sum);
		System.out.println(sum);
	}
	
	private int extractMethod(int sum) {
		for (int i = 0; i < 11; i++) {
			sum += i;
		}
		return sum;
	}
}