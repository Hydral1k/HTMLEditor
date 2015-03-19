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
        int pos = this.editor.getCarrotPosition() ;
        String oldBuff = this.editor.getText().getText() ;
        
        String newBuff = oldBuff.substring(0,pos) ;
        newBuff += this.editor.getClipboard() ;
        newBuff += oldBuff.substring(pos) ;
        
        this.editor.setBuffer(newBuff) ;
        
        this.saveState() ; //Save after state changes.
    }   
}