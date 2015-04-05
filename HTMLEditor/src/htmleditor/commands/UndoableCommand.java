/*
 * This class provides better organization.
 * All classes that extend this are undoable, and thus
 * have the ability to send a memento to the undomanager.
 * 
 * Classes to extend this: InsertCommand, CutCommand, PasteCommand,
 * TextEntryCommand, CollapseViewCommand
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.texteditor.TabData;

/**
 * @author aac6012
 */
public abstract class UndoableCommand implements Command{
    /* This field will be inherited by subclasses */
    public HTMLEditor editor ;
    
    /* A note about the children's execute() implementation:
       To ensure each state is saved with text block entry/deletion,
       the implementation is structured as:
            saveState()
            [implementation]
            saveState()
       There is a checker in save() of UndoManager to prevent duplicates.
    */
    
    public void saveState(){
        //This gets the TabData class from the current open tab.
        TabData tabdata = (TabData)this.editor.getCurrentTab().getUserData() ;
        
        //This gets the undoManager from TabData and sends it a memento.
        tabdata.getUndoManager().save(this.editor.createMemento());
    }
}
