/*
 * The Command to save a new file.
 */
package htmleditor;

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
    }
}
