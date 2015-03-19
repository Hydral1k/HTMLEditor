/*
 * The command to paste text from the clip tray.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class PasteCommand extends UndoableCommand {

    public PasteCommand(HTMLEditor e) {
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        String oldBuff = this.editor.getText().getText() ;
        String newBuff ;
        
        String selection = this.editor.getBufferSelection() ;
        int pos = this.editor.getCarrotPosition() ;
        
        //Overwrite the selection with the pasted text.
        if (!selection.equals("")) {
            
            //Text is to the left of cursor
            if(pos + selection.length() <= oldBuff.length()
            &&
            selection.equals(oldBuff.substring(pos, pos+selection.length()))){
                //Remove text from right of cursor and insert pasted text
                newBuff = oldBuff.substring(0,pos) ;
                newBuff += this.editor.getClipboard() ;
                newBuff += oldBuff.substring(pos+selection.length()) ;
            }
            
            //Text is to the right of cursor
            else if(pos - selection.length() > 0 
            &&
            selection.equals(oldBuff.substring(pos-selection.length(), pos))){
                //Remove text from left of cursor and insert pasted text
                newBuff = oldBuff.substring(0,pos-selection.length()) ;
                newBuff += this.editor.getClipboard() ;
                newBuff += oldBuff.substring(pos) ;
                
            }
            
            //Unable to find text with respect to cursor
            else {
                System.out.println("You broke it.") ;
                //Don't change the buffer if something broke (damage control)
                newBuff = oldBuff ;
            }
            
        }
        
        //No text to overwrite because no selection.
        else {
            newBuff = oldBuff.substring(0,pos) ;
            newBuff += this.editor.getClipboard() ;
            newBuff += oldBuff.substring(pos) ;
        }
        
        
        this.editor.setBuffer(newBuff) ;
        
        this.saveState() ; //Save after state changes.
    }
}