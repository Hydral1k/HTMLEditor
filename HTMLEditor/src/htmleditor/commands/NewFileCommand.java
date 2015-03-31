/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.texteditor.CloseListener;
import htmleditor.HTMLEditor;
import htmleditor.texteditor.TabData;
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
        final TextArea lineNumbers = new TextArea("1");
        lineNumbers.setStyle("-fx-text-fill: black;"+
                             "-fx-background-color: lightgrey;"+
                             "-fx-font: Courier New;"+
                             "-fx-font-family: monospace;"+
                             "-fx-font-size: 12;");
        lineNumbers.setDisable(true);
        lineNumbers.setWrapText(true);
        lineNumbers.setPrefWidth(50);
        lineNumbers.autosize();
        tabBorderContent.setLeft(lineNumbers);
        
        // text area
        TextArea ta = new TextArea();
        ta.setStyle("-fx-text-fill: black;"+
                    "-fx-background-color: white;"+
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 12;");
        lineNumbers.scrollTopProperty().bindBidirectional(ta.scrollTopProperty());
        ta.setOnKeyReleased(new MyEventHandler(new TextAnalysisCommand(this.editor)));
        ta.setWrapText(true);
        ta.prefHeightProperty().bind(this.editor.getScene().heightProperty());
        ta.prefWidthProperty().bind(this.editor.getScene().widthProperty());
        ta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                lineNumbers.clear();
                int lineCount = new HTMLAnalyzer().lineCount(editor.getBuffer());
                Integer max_length = String.valueOf(lineCount).length();
                lineNumbers.setPrefWidth(50 + (10 * max_length));
                
                for (int i = 1; i <= lineCount; i++){
                    Integer curr_length = String.valueOf(i).length();
                    String spacing = new String(new char[max_length - curr_length + TOGGLER_SPACING]).replace("\0", " ");
                    lineNumbers.appendText(i + spacing + "[+]\n");
                }
            }
        });
        tabBorderContent.setRight(ta);
        
        tab.setContent(tabBorderContent);
        this.editor.getTabPane().getTabs().add(tab);
        this.editor.getTabPane().getSelectionModel().select(tab);
        
        //This saves the initial state to the newly created tab's undoManager.
        ((TabData)tab.getUserData()).getUndoManager().save(this.editor.createMemento());
        
    }
}
