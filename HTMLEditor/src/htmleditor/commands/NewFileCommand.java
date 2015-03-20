/*
 * The Command to create a new file.
 */
package htmleditor.commands;

import htmleditor.texteditor.CloseListener;
import htmleditor.HTMLEditor;
import htmleditor.texteditor.TabData;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author aac6012
 */
public class NewFileCommand implements Command {
    HTMLEditor editor ;
    
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
        /*
        TextArea lineNumbers = new TextArea("1");
        lineNumbers.setDisable(true);
        lineNumbers.setWrapText(true);
        lineNumbers.setPrefWidth(20);
        tabBorderContent.setLeft(lineNumbers);
        */
        GridPane lineNumbers = new GridPane();
        lineNumbers.addEventHandler(EventType.ROOT, null);
        Label lineno = new Label(" 1 ");
        lineno.setStyle("-fx-padding: 4;"+
                        "-fx-font-size: 10;");
        GridPane.setConstraints(lineno, 3, 1); // column=3 row=1

        lineNumbers.getChildren().addAll(lineno);
        tabBorderContent.setLeft(lineNumbers);
        
        
        // text area
        TextArea ta = new TextArea();
        ta.setStyle("-fx-text-fill: black;"+
                    "-fx-background-color: white;"+
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 12;");
        ta.setOnKeyReleased(new MyEventHandler(new TextAnalysisCommand(this.editor)));
        ta.setWrapText(true);
        ta.prefHeightProperty().bind(this.editor.getScene().heightProperty());
        ta.prefWidthProperty().bind(this.editor.getScene().widthProperty());
        tabBorderContent.setRight(ta);
        
        
        tab.setContent(tabBorderContent);
        this.editor.getTabPane().getTabs().add(tab);
        this.editor.getTabPane().getSelectionModel().select(tab);
        
        /*
        if (tab.isSelected()){
            tab.getContent().requestFocus();
        }
        */
        
        //This saves the initial state to the newly created tab's undoManager.
        ((TabData)tab.getUserData()).getUndoManager().save(this.editor.createMemento());
        
    }
}
