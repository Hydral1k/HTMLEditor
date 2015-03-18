/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.texteditor;

import htmleditor.HTMLAnalyzer;
import htmleditor.HTMLEditor;
import htmleditor.YesNoDialogBox;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author thn1069
 */
public class CloseListener implements EventHandler<Event>{
    /** 
     * Custom listener to handle the closing of a tab. 
     * The purpose of this is make sure the file is saved before closing in the buffer,
     * otherwise all the work since the last save will be lost.
     */
    private HTMLAnalyzer analyzer;
    private HTMLEditor editor;
    
    public CloseListener(HTMLEditor editor){
        this.analyzer = new HTMLAnalyzer();
        this.editor = editor;
    }

    @Override
    public void handle(Event t) {
        boolean changedText;
        
        Tab closedTab = (Tab) t.getTarget();
        changedText = this.editor.hasChanged(closedTab);
        if(!changedText) //if nothing changed, let them leave
            return;
        int result = YesNoDialogBox.show(this.editor.getStage(), "Warning!\nThe file you are closing contained unsaved changes.\nAre you sure you wish to close?");
        if( result == 1 )
            return;
        else{ //restore tab
            this.editor.getTabPane().getTabs().add(closedTab);
            this.editor.getTabPane().getSelectionModel().select(closedTab);
        }
    }
    
    
}
