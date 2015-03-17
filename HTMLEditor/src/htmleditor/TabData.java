/*
 * Stores data about each tab
 * This can be very useful! Would've made things easier if I'd thought of it earlier
 */
package htmleditor;

import htmleditor.undomanager.UndoManager;
import javafx.scene.control.Tab;

/**
 *
 * @author jlt8213
 */
public class TabData {

    private boolean wordWrap ;
    private boolean autoIndent ;
    private int indentSpace ; //default indent length
    private UndoManager undoManager ;
    private String clipTray ; //for cut/copy + paste
    
    public TabData(HTMLEditor e) {
        this.wordWrap = true ;
        this.autoIndent = true ;
        this.indentSpace = 4 ;
        this.undoManager = new UndoManager(e) ;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public boolean isWordWrap() {
        return wordWrap;
    }
    
    public UndoManager getUndoManager(){
        return this.undoManager ;
    }
    
    
}
