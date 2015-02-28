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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        int nextTagLoc = 1;
        List<String> allTags = new ArrayList<String>();
        
        // This regular expression matches HTML tags.
        Pattern tagPattern = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^'\">])*>");
        Matcher tagFinder = tagPattern.matcher( bufferHTML );
		
        // Create a list containing all of the HTML tags.
	while( tagFinder.find() ){
            allTags.add( tagFinder.group(nextTagLoc) );
            nextTagLoc++;
	}
        
        String nextTag = "";
        
        //  Remove self-closing tags from the list.
        for( int x = 0; x < allTags.size(); x++ ){
            nextTag = allTags.get( x );
            
            if( nextTag.charAt( nextTag.length() - 2 ) == '/' ){
                x--;
                allTags.remove(x + 1);
            }
        }
        
        int closingLoc = -1;
        int openTags = 0;
        String firstTag = "";
        String lastTag  = "";
        
        while( allTags.size() > 0 ){
        
            // Store the first tag in the file.
            firstTag = getTagType( allTags.get( 0 ) );
            openTags = 1;
            closingLoc = 1;
            
            // Starting from the next tag, find the leftmost possible closing tag.
            while( openTags > 0 && closingLoc < allTags.size() ){
            
                // Add 1 for each matching opening tag.
                // Subtract 1 for each matching closing tag.
                lastTag = getTagType( allTags.get( closingLoc ) );
                if( firstTag.equals( lastTag ) ){
                    openTags++;
                } else if( firstTag.equals( lastTag.substring(1) ) &&
                           lastTag.charAt(0) == '/' ){
                    openTags--;
                }
                closingLoc++;
                
            }
            
            // If the opening tag has a closing tag, remove both.
            // If the first tag is a closing tag, this will always return false.
            if( openTags == 0 ){
                allTags.remove( closingLoc );
                allTags.remove( 0 );
            } else {
                return false;
            }
            
        }
		
        return true;
    }
	
    /**
     * Helper function for the wellFormed() check.
     * Given an HTML tag, extract the type at the beginning of the tag.
     *
     * @param tag The HTML tag being tested in the wellFormed() method.
     * @return the tag type, e.g. all text from the left bracket to the first space
     */
    private String getTagType( String tag ){
        tag = tag.substring(1); // Remove left bracket
        tag = tag.trim();       // Remove leading whitespace, if any
        int location = tag.indexOf(" ");
        
        // Truncate tag to first space if it exists.
        // If not, truncate before the right bracket.
        if( location >= 0 ){
            return tag.substring( 0, location );
        } else {
            return tag.substring( 0, tag.length() - 1) ;
        }
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
