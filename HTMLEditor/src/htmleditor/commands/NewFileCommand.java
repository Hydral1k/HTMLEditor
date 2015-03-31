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
                             "-fx-background-color: white;"+
                             "-fx-font: Courier New;"+
                             "-fx-font-family: monospace;"+
                             "-fx-font-size: 12;");
        lineNumbers.setDisable(true);
        lineNumbers.setWrapText(true);
        lineNumbers.setPrefWidth(20);
        lineNumbers.autosize();
        tabBorderContent.setLeft(lineNumbers);
        
        /*
        final GridPane lineNumbers = new GridPane();
        ColumnConstraints column = new ColumnConstraints(20,20,Double.MAX_VALUE);
        column.setHgrow(Priority.ALWAYS);
        
        Label lineno = new Label(" 1 ");
        lineno.setStyle("-fx-padding: 5 4 0 4;"+
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 1;"+
                    "-fx-background-color: black");
        lineNumbers.getChildren().addAll(lineno);
        tabBorderContent.setLeft(lineNumbers);
        */
        
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
                System.out.println("line: " + new HTMLAnalyzer().lineCount(editor.getBuffer()));
                lineNumbers.clear();
                int lineCount = new HTMLAnalyzer().lineCount(editor.getBuffer());
                lineNumbers.setPrefWidth(20 * (int)(Math.log10(lineCount)+1));
                for (int i = 1; i <= lineCount; i++){
                    System.out.println(lineNumbers.getClass());
                    lineNumbers.appendText(""+i+"\n");
                    
                    /*
                    if (i == 1){
                        newLineNo.setStyle("-fx-padding: 5 4 0 4;"+ 
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 11;");
                    }
                    else{
                    newLineNo.setStyle("-fx-padding: 0 4 0 4;"+
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 11;");
                    }
                    lineNumbers.addRow(i-1, newLineNo);
                    */
                }
            }
        });
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
