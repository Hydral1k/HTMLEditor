/*
 * The Command to save a new file.
 */
package htmleditor;

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

    public SaveAsCommand(HTMLEditor editor){
        this.editor = editor ;
    }
    
    public void execute(Event t){
        String htmlText = this.editor.getBuffer();
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
                thisTab.setText(file.getAbsolutePath());
            } catch (IOException ex) {
                    System.out.println(ex.getMessage());
            }
        }
    }
}
