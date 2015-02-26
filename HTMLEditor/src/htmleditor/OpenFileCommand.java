/*
 * The Command to open a file.
 */
package htmleditor;

/**
 * @author aac6012
 */
public class OpenFileCommand implements Command {
    HTMLEditor editor ;
    
    public OpenFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        //Substitute this for actual implementation
        System.out.println("Open a File!") ;
    }
}