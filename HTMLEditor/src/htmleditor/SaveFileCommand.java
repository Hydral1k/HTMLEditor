/*
 * The Command to save an alredy existing file.
 */
package htmleditor;

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
    }
}
