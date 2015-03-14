/*
 * This is the caretaker (Memento pattern) for each tab's state.
 */
package htmleditor.undomanager;

import java.util.Stack;
import javafx.scene.control.Tab;

/**
 * @author aac6012
 */
public class UndoManager {
    
    private Stack<Object> undoStack ;
    private Stack<Object> redoStack ;
    private Tab tab ;
    
    public UndoManager (Tab t) {
        this.undoStack = new Stack() ;
        this.redoStack = new Stack() ;
        this.tab = t ;
    }
    
    /**
     * Saves the most recent state (after an undoable operation is
     * performed) and updates the stacks accordingly
     * 
     * @param m - The object that handles the state of the tab.
     * m is of type Object and not Memento because Memento 
     * is a private class in HTMLEditor (encapsulation).
     */
    public void save(Object m) {
        this.undoStack.push(m) ;
        this.redoStack.clear() ;
    }
    
    /**
     * Tells the tab to revert to the previous state
     * and updates the stacks accordingly.
     */
    public void undo() {
        //Set tab state back to top of undoStack
        //tab.setState(undoStack.peek()) ;
        
        //Then push the top of undoStack to top of redoStack.
        this.redoStack.push(this.undoStack.pop()) ;
    }
    
    /**
     * Tells the tab to revert back to the more recent state
     * and updates the stacks accordingly.
     */
    public void redo() {
        //Set tab state back to top of redoStack
        //tab.setState(redoStack.peek()) ;
        
        //Then push the top of redoStack to top of undoStack.
        this.undoStack.push(this.redoStack.pop()) ;
    }   
}