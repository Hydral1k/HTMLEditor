/*
 * Insert Command pattern for inserting HTML tags into the selected
 * item's buffer.
 */
package htmleditor;

import java.util.Map;
import javafx.scene.control.TextArea;

/**
 *
 * @author jlt8213
 * @author trh8614
 */
public class InsertCommand implements Command {
    HTMLEditor editor ;
    Tag tag ;
    Map details;
    
    
    public InsertCommand(HTMLEditor editor, Tag tag){
        this.editor = editor;
        this.tag = tag;
    }
    
    /* Override if extra piping for tag information is needed */
    public InsertCommand(HTMLEditor editor, Tag tag, Map details ){
        this.editor = editor;
        this.tag = tag;
        this.details = details;
        
    }
    
    
    @Override
    public void execute(){
        String symbol = "";
        //NOTE - header, list, table require user interactions
        switch (tag){
            case HEADER:
                symbol = "h" + details.get("Header Type");
                break;
            case BOLD:
                symbol = "b";
                break;
            case ITALICS:
                symbol = "i";
                break;
            case LIST:
                break;
            case TABLE:
                break;
            default:
                System.out.println("wrong tag, ya dingus");
                return;
        }
        TextArea thisTA = this.editor.getText();
        String thisText = thisTA.getText();
        int position = thisTA.getCaretPosition();
        thisText = thisText.substring(0, position) 
            + "<" + symbol + "></" + symbol + ">" 
            + thisText.substring(position, thisText.length());
        thisTA.setText(thisText);
        thisTA.positionCaret(position + 2 + symbol.length()); //sets cursor after opening tag
    }
}