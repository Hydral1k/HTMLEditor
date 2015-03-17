/*
 * The Command to make objects out of a buffer
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;



/**
 *
 * @author jlt8213
 */
public class ObjectCommand implements Command {
    HTMLEditor editor ;
    
    public ObjectCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        System.out.println("Objectify stub");
        
    }
}
