import java.util.Date;

/**
 * Document type PackageDeclaration2Subclass.
 *
 * @author Christian Möstl
 */
public class PackageDeclaration2Subclass extends PackageDeclaration2Superclass {
	private Date date;
	private int number;
	private final String text;
	
	/**
	 * Creates a new instance of {@link PackageDeclaration2Subclass}.
	 *
	 * @param date
	 *            Date
	 * @param number
	 *            number
	 * @param text
	 *            Text
	 */
	public PackageDeclaration2Subclass(final Date date, final int number, final String text) {
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