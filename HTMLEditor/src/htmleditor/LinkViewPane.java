package htmleditor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.VBox;

/**
 * Used to create and provide display logic for the Link View.
 * 
 * @author mss9627
 */
public class LinkViewPane extends VBox {
    
    private List<String> links;
    private HashMap<String, Integer> alphabeticalLinks;
    private final HTMLAnalyzer analyzer;
    
    public LinkViewPane( String buffer ){
        // Set up data structures for the display
        this.analyzer = new HTMLAnalyzer();
        this.links = new LinkedList<String>();
        this.alphabeticalLinks = new HashMap<String, Integer>();
        updateLinks( buffer );
    }
    
    /**
     * Given a list of the tags from the HTML buffer, extracts any links from
     * &lt;a href&gt; tags and places them into a list.
     * 
     * @param tags the list of all tags from a given HTML buffer
     * @return a list containing each 
     */
    private List<String> extractLinks( List<String> tags ){
        int end;
        
        // Loop through all HTML tags
        for( String t : tags ){
            // Remove whitespace (URLs do not contain whitespace)
            t = t.replaceAll("\\s", "");
            if( !t.contains("<ahref=\"") ){
                // Remove non-link tags
                tags.remove(t);
            } else {
                // Extract URL from link tag and keep it in the list
                t = t.replace("<ahref=\"", "");
                end = t.indexOf("\"");
                t = t.substring(0, end);
            }
        }
        
        return tags;
    }
    
    /**
     * Converts the in-order list of links into an alphabetical table.
     * The first copy of each link will be given a counter.
     * Subsequent copies are dropped and the counter on the first increases.
     * 
     * @param links the in-order list of URLs
     */
    private void createAlphabetical( List<String> links ){
        // Sort the links (may not work, still needs testing)
        Collections.sort(links);
        alphabeticalLinks.clear();
        for( String k : links ){
            if( !alphabeticalLinks.containsKey(k) ){
                // Add the new link into the map
                alphabeticalLinks.put(k, 1);
            } else {
                // Replace the old link by incrementing the counter
                alphabeticalLinks.put(k, (alphabeticalLinks.get(k) + 1) );
            }
        }
    }
    
    /**
     * Updates the lists of URLs with links from the current buffer.
     * Recreates each list, as links could potentially be added and removed.
     * 
     * @param buffer the current buffer
     */
    public final void updateLinks( String buffer ){
        links = extractLinks( analyzer.extractTags( buffer ) );
        createAlphabetical( links );
    }
    
}
