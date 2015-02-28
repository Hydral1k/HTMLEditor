/*
 * The Command to save an alredy existing file.
 */
package htmleditor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Tab;

/**
 * @author aac6012
 */
public class SaveFileCommand implements Command {
    HTMLEditor editor ;
    
    public SaveFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        //Substitute this for actual implementation
        System.out.println("Save an already existing File!") ;
        String fileName = editor.getFileName();
        if (fileName.equals("Untitled")){
            editor.saveAsChooser();
        }
        else{
            String htmlText = editor.getBuffer();
            File file = new File(editor.getFileName());
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(htmlText);
                fileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(SaveFileCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(editor.getBuffer());
    }
}
