import java.util.Date;

/**
 * Document type PackageDeclarationSubclass_PullUpMethod.
 *
 * @author Christian Möstl
 */
public class PackageDeclarationSubclass_PullUpMethod extends PackageDeclarationSuperclass_PullUpMethod {
	 private Date date; 
	    private int number;
	    private final String text;
	    
	    /**
	     * Creates a new instance of {@link PackageDeclarationSubclass_PullUpMethod}.
	     *
	     * @param date
	     *            Date
	     * @param number
	     *            number
	     * @param text
	     *            Text
	     */
	    public PackageDeclarationSubclass_PullUpMethod(final Date date, final int number, final String text) {
	        this.date = date;
	        this.number = number;
	        this.text = text;
	    }
	        
	    /**
	     * Returns the date.
	     *
	     * @return the date
	     */
	    public Date getDate() {
	        return date;
	    }
	    
	    /**
	     * Returns the number.
	     *
	     * @return the number
	     */
	    public int getNumber() {
	        return number;
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