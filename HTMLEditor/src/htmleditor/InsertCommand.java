/*
 * Insert Command pattern for inserting HTML tags into the selected
 * item's buffer.
 */
package htmleditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 *
 * @author jlt8213
 * @author trh8614
 */
public class InsertCommand implements Command {
    HTMLEditor editor;
    TagType tag ;
    Map details;
    
    
    public InsertCommand(TagType tag){
        this.editor = HTMLEditor.getInstance();
        this.tag = tag;
    }
    
    /* Override if extra piping for tag information is needed */
    public InsertCommand(HTMLEditor editor, TagType tag, Map details ){
        this.editor = editor;
        this.tag = tag;
        this.details = details;
        
    }
    
    public void execute(Event t){
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
                
                ArrayList<Tag> children = new ArrayList<Tag>();
                children.add(new Tag("li", TagType.LIST_ITEM, "", new ArrayList<Tag>()));
                children.add(new Tag("li", TagType.LIST_ITEM, "", new ArrayList<Tag>()));
                children.add(new Tag("li", TagType.LIST_ITEM, "", new ArrayList<Tag>()));
                
                if(details.get("List Type") == "Number"){
                    symbol = "ol";
                }else if(details.get("List Type") == "Dictionary" ){
                    symbol = "ol";
                }else if(details.get("List Type") == "Bullet"){
                    symbol = "ul";
                }
                
                new_tag = new Tag(symbol, TagType.LIST, "", children);
                
                makeTag(new_tag);
                break;
            case TABLE:
                //Prompts for user input, then makes table after submission
                Stage stage = new Stage();
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("Table.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.showAndWait();    //should change later to avoid confusion!
                } catch (IOException ex) {
                    Logger.getLogger(InsertCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(Tag.TableRows != -1){ //check for new table data, temporary?
                    makeTable(Tag.TableRows, Tag.TableCols);
                    Tag.TableRows = -1;
                    Tag.TableCols = -1;
                }
                break;
            default:
                System.out.println("wrong tag, ya dingus"); 
                return;
        }
        
       
    }
    
    /* Makes an html table at cursor with specified rows and columns */
    public void makeTable(int rows, int cols){
        String indent = new String(new char[editor.indent_size]).replace("\0", " ");
        TextArea thisTA = this.editor.getText();
        String thisText = thisTA.getText();
        int position = thisTA.getCaretPosition();
        String newTable = "<table>\n";
        for(int i=0; i<rows; i++){
            newTable += indent + "<tr>\n";
            for(int j=0; j<cols; j++){
                newTable += indent + indent +"<td></td>\n";
            }
            newTable += indent + "</tr>\n";
        }
        newTable += "</table>\n";
        thisText = thisText.substring(0, position) 
            + newTable
            + thisText.substring(position, thisText.length());
        thisTA.setText(thisText);
        
        return;
    }
    
    
    public void makeTag(Tag tag){
         
        TextArea thisTA = this.editor.getText();
        String thisText = thisTA.getText();
        int position = thisTA.getCaretPosition();
        thisText = thisText.substring(0, position) 
            + tag
            + thisText.substring(position, thisText.length());
        thisTA.setText(thisText);
        
        while(thisText.charAt(position)!='>')
            position++;
        thisTA.positionCaret(position+1);
        
    };
    
}