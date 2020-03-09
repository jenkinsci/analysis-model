package edu.hm.hafner;

/**
 * Document type PackageName1Superclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class PackageName1Superclass_PullUpMethod {
	/**
	 * Do sth...
	 * 
	 * @param a
	 *            number
	 */
	public void doSth(int a) {
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
}