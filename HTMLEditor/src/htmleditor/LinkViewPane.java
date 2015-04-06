package htmleditor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Used to create and provide display logic for the Link View.
 * 
 * @author mss9627
 */
public class LinkViewPane extends VBox {
    
    final boolean ALPHABETICAL = true;
    final boolean IN_ORDER = false;
    private VBox view = null;
    private List<String> links;
    private HashMap<String, Integer> alphabeticalLinks;
    private final HTMLAnalyzer analyzer;
    private final HTMLEditor editor;
    private boolean mode;
    
    /**
     * Creates a link view pane to display in the main editor.
     * Sets the initial links and mode according to user selections.
     * 
     * @param editor         The open HTMLEditor, used to find the open buffer.
     * @param isAlphabetical True if alphabetical mode is enabled.
     *                       ALPHABETICAL and IN_ORDER are provided as constants
     *                       in order to make the code more readable.
     */
    public LinkViewPane( HTMLEditor editor, boolean isAlphabetical ){
        // Set up data structures for the display
        this.analyzer = new HTMLAnalyzer();
        this.editor = editor;
        this.links = new LinkedList<String>();
        this.alphabeticalLinks = new HashMap<String, Integer>();
        this.mode = isAlphabetical;
        updateLinks( editor.getBuffer() );
        
        view = new VBox();
        view.setLayoutX(10);
        view.setSpacing(10);
        update();
        
        
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
    public void updateLinks( String buffer ){
        links = extractLinks( analyzer.extractTags( buffer ) );
        createAlphabetical( links );
    }
    
    /**
     * Updates the display mode for the current buffer.
     * 
     * @param isAlphabetical True if alphabetical mode is enabled.
     */
    public void setMode( boolean isAlphabetical ){
        mode = isAlphabetical;
    }
    
    /**
     * Updates and returns a copy of the current view.
     * 
     * @return the VBox containing the current links
     */
    public VBox getLinkView(){
        update();
        return view;
    }
    
    /**
     * Resets the elements in the link view with new links.
     */
    private void update(){
        
        // Get new links and reset display
        updateLinks( editor.getBuffer() );
        view.getChildren().clear();
        Text title, element;
        String nextLink;
        
        if( mode == IN_ORDER ){
            
            // Add all links from the current document 
            // Display in the order they were found
            title = new Text("Links - In order of appearence");
            view.getChildren().add( title );
            for( String s : links ){
                nextLink = " - " + s;
                element = new Text( nextLink );
                view.getChildren().add( element );
            }
            
        } else {
            
            title = new Text("Links - In alphabetical order");
            view.getChildren().add(title);
            
            // Sort the hash map of links by key (text in link)
            Map<String, Integer> sortMap;
            sortMap = new TreeMap<>( alphabeticalLinks );
            Set sortedLinks = sortMap.entrySet();
            Iterator i = sortedLinks.iterator();
            
            // Iterate through map and add each link to the view
            while( i.hasNext() ){
                Map.Entry nextInSorted = (Map.Entry) i.next();
                nextLink = " - " + nextInSorted.getKey() + " - ";
                
                if( nextInSorted.getValue().equals(1) ){
                    nextLink += nextInSorted.getValue() + " occurence";
                } else {
                    nextLink += nextInSorted.getValue() + " occurences";
                }
                
                // Add link to view, displays in alphabetical order
                element = new Text( nextLink );
                view.getChildren().add( element );
            }
        }
    }
    
}
