/*
 * The Command to save an alredy existing file.
 */
package htmleditor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;


/**
 * @author aac6012
 */
public class SaveFileCommand implements Command {
    HTMLEditor editor ;
    
    public SaveFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        String fileName = editor.getFileName();
        if (fileName.equals("Untitled")){
            SaveAsCommand saveAs = new SaveAsCommand(editor) ;
            saveAs.execute(t) ; //If a file has not been saved yet, the user will be prompted to save a new file.
        }
        else{
            String htmlText = editor.getBuffer();
            File file = new File(editor.getFileName());
            try {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(htmlText);
                }
            } catch (IOException ex) {
                Logger.getLogger(SaveFileCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(editor.getBuffer());
    }
}
