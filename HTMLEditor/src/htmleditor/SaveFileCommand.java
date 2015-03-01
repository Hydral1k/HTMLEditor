/*
 * The Command to save an alredy existing file.
 */
package htmleditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


/**
 * @author aac6012
 */
public class SaveFileCommand implements Command {
    HTMLEditor editor ;
    TabPane tabPane ;
    Stage stage ;
    
    public SaveFileCommand(HTMLEditor editor, TabPane tabpane, Stage stage){
        this.editor = editor;
        this.tabPane = tabpane ;
        this.stage = stage ;
    }
    
    @Override
    public void execute(){
        String fileName = editor.getFileName();
        if (fileName.equals("Untitled")){
            SaveAsCommand saveAs = new SaveAsCommand(editor, tabPane, stage) ;
            saveAs.execute() ; //If a file has not been saved yet, the user will be prompted to save a new file.
        }
        else{
            String htmlText = editor.getBuffer();
            File file = new File(editor.getFileName());
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file);
                System.out.println("Writing...") ;
                fileWriter.write(htmlText);
                System.out.println("Done Writing.") ;
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(SaveFileCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(editor.getBuffer());
    }
}
