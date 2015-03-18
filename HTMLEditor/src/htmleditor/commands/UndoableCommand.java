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
    
    public void saveState(){
        //This gets the TabData class from the current open tab.
        TabData tabdata = (TabData)this.editor.getCurrentTab().getUserData() ;
        
        //This gets the undoManager from TabData and sends it a memento.
        tabdata.getUndoManager().save(this.editor.createMemento());
    }
}
