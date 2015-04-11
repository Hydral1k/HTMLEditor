/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.builders.texteditorbuilders.LineNumbersBuilder;
import htmleditor.builders.texteditorbuilders.TextAreaBuilder;
import htmleditor.texteditor.CloseListener;
import htmleditor.texteditor.TabData;
import javafx.event.Event;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author aac6012
 */
public class NewFileCommand implements Command {
    HTMLEditor editor ;
    final int TOGGLER_SPACING = 1;
    
    public NewFileCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        Tab tab = new Tab();
        tab.setOnClosed(new CloseListener(this.editor));
        tab.setText("Untitled");
        tab.setId("Untitled");
        tab.setUserData(new TabData(this.editor));
        
        BorderPane tabBorderContent = new BorderPane();
        
        // line numbers
        LineNumbersBuilder lineNumBuilder = new LineNumbersBuilder();
        final TextArea lineNumbers = lineNumBuilder.getProduct(editor);
        tabBorderContent.setLeft(lineNumbers);
        
        // text area
        TextAreaBuilder taBuilder = new TextAreaBuilder();
        TextArea ta = taBuilder.getProduct(this.editor, lineNumbers);
        tabBorderContent.setRight(ta);
     
        
        lineNumbers.scrollTopProperty().bindBidirectional(ta.scrollTopProperty());
        
        tab.setContent(tabBorderContent);
        this.editor.getTabPane().getTabs().add(tab);
        this.editor.getTabPane().getSelectionModel().select(tab);
        
        //This saves the initial state to the newly created tab's undoManager.
        ((TabData)tab.getUserData()).getUndoManager().save(this.editor.createMemento());
        
    }
}
