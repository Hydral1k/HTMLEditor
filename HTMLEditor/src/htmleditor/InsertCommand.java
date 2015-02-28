/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import javafx.scene.control.TextArea;

/**
 *
 * @author jlt8213
 */
public class InsertCommand implements Command {
    HTMLEditor editor ;
    Tag tag ;
    
    public InsertCommand(HTMLEditor editor, Tag tag){
        this.editor = editor;
        this.tag = tag;
    }
    
    
    @Override
    public void execute(){
        String symbol = "";
        //NOTE - header, list, table require user interactions
        switch (tag){
            case HEADER:
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