/*
 * A model used for analyzing each HTML Editor session and 
 * verify if each is in proper form.
 *
 * @author trh8614
 * @author mss9627
 */
package htmleditor;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Analyzer Class
 */
public class HTMLAnalyzer {
    
    /**
     * Determines whether each HTML tag in the buffer has been properly closed at some point.
	 *
     * @param bufferHTML the current input buffer
     * @return true if no tag is left open throughout the input buffer
     */
    public boolean wellFormed( String bufferHTML ){
        //Must check that every open tag has a corresponding close tag.
		
		int currentLoc = 0;
		int charToRead = 0;
		List<String> openingTags = new ArrayList<String>();
		
		while( currentLoc < bufferHTML.length() && currentLoc >= 0 ){
		
			try {
				currentLoc = findNextTag( bufferHTML, currentLoc );
			} catch( NotWellFormedException e ){
				// This should only be reached if a tag is missing a right angle bracket.
				return false;
			}
			
		}
		
        return openingTags.size() == 0;
    }
	
	/**
	 * Helper function for the wellFormed() check.
	 * Finds the location of the next closed tag, if there is one.
	 *
	 * @param bufferHTML The full HTML buffer from the wellformed() check.
	 * @param start      The location after the previously found tag. 
	 * @return the location of the next tag, whether it's an opening or closing tag
	 */
	private int findNextTag( String bufferHTML, int start ) throws NotWellFormedException {
	
		// Simplify the temporary buffer to make the next bit easier.
		bufferHTML = bufferHTML.substring( start );
		int currentLoc = 0;
		int tempLoc = 0;
		
		
		if( currentLoc >= bufferHTML.length() ){
			return -1; // This will cause the wellFormed check to stop looking for tags.
		}
                else return 0; //Prevents a void return 
	}
	
	/**
	 * Helper function for the findNextTag() check.
	 * 
	 * @param bufferHTML The truncated buffer from the findNextTag() check.
	 * @param loc        The starting location which needs to be skipped.
	 * @return the number of characters to skip in the findNextTag() method
	 */
	private int skip( String bufferHTML, int loc ){
		bufferHTML = bufferHTML.substring( loc );
		loc = 0;
                return 0; //Replace with necessary return value.
	}
    
    /**
     * Another fancy comment about what this does
     * @param bufferHTML
     * @return 
     */
    public String autoIndent( String bufferHTML ){
        
        // we return bufferHTML after it has been properly indented. 
        
        return bufferHTML;
    }
    
    /**
     * Counts the number of words in a given HTML buffer.
     * @param bufferHTML
     * @return count
     */
    public int wordCount( String bufferHTML ){
        int count = 0 ;
        //Count the number of words
		
		// This will approximate the right answer for now.
        return bufferHTML.length() / 5 ;
    }
    
    /**
     * Counts the number of lines in a given HTML buffer.
     * @param bufferHTML
     * @return 
     */
    public int lineCount( String bufferHTML ){
        int count = 0 ;
        
		for( int x = 0; x < bufferHTML.length(); x++ ){
			if( bufferHTML.charAt(x) == '\n' ){
				count++;
			}
		}
		
        return count ;
    }
    
    // more functions will go in here. check the Domain Model if you would
    // like to add more and add the functions in there
    
}
