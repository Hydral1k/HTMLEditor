/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.CloseListener;
import htmleditor.HTMLEditor;
import htmleditor.MyEventHandler;
import htmleditor.TabData;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author aac6012
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
