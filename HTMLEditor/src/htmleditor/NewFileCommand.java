/*
 * The Command to create a new file.
 */
package htmleditor;

import javafx.event.Event;

/**
 *
 * @author aac6012
 */
public class NewFileCommand implements Command {
    HTMLEditor editor ;
    
    public NewFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        editor.addNewTab() ;
    }
}
