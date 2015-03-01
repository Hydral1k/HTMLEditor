/*
 * The Command to save a new file.
 */
package htmleditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.scene.control.Tab;
import javafx.stage.FileChooser;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;



/**
 * @author aac6012
 */
public class SaveAsCommand implements Command {
    HTMLEditor editor ;
    TabPane tabPane ;
    Stage stage ;

    public SaveAsCommand(HTMLEditor editor, TabPane tabPane, Stage stage){
        this.editor = editor ;
        this.tabPane = tabPane ;
        this.stage = stage ;
    }
    
    @Override
    public void execute(){
        String htmlText = editor.getBuffer();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save HTML");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html")) ;
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(htmlText);
                }
                Tab thisTab = tabPane.getSelectionModel().getSelectedItem();
                thisTab.setText(file.getName());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
        }
    }
}
