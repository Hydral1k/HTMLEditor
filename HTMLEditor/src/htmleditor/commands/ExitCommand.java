/*
 * The Command to close the editor.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.YesNoDialogBox;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Tab;

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
        boolean confirm;
        String message = "Warning!\nThese following files contain unsaved changes:\n";
        ArrayList<String> changedTabs = new ArrayList<String>();
        ObservableList<Tab> tabs = this.editor.getTabPane().getTabs();
        for( Tab thisTab : tabs ){
            if (editor.hasChanged(thisTab)){
                changedTabs.add(thisTab.getText());
            }
        }

        if (changedTabs.size() > 0){
            for(int i=0; i<changedTabs.size(); i++){
                message += changedTabs.get(i);
                if (changedTabs.size() > 1 && i < changedTabs.size()-1)
                    message += ", ";
            }
            message += "\nAre you sure you wish to close?";
            boolean changedText;
            int result = YesNoDialogBox.show(editor.getStage(), message);
            if( result == 1 ){
                System.exit(0);
                return;
            }
            else{ //cancel close
                System.out.println("CANCEL EXIT");
                t.consume();
                return;
            }
        }
    }
}
