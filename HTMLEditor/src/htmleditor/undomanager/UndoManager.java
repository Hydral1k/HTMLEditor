/*
 * This is the caretaker (Memento pattern) for each tab's state.
 */
package htmleditor.undomanager;

import htmleditor.HTMLEditor;
import java.util.Stack;
import javafx.scene.control.Tab;

/**
 * @author aac6012
 */
public class UndoManager {
    
    /*
     * The purpose of lastUndo: when you hit undo after you previously
     * hit undo, undo will work correctly.  If you previously hit redo
     * then undo will not work correctly (you must hit it one more time).  
     * If lastUndo is true then the last operation was an undo operation.
     */
    
    private boolean lastUndo ;
    private Stack<Object> undoStack ;
    private Stack<Object> redoStack ;
    private HTMLEditor editor ;
    
    public UndoManager (HTMLEditor e) {
        this.undoStack = new Stack() ;
        this.redoStack = new Stack() ;
        this.editor = e ;
        this.lastUndo = false ;
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
        //Pushes a new state onto the undoStack.
        this.undoStack.push(m) ;
        
        //Clear the redoStack after a new operation happens.
        this.redoStack.clear() ;
        
        System.out.println(this.undoStack) ;
        System.out.println(this.redoStack) ;
    }
    
    /**
     * Tells the tab to revert to the previous state
     * and updates the stacks accordingly.
     */
    public void undo() {
        //If undoStack is empty, no undo operation can be performed.
        if (this.undoStack.isEmpty()){
            System.out.println("Undo Stack empty") ;
            return ;
        }
        
        /* If the last operation was redo, you must do move the top of 
         * redoStack to undoStack before trying to undo normally.      */
        if (!this.lastUndo && this.undoStack.size() > 1) {
            this.redoStack.push(this.undoStack.pop()) ;
            this.lastUndo = true ;
        }
        
        //Set tab state back to top of undoStack
        this.editor.setState(undoStack.peek()) ;
        
        //Then push the top of undoStack to top of redoStack.
        this.redoStack.push(this.undoStack.pop()) ;
        
        System.out.println(this.undoStack) ;
        System.out.println(this.redoStack) ;
    }
    
    /**
     * Tells the tab to revert back to the more recent state
     * and updates the stacks accordingly.
     */
    public void redo() {
        //If redoStack is empty, do nothing.
        if (this.redoStack.isEmpty()){
            System.out.println("Redo Stack empty") ;
            //Redo stack is empty.
            return ;
        }
        
        /* If the last operation was undo, you must do move the top of 
         * undoStack to redoStack before trying to redo normally.      */
        if (this.lastUndo && this.redoStack.size() > 1) {
            this.undoStack.push(this.redoStack.pop()) ;
            this.lastUndo = false ;
        }
        
        //Set tab state back to top of redoStack
        this.editor.setState(redoStack.peek()) ;
        
        //Then push the top of redoStack to top of undoStack.
        this.undoStack.push(this.redoStack.pop()) ;
        
        
        System.out.println(this.undoStack) ;
        System.out.println(this.redoStack) ;
    }   
}