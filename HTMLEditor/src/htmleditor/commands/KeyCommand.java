/*
 * This is the command for hitting a key on the keyboard.
 * It is used to handle being able to undo/redo in blocks of code
 * instead of individual characters.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author aac6012
 */
public class KeyCommand extends UndoableCommand {
    //Used to keep track of if the user is typing or deleting text.
    boolean isEntering ;
    
    public KeyCommand(HTMLEditor e) {
        this.editor = e ;
        isEntering = true ;
    }
    
    @Override
    /**
     * Execute must be overriden because it does not follow the same
     * pattern "save,operate,save".
     */
    public void execute(Event t){
        this.operate(t) ;
    }

    @Override
    public void operate(Event t) {
        //If user presses backspace..
            //if isEntering is true, save state.
            //if isEntering false, backspace as normal (i.e. do nothing).
        //If user presses any other character key
            //if isEntering true, enter character as usual (i.e. do nothing).
            //if isEntering false, save state.
        
        //Cast Event t to a new KeyEvent.
        KeyEvent k = (KeyEvent)t ;
        if (k.getCode().equals(KeyCode.BACK_SPACE) || k.getCode().isArrowKey()){
            if (this.isEntering){
                this.saveState() ;
                this.isEntering = false ;
            }
        }
        else if (k.getCode().isDigitKey()       ||
                 k.getCode().isLetterKey()      ||
                 k.getCode().isWhitespaceKey()  ||
                 k.getCode().isNavigationKey()){
            if (!this.isEntering){
                this.saveState();
                this.isEntering = true ;
            }
        }
        
    }
}
