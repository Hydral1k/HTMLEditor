/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author jlt8213
 */
public class KeyboardListener implements EventHandler<Event>{

    @Override
    public void handle(Event t) {
        KeyEvent key = (KeyEvent)t;
        System.out.println(key.getCode() == KeyCode.ENTER);
        if(key.getCode() == KeyCode.ENTER){
            HTMLEditor.getInstance();
            /*if (!this.performAutoIndent)
                return;
            HTextPane htp = EditorWindow.getInstance().getPane()
            .getCurrentTextPane();
            String s = htp.getText().replace("\r", "");
            int caretPos = htp.getCaretPosition();
            int indexPreviousLineStart = s.lastIndexOf('\n', caretPos - 2) + 1;
            String lastLine = s.substring(indexPreviousLineStart, caretPos - 1);
            Matcher m = BEGINNING_SPACE_PAT.matcher(lastLine);
            String indentation = (m.matches() ? m.group(1) : "");
            // figure out if we need to increase indent for an opening tag
            Matcher start_matcher = START_TAG_PAT.matcher(lastLine);
            if (start_matcher.matches()
            && !start_matcher.group(start_matcher.groupCount())
            .equals("/>")) {
            // this is a start tag... add some more indentation
            for (int i = 0; i < this.numSpacesForIndent; i++)
            indentation += " ";
            }
            }
            Document doc = htp.getDocument();
            if (doc instanceof HDocument) {
            HDocument hdoc = (HDocument) doc;
            try {
            hdoc.insertString(caretPos, indentation, null);
            } catch (BadLocationException e) {
            e.printStackTrace();
            }
            }*/
        }
    }
    
}
