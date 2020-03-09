package edu.hm.hafner;

/**
 * Document type MethodName4Subclass_PushDownMethod.
 *
 * @author Christian Möstl
 */
public class MethodName4Subclass_PushDownMethod extends MethodName4Superclass_PushDownMethod {
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