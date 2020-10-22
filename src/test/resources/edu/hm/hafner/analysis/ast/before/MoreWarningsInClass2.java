package edu.hm.hafner;

/**
 * Document type MoreWarningsInClass2.
 *
 * @author Christian Möstl
 */
public class MoreWarningsInClass2 {
private int width = 0;
	
	private String text;
	
	public MoreWarningsInClass2(final int width, final String text) {
		this.width = width;
		this.text = text;
	}
	
	/**
	 * Sets the width to the specified value.
	 * 
	 * @param width
	 *            the value to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Returns the width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Do sth.
	 * 
	 * @param flag
	 *            flag
	 * @param a
	 *            number
	 * @return number
	 */
	public int doSth(final boolean flag, final int a) {
		System.out.println(flag);
		System.out.println(a);
		int x = 3;
		if (flag == true) {
			x = x - a;
		}
		else {
			x = x + a;
		}
		DoSth();
		return x;
	}
	
	private void DoSth() {
	    System.out.println("Hello");
	}
	
    /**
     * Do sth.
     * 
     * @param w
     *            the width
     * @param height
     *            the height
     */
    public void doSth(final int w, final int height) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Prints sth...");
        }
        int[][] result = new int[height][w];
        int size = 50;
        
        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++)
                result[row][col] = size;
            System.out.println("Next Step");
        }
        
        System.out.println("Half = " + result[height / 2][w / 2]);
    }
	
	/**
	 * Sets the text to the specified value.
	 * 
	 * @param text
	 *            the value to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Returns the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}