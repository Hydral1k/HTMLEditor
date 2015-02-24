/*
 * The Command to close the editor.
 */
package htmleditor;

/**
 *
 * @author aac6012
 */
public class ExitCommand implements Command{
    HTMLEditor editor ;
    
    public ExitCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        editor.closeApp();
    }
}
