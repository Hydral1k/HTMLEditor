/*
 * Insert Command pattern for inserting HTML tags into the selected
 * item's buffer.
 */
package htmleditor;

import java.util.ArrayList;
import java.util.Map;
import javafx.scene.control.TextArea;

/**
 *
 * @author jlt8213
 * @author trh8614
 */
public class InsertCommand implements Command {
    HTMLEditor editor ;
    TagType tag ;
    Map details;
    
    
    public InsertCommand(HTMLEditor editor, TagType tag){
        this.editor = editor;
        this.tag = tag;
    }
    
    /* Override if extra piping for tag information is needed */
    public InsertCommand(HTMLEditor editor, TagType tag, Map details ){
        this.editor = editor;
        this.tag = tag;
        this.details = details;
        
    }
    
    
    @Override
    public void execute(){
        String symbol = "";
        //NOTE - header, list, table require user interactions
        Tag new_tag;
        switch (tag){
            case HEADER:
                symbol = "h" + details.get("Header Type");
                new_tag = new Tag(symbol, TagType.HEADER, "", new ArrayList<Tag>());
                
                makeTag(new_tag);
                break;
            case BOLD:
                symbol = "b";
                new_tag = new Tag(symbol, TagType.BOLD, "", new ArrayList<Tag>());
                makeTag(new_tag);
                break;
            case ITALICS:
                symbol = "i";
                new_tag = new Tag(symbol, TagType.ITALICS, "", new ArrayList<Tag>());
                makeTag(new_tag);
                break;
            case LIST:
                // TBA
                symbol = "";
                break;
            case TABLE:
                break;
            default:
                System.out.println("wrong tag, ya dingus"); 
                return;
        }
        
       
    }
    
    
    public void makeTag(Tag tag){
         
        TextArea thisTA = this.editor.getText();
        String thisText = thisTA.getText();
        int position = thisTA.getCaretPosition();
        thisText = thisText.substring(0, position) 
            + tag.toString()
            + thisText.substring(position, thisText.length());
        thisTA.setText(thisText);
        
        // Need's to be rethought somehow.
        
        //thisTA.positionCaret(position + 2 + symbol.length()); //sets cursor after opening tag
        
    };
    
}