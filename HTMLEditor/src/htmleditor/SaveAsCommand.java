/*
 * The Command to save a new file.
 */
package htmleditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

/**
 * @author aac6012
 */
public class SaveAsCommand implements Command {
    HTMLEditor editor ;
    
    public SaveAsCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        //Substitute this for actual implementation
        System.out.println("Save a new file!") ;
        editor.saveAsChooser();
        /*
        MenuItem cmItem2 = new MenuItem("Save Image");
        cmItem2.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Image");
                    FileWriter fileWriter = null;
                    File file = fileChooser.showSaveDialog(editor.stage);
                    if (file != null) {
                        try {
                            fileWriter = new FileWriter(file);
                            fileWriter.write(content);
                            fileWriter.close();
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
        );
        */
    }
}
