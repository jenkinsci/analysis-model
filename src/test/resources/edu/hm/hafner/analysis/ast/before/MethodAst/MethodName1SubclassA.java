package edu.hm.hafner;

/**
 * Document type MethodName1SubclassA.
 *
 * @author Christian Möstl
 */
public class MethodName1SubclassA extends MethodName1Superclass {
    /**
     * Prints sth.
     */
    public static void DoSth() {
        System.out.println("Not useful");
    }
    
    /**
     * Prints sth unuseful.
     */
    public static void print() {
    	DoSth();
    	System.out.println("Hello");
    }
}