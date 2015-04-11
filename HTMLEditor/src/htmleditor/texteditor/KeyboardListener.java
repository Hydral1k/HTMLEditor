package htmleditor.texteditor;

import htmleditor.HTMLEditor;
import java.util.Scanner;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *  *** DELETE IF NOT USED ***
 * @author jlt8213
 */
public class KeyboardListener implements EventHandler<Event>{

    @Override
    public void handle(Event t) {
        HTMLEditor editor = HTMLEditor.getInstance();
        KeyEvent key = (KeyEvent)t;
        if(key.getCode() == KeyCode.ENTER){
            String indent = "";
            String str = editor.getText().getText();
            int caretPos = editor.getCarrotPosition();
            String prevLine = getPrevLine(str, caretPos);
            int i=0;
            while(i<prevLine.length()){
                char thisChar = prevLine.charAt(i);
                if(thisChar != ' ' && thisChar != '\t')
                    break;
                indent+=thisChar;
                i++;
            }
            String thisText = editor.getText().getText();
            thisText = thisText.substring(0, editor.getText().getCaretPosition()) 
            + indent
            + thisText.substring(editor.getText().getCaretPosition(), thisText.length());
            editor.getText().setText(thisText);
            editor.getText().positionCaret(caretPos + indent.length());
            

        }
    }
    
    public String getPrevLine(String str, int caretPos){
        str = str.substring(0, caretPos);
        Scanner sc = new Scanner(str);
        String returnString = "";
        while(sc.hasNextLine())
            returnString = sc.nextLine();
        return returnString;
    }
}
