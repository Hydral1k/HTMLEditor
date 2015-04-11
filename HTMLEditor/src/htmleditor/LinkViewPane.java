package htmleditor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Used to create and provide display logic for the Link View.
 * 
 * @author mss9627
 */
public class LinkViewPane extends VBox {
    
    final static boolean ALPHABETICAL = true;
    final static boolean IN_ORDER = false;
    private BorderPane pane = null;
    private ToolBar toolbar = null;
    private VBox view = null;
    private ScrollPane scroll = null;
    private List<String> links;
    private Text modeDisplay;
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
        this.pane = new BorderPane();
        this.editor = editor;
        this.links = new LinkedList<String>();
        this.alphabeticalLinks = new HashMap<String, Integer>();
        this.mode = isAlphabetical;
        this.scroll = new ScrollPane();
        updateLinks( editor.getBuffer() );
        
        view = new VBox();
        view.setLayoutX(10);
        view.setSpacing(10);
        update();
        
        this.toolbar = new ToolBar();
        addToolBarElements();
        
        scroll.setContent( view );

        pane.setMaxHeight( 200 );        
        pane.setTop( toolbar );
        pane.setCenter( scroll );
        pane.setVisible( true );
    }
    
    private void addToolBarElements(){
        if( toolbar == null ) return;
        
        this.modeDisplay = new Text();
        toolbar.setStyle( editor.getStyleCss() );
        Region rightAlign = new Region();
        HBox.setHgrow(rightAlign, Priority.ALWAYS);
       
        Button refresh = new Button( "Refresh" );
        Button alphabetical = new Button( "Alphabetical Mode" );
        Button inOrder = new Button( "In-Order Mode" );
        Button close = new Button( "Close" );
        
        modeDisplay.setText( createDisplayText() );
        
        refresh.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                update();
                modeDisplay.setText( createDisplayText() );
            }
        });
        
        alphabetical.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                setMode( ALPHABETICAL );
                update();
                modeDisplay.setText( createDisplayText() );
            }
        });
        
        inOrder.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                setMode( IN_ORDER );
                update();
                modeDisplay.setText( createDisplayText() );
            }
        });
        
        close.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                editor.showLinkView( false );
            }
        });
        
        toolbar.getItems().add( modeDisplay );
        toolbar.getItems().add( rightAlign );
        toolbar.getItems().add( alphabetical );
        toolbar.getItems().add( inOrder );
        toolbar.getItems().add( refresh );
        toolbar.getItems().add( close );
    }
    
    /**
     * Subroutine for the toolbar creation.
     * Sets the explanatory text on the left of the toolbar.
     * 
     * @return a formatted string describing the current mode and links
     */
    private String createDisplayText(){
        String helperText;
        
        if( mode == ALPHABETICAL ){
            helperText = "Displaying links in alphabetical order - ";
        } else {
            helperText = "Displaying links in order of appearence - ";
        }
        helperText = helperText + links.size() + " links found";
        
        return helperText;
    }
    
    /**
     * Given a list of the tags from the HTML buffer, extracts any links from
     * &lt;a href&gt; tags and places them into a list.
     * 
     * @param initialTags the list of all tags from a given HTML buffer
     * @return a list containing each 
     */
    private synchronized List<String> extractLinks( List<String> initialTags ){
        int end;
        // A temporary storage list, necessary to avoid a ConcurrentModificationException.
        List<String> tags = new LinkedList<String>();
        
        // Loop through all HTML tags
        for( String t : initialTags ){
            // Remove whitespace (URLs do not contain whitespace)
            t = t.replaceAll("\\s", "");
            if( t.contains("<ahref=\"") ){
                // Extract URL from link tag and keep it in the list
                t = t.replace("<ahref=\"", "");
                end = t.indexOf("\"");
                t = t.substring(0, end);
                tags.add( t );
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
     * Resets the elements in the link view with new links.
     */
    public void update(){    
        // Get new links and reset display
        updateLinks( editor.getBuffer() );
        System.out.println( links.size() );
        view.getChildren().clear();
        Text element;
        String nextLink;
        
        if( mode == IN_ORDER ){
            
            // Add all links from the current document 
            // Display in the order they were found
            for( String s : links ){
                nextLink = " - " + s;
                element = new Text( nextLink );
                view.getChildren().add( element );
            }
            
        } else {
            
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
    
    /**
     * @return a copy of the BorderPane containing the link view
     */
    public BorderPane getPane(){
        update();
        return pane;
    }
    
    
    /**
     * Opens the provided link in a browser window.
     * @param link - the link for the file to open.
     */
    public void openLink(String link){
        File file = new File(link);
        try {
            Desktop.getDesktop().open(file) ;
        } catch (IllegalArgumentException|IOException ex) {
            System.out.println("Unable to locate file.") ;
        }
        
    }
    
}
