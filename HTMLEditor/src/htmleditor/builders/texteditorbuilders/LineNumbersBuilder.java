/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.texteditorbuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
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
        this.lineNumbers.setStyle("-fx-text-fill: black;"+
                             "-fx-background-color: lightgrey;"+
                             "-fx-font: Courier New;"+
                             "-fx-font-family: monospace;"+
                             "-fx-font-size: 12;");
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
                char newChar = '+';
                if(nums.charAt(caretPos) == '+'){
                    newChar = '-';
                }
                newNums.setCharAt(caretPos, newChar);
                /* TODO: 
                   for above line, implement function to collapse element,
                    which should return a boolean.
                    changing character should depend on collapse
                */
                lineNumbers.setText(newNums.toString());
                System.out.println("LINE " + clickedLine);
            }
        });
    }
    
    
    public TextArea getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.lineNumbers;
    }
}
