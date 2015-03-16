/*
 * The Command to toggle text wrapping.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.TabData;
import javafx.event.Event;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

/**
 * WrapTextSwitchCommand, used for toggling wrapping on the TextArea
 * @author aac6012
 */
public class WrapTextSwitchCommand implements Command {
    HTMLEditor editor ;
    
    public WrapTextSwitchCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        Tab tab = this.editor.getTabPane().getSelectionModel().getSelectedItem() ;
        //If tab is null it will not attempt to change the text-wrap setting
        if (tab != null){
            TextArea ta = (TextArea) this.editor.getText();
            ta.setWrapText(!ta.isWrapText());
            //set wrap status
            TabData tData = (TabData)editor.getTabPane().getSelectionModel().getSelectedItem().getUserData();
            tData.setWordWrap(ta.isWrapText());
            MenuItem wrapMenu = (MenuItem)t.getTarget();
            if(ta.isWrapText())
                wrapMenu.setText("Wrap Text (On)");
            else{
                wrapMenu.setText("Wrap Text (Off)");
            }
        }
    }
}
