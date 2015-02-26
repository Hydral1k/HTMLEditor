/*
 * The Command to create a new file.
 */
package htmleditor;

/**
 *
 * @author aac6012
 */
public class NewFileCommand implements Command {
    HTMLEditor editor ;
    
    public NewFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        editor.addNewTab() ;
    }
}
