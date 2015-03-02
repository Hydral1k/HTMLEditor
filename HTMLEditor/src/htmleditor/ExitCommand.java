/*
 * The Command to close the editor.
 */
package htmleditor;

import javafx.event.Event;

/**
 *
 * @author aac6012
 */
public class ExitCommand implements Command{
    HTMLEditor editor ;
    
    public ExitCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        System.exit(0);
    }
}
