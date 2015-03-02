/*
 * The Command to open a file.
 */
package htmleditor;

import javafx.event.Event;

/**
 * @author aac6012
 */
public class OpenFileCommand implements Command {
    HTMLEditor editor ;
    
    public OpenFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        this.editor.openFile();
    }
}