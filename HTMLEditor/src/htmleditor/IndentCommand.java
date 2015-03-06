/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

/**
 *
 * @author Thomas Heissenberger (trh8614@rit.edu)
 */
/*
 * The Command to close the editor.
 */

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Tab;

/**
 *
 * @author aac6012
 */
public class IndentCommand implements Command{
    HTMLEditor editor ;
    IndentType type;
    
    
    public IndentCommand(HTMLEditor editor, IndentType t){
        this.editor = editor;
        this.type = t;
    }

    
    public void execute(Event t) {
        String indent = new String(new char[editor.indent_size]).replace("\0", " ");
        if(type == IndentType.INDENT_ALL){
            
            System.out.println("Indenting entire buffer...");
            
            String[] lines = editor.getBuffer().split("\n");
            String buffer_out = "";
            for (int i = 0; i < lines.length; i++) {
                buffer_out += indent + lines[i] + "\n"; 
            }
            editor.setBuffer(buffer_out);
        }else if( type == IndentType.INDENT_CURRENT_LINE){
            
            System.out.println("Indenting current line...");
            
            editor.insertIntoBufferAtPos(indent, editor.getCurrentLineStartPosition() + 1);
            
            
            
        }else if( type == IndentType.INDENT_SELECTION){     
            
            System.out.println("Indenting selection...");
            
        }
    }
}

enum IndentType{
    INDENT_CURRENT_LINE, INDENT_SELECTION, INDENT_ALL;
}