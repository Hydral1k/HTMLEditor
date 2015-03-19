/*
 * The command to copy text and save it to the clip tray.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class CopyCommand implements Command {
    HTMLEditor editor ;
    
    public CopyCommand(HTMLEditor e) {
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        String selection = this.editor.getText().getSelectedText() ;
        this.editor.setClipboard(selection) ;
    }
}