/*
 * The Command to toggle text wrapping.
 */
package htmleditor;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

/**
 * @author aac6012
 */
public class WrapTextSwitchCommand implements Command {
    HTMLEditor editor ;
    
    public WrapTextSwitchCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        Tab tab = this.editor.getTabPane().getSelectionModel().getSelectedItem() ;
        //If tab is null it will not attempt to change the text-wrap setting
        if (tab != null){
            TextArea ta = (TextArea) tab.getContent();
            ta.setWrapText(!ta.isWrapText());
        }
    }
}