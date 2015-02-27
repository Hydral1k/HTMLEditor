/*
 * The Command to open a file.
 */
package htmleditor;

/**
 * @author aac6012
 */
public class WrapTextSwitchCommand implements Command {
    HTMLEditor editor ;
    
    public WrapTextSwitchCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    @Override
    public void execute(){
        this.editor.wrapTextSwitch();
    }
}