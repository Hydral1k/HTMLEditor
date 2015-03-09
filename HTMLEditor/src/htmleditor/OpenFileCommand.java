/*
 * The Command to open a file.
 */
package htmleditor;

import javafx.event.Event;

/**
 * @author aac6012
 */
public class OpenFileCommand implements Command {
    HTMLEditor editor ;
    HTMLAnalyzer analyzer;
    
    public OpenFileCommand(HTMLEditor editor){
        this.editor = editor;
        this.analyzer = new HTMLAnalyzer();
    }
    
    public void execute(Event t){
        this.editor.openFile(editor.requestFile());
        int result;
            if( !analyzer.wellFormed(editor.getBuffer()) ){
                result = YesNoDialogBox.show(editor.getStage(), "The HTML file you are trying to open is not well formed.\nOpen file with reduced functionality?");
            } else {
                result = 1;
            }
            if (result == 0){
                editor.closeCurrentTab();
            }
    }
}