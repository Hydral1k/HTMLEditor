package htmleditor.builders.texteditorbuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.outline.Folder;
import java.util.Scanner;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author thn1069
 */
public class LineNumbersBuilder implements Builder {
    private TextArea lineNumbers;
    
    @Override
    public void build(HTMLEditor editor) {
        this.lineNumbers = new TextArea("1");
        this.lineNumbers.setId("line-numbers");
        this.lineNumbers.setStyle("{-fx-text-fill: black;"+
                             "-fx-background-color: lightgrey;"+
                             "-fx-font: Courier New;"+
                             "-fx-font-family: monospace;"+
                             "-fx-font-size: 12;}\n");
        this.lineNumbers.setEditable(false);
        this.lineNumbers.setWrapText(true);
        this.lineNumbers.setPrefWidth(50);
        this.lineNumbers.autosize();
        this.lineNumbers.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent t) {
                int caretPos = lineNumbers.getCaretPosition();
                String nums = lineNumbers.getText();
                if(nums.isEmpty())
                    return;
                String str = nums.substring(0, caretPos);
                caretPos = str.lastIndexOf("\n");
                if(caretPos == -1)
                    caretPos++;
                Scanner sc = new Scanner(nums.substring(caretPos));
                int clickedLine = sc.nextInt();
                while(nums.charAt(caretPos) != '['){
                    caretPos++;
                }
                caretPos++;
                StringBuilder newNums = new StringBuilder(nums);
                Folder thisFolder = HTMLEditor.getInstance().getTabData().getFolder();
                boolean isFolded = thisFolder.foldLine(clickedLine);
                if(isFolded){
                    char newChar = '+';
                    if(nums.charAt(caretPos) == '+'){
                        newChar = '-';
                    }
                    newNums.setCharAt(caretPos, newChar);
                    lineNumbers.setText(newNums.toString());
                }
            }
        });
    }
    
    
    public TextArea getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.lineNumbers;
    }
}
