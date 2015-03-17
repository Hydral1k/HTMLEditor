/*
 * The command to undo an undo operation (set state to original state).
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.TabData;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class RedoCommand implements Command {
    private HTMLEditor editor ;
    
    public RedoCommand(HTMLEditor e){
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        TabData tabdata = (TabData)this.editor.getCurrentTab().getUserData() ;
        tabdata.getUndoManager().redo() ;
    }
}
