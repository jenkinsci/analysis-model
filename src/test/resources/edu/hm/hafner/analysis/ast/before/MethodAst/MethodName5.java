package edu.hm.hafner;

/**
 * Document type MethodName5.
 *
 * @author Christian Möstl
 */
public class MethodName5 {
	private MethodName5() {
		
	}
	
	/**
	 * Do sth...
	 * 
	 * @param a
	 *            number
	 */
	public void DoSth(int a) {
		int b = 0;
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
		System.out.println(b);
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