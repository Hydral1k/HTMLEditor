/*
 * The Command to make objects out of a buffer
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;
import javafx.scene.control.TextArea;



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
        TextArea thisTA = editor.getText();
        
    }
}
