/*
 * The command to undo operations (revert state).
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.TabData;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class UndoCommand implements Command {
    private HTMLEditor editor ;
    
    public UndoCommand(HTMLEditor e){
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        System.out.println("Reverting state...") ;
        TabData tabdata = (TabData)this.editor.getCurrentTab().getUserData() ;
        tabdata.getUndoManager().undo() ;
    }
}
