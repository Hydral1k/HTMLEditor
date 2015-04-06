/*
 * This class provides better organization.
 * All classes that extend this are undoable, and thus
 * have the ability to send a memento to the undomanager.
 * 
 * This is also a template pattern implementation, providing
 * the same structure for subclasses' execute() methods.
 * 
 * Classes to extend this: InsertCommand, CutCommand, PasteCommand,
 * KeyCommand, CollapseViewCommand
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.texteditor.TabData;
import javafx.event.Event;

/**
 * @author aac6012
 */
public abstract class UndoableCommand implements Command{
    /* This field will be inherited by subclasses */
    public HTMLEditor editor ;
    
    @Override
    public void execute(Event t) {
        this.saveState();
        operate(t) ;
        this.saveState();
    }
    
    public void saveState(){
        //This gets the TabData class from the current open tab.
        TabData tabdata = (TabData)this.editor.getCurrentTab().getUserData() ;
        
        //This gets the undoManager from TabData and sends it a memento.
        tabdata.getUndoManager().save(this.editor.createMemento());
    }
    
    abstract public void operate(Event t) ;
    
}
