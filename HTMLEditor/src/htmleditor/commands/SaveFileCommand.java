/*
 * The Command to save an alredy existing file.
 */
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.HTMLEditor;
import htmleditor.YesNoDialogBox;
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
    HTMLAnalyzer analyzer;
    
    public SaveFileCommand(HTMLEditor editor){
        this.editor = editor;
        this.analyzer = new HTMLAnalyzer();
    }
    
    public void execute(Event t){
        String fileName = editor.getFileName();
        if (fileName.equals("Untitled")){
            SaveAsCommand saveAs = new SaveAsCommand(editor) ;
            saveAs.execute(t) ; //If a file has not been saved yet, the user will be prompted to save a new file.
        }
        else{
            // Check whether the buffer is well formed.
            int result;
            if( !analyzer.wellFormed(editor.getBuffer()) ){
                result = YesNoDialogBox.show(editor.getStage(), "The HTML file you are trying to save is not well formed.\nAre you sure that you wish to save?");
            } else {
                result = 1;
            }
            
            // Only save file if the user chooses to.
            if( result == 1 ){
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
        }
    }
}
