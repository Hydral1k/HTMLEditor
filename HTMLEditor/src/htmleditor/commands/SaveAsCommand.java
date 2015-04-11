/*
 * The Command to save a new file.
 */
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.HTMLEditor;
import htmleditor.YesNoDialogBox;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.Event;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;


/**
 * @author aac6012
 */
public class SaveAsCommand implements Command {
    HTMLEditor editor ;
    HTMLAnalyzer analyzer;

    public SaveAsCommand(HTMLEditor editor){
        this.editor = editor ;
        this.analyzer = new HTMLAnalyzer();
    }
    
    public void execute(Event t){
        String htmlText = this.editor.getBuffer();
        // Check whether the buffer is well formed.
        int result;
        if( !analyzer.wellFormed(htmlText) ){
            result = YesNoDialogBox.show(editor.getStage(), "The HTML file you are trying to save is not well formed.\nAre you sure that you wish to save?");
        } else {
            result = 1;
        }
        if( result == 1 ){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save HTML");
            //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html")) ;
            File file = fileChooser.showSaveDialog(this.editor.getStage());
            if (file != null) {
                try{
                    try (FileWriter fileWriter = new FileWriter(file)) {
                        fileWriter.write(htmlText);
                    }
                    Tab thisTab = this.editor.getTabPane().getSelectionModel().getSelectedItem();
                    thisTab.setId(file.getAbsolutePath());
                    thisTab.setText(file.getName());
                } catch (IOException ex) {
                }
            }
        }
    }
}
