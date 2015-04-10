<<<<<<< HEAD
/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.texteditor.CloseListener;
import htmleditor.HTMLEditor;
import htmleditor.builders.texteditorbuilders.LineNumbersBuilder;
import htmleditor.builders.texteditorbuilders.TextAreaBuilder;
import htmleditor.texteditor.TabData;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

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
=======
/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.texteditor.CloseListener;
import htmleditor.HTMLEditor;
import htmleditor.builders.texteditorbuilders.LineNumbersBuilder;
import htmleditor.builders.texteditorbuilders.TextAreaBuilder;
import htmleditor.texteditor.TabData;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

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
>>>>>>> c7881afa7a993e0913768534e4c51b772471f295
