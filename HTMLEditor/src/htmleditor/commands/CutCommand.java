/*
 * The command to cut text and save it to the clip tray.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class CutCommand extends UndoableCommand {

    public CutCommand(HTMLEditor e) {
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        this.saveState(); //Save before state change.
        
        String oldBuff = this.editor.getText().getText() ;
        //In case the if's don't catch, newBuff equals oldBuff by default.
        String newBuff = oldBuff ;
        
        String selection = this.editor.getBufferSelection() ;
        int pos = this.editor.getCarrotPosition() ;
        this.editor.setClipboard(selection) ;
        
        //If the cursor is at the beginning of the text to be cut.
        if(pos + selection.length() <= oldBuff.length()
            &&
            selection.equals(oldBuff.substring(pos, pos+selection.length()))){
                //Remove text from right of cursor
                newBuff = oldBuff.substring(0,pos) ;
                newBuff += oldBuff.substring(pos+selection.length()) ;
                
        }
        
        //If the cursor is at the end of the text to be cut.
        else if(pos - selection.length() > 0 
            &&
            selection.equals(oldBuff.substring(pos-selection.length(), pos))){
                //Remove text from left of cursor
                newBuff = oldBuff.substring(0,pos-selection.length()) ;
                newBuff += oldBuff.substring(pos) ;
                
        }
        
        //Update the buffer
        this.editor.setBuffer(newBuff) ;
        
        this.saveState() ; //Save after state changes.
    }   
}