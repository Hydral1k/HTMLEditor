/*
 * The Command to toggle text wrapping.
 */
package htmleditor;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

/**
 * @author aac6012
 */
public class WrapTextSwitchCommand implements Command {
    HTMLEditor editor ;
    TabPane tabPane ;
    
    public WrapTextSwitchCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        TextArea ta = (TextArea) editor.getTabPane().getSelectionModel().getSelectedItem().getContent();
        ta.setWrapText(!ta.isWrapText());
    }
}