package edu.hm.hafner;

/**
 * Document type FinalClass5_ExtractConstant.
 *
 * @author Christian Möstl
 */
public class FinalClass5_ExtractConstant {

	private static final String HELLO = "hello";
	
	private int a;

    private FinalClass5_ExtractConstant() {

    }
    
    public void doSth() {
    	System.out.println(HELLO);
    }
}