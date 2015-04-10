/*
 * The command to open the link selected from HTML buffer in a browser window.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import javafx.event.Event;

/**
 * @author aac6012
 */
public class OpenLinkCommand implements Command {
    private HTMLEditor editor ;
    
    public OpenLinkCommand(HTMLEditor e){
        this.editor = e ;
    }

    @Override
    public void execute(Event t) {
        String link = this.editor.getText().getSelectedText() ;
        this.editor.getLinkView().openLink(link) ;
    }
}
