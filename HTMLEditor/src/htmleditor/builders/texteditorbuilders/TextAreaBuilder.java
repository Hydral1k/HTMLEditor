package htmleditor.builders.texteditorbuilders;

import htmleditor.HTMLAnalyzer;
import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.commands.MyEventHandler;
import htmleditor.commands.TextAnalysisCommand;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 *
 * @author thn1069
 */
public class TextAreaBuilder implements Builder {
    private TextArea ta;
    final int TOGGLER_SPACING = 1;
    
    public void build(final HTMLEditor editor, final TextArea lineNumbers) {
        this.ta = new TextArea();
        this.ta.setStyle("-fx-text-fill: black;"+
                    "-fx-background-color: white;"+
                    "-fx-font: Courier New;"+
                    "-fx-font-family: monospace;"+
                    "-fx-font-size: 12;");
        this.ta.setOnKeyReleased(new MyEventHandler(new TextAnalysisCommand(editor)));
        this.ta.setWrapText(true);
        this.ta.prefHeightProperty().bind(editor.getScene().heightProperty());
        this.ta.prefWidthProperty().bind(editor.getScene().widthProperty());
        this.ta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                lineNumbers.clear();
                int lineCount = new HTMLAnalyzer().lineCount(editor.getBuffer());
                Integer max_length = String.valueOf(lineCount).length();
                lineNumbers.setPrefWidth(50 + (10 * max_length));
                
                for (int i = 1; i <= lineCount; i++){
                    Integer curr_length = String.valueOf(i).length();
                    String spacing = new String(new char[max_length - curr_length + TOGGLER_SPACING]).replace("\0", " ");
                    if (i == 1){
                        lineNumbers.appendText(i + spacing + "[+]");
                    }
                    else{lineNumbers.appendText("\n" + i + spacing + "[+]");}
                }
            }
        });
    }
    
    
    public TextArea getProduct(HTMLEditor editor, TextArea lineNumbers) {
        this.build(editor, lineNumbers);
        return this.ta;
    }

    
    @Override
    public void build(HTMLEditor editor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
