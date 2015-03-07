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
import javafx.scene.control.TextArea;

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
        
        
        if(type == IndentType.INDENT_ALL){
            String indent = new String(new char[editor.indent_size]).replace("\0", " ");
            System.out.println("Indenting entire buffer...");
            
            String[] lines = editor.getBuffer().split("\n");
            String buffer_out = "";
            for (int i = 0; i < lines.length; i++) {
                buffer_out += indent + lines[i] + "\n"; 
            }
            editor.setBuffer(buffer_out);
        }else if( type == IndentType.INDENT_CURRENT_LINE){
            System.out.println("Indenting current line..." + editor.getPrevLine(editor.getCarrotPosition()));
            Integer depth = getDepthOfBuffer(editor.getPrevLine(editor.getCarrotPosition()));
            String indent = new String(new char[editor.indent_size]).replace("\0", " ");
            if(depth > 0){
                indent = new String(new char[depth]).replace("\0", " ");
            }
             
            editor.insertIntoBufferAtPos(indent, editor.getCurrentLineStartPosition() + 1);
            
        }else if( type == IndentType.INDENT_SELECTION){     
           
            System.out.println("Indenting selection...");
            
            TextArea ta = editor.getTextArea();
            
            String lside = ta.getText(0, ta.getSelection().getStart());
            Integer depth = getDepthOfBuffer(editor.getPrevLine(ta.getSelection().getStart()));
            String indent = new String(new char[editor.indent_size]).replace("\0", " ");
            
            if( depth > 0){
                indent = new String(new char[depth]).replace("\0", " ");
            }
            
            String selection = ta.getText(ta.getSelection().getStart(), ta.getSelection().getEnd());
            String rside = ta.getText(ta.getSelection().getEnd(), ta.getText().length());
            
            String[] lines = selection.split("\n");
            String buffer_out = "";
            for (int i = 0; i < lines.length; i++) {
                buffer_out += indent + lines[i];
                if( i < lines.length - 1){
                    buffer_out += "\n"; 
                }
                
            }
            
            editor.setBuffer(lside + buffer_out + rside);
            
        }
    }

    public Integer getDepthOfBuffer(String previousLine){
        int depth = 0;
        while( previousLine.length() > 0){
            String currChar = previousLine.substring(0, 1);
            previousLine = previousLine.substring(1, previousLine.length());
            if( currChar.equals(" ") ){
                depth ++;
            }else{
                return depth;
            }
        }
        return depth;
    }; 
}

enum IndentType{
    INDENT_CURRENT_LINE, INDENT_SELECTION, INDENT_ALL;
}