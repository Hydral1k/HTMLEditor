/**
 * Used for the well-formed buffer check.
 * If a tag is created without a right angle bracket, this should be thrown.
 *
 * @author mss9627
 */
package htmleditor.texteditor;

public class NotWellFormedException extends Exception {
	
	public NotWellFormedException( String message ){
		super(message);
	}
	
}