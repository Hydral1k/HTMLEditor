/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import java.util.Scanner;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author trh8614
 */
public class TextAnalysisCommand implements Command {
    
    HTMLEditor editor;
    
    public TextAnalysisCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        String buffer = editor.getBuffer();
        Integer carrotPosition = editor.getCarrotPosition();
        
        System.out.println("Carrot Position: "+ carrotPosition );
        KeyCode keyType = ((KeyEvent)t).getCode();
        // Auto Indent
        if( buffer.length() >= 4 && 
            buffer.substring(carrotPosition - 1, carrotPosition).matches("\n") &&
            getClosingTagType(buffer) == "Opening" &&
            keyType != KeyCode.BACK_SPACE ){
            String indent = "";
            Integer depth = getDepthOfBuffer( buffer.substring( 0, buffer.length() - 1 ) );
            
            for (int i = 0; i < (4 + depth); i++) {
                indent += " ";
            }
            
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + 4 + depth);
            System.out.println("Inserting Tab");
        }else if(   buffer.length() >= 4 && 
                    buffer.substring(carrotPosition - 1, carrotPosition).matches("\n") &&
                    getClosingTagType(buffer) == "Closing" && 
                    keyType != KeyCode.BACK_SPACE){
            Integer depth = getDepthOfBuffer( buffer.substring( 0, buffer.length() - 1 ) );
            String indent = "";
            
            for (int i = 0; i < (depth); i++) {
                indent += " ";
            }
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + depth);
            System.out.println("Inserting Tab");
        }
        else{
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
    }

        private String getPrevLine(String str, int caretPos){
        str = str.substring(0, caretPos);
        Scanner sc = new Scanner(str);
        String returnString = "";
        while(sc.hasNextLine())
            returnString = sc.nextLine();
        return returnString;
    }
        
    private Integer getDepthOfBuffer(String text){
        
        Integer depth = 0;
        Integer lengthToNewLine = 0;
        String previousLine = "";
        
        while( text.length() > 0 ){
            String lastChar = text.substring(text.length() - 1, text.length());
            
            text = text.substring(0, text.length() - 1);
            System.out.println("Last character: " + lastChar +", Text:" + text);
            lengthToNewLine ++;
            
            if( lastChar.equals("\n")){
                System.out.println("End of previous line found.");
                break;
            }else{
                previousLine = lastChar + previousLine;
            }
           
        }
        
        while( previousLine.length() > 0){
            String currChar = previousLine.substring(0, 1);
            previousLine = previousLine.substring(1, previousLine.length());
            if( currChar.equals(" ") ){
                depth ++;
            }else{
                return depth;
            }
        }
        return depth;
    };
    
    private String getClosingTagType(String text) {
        Boolean closingBracket = false;
        Boolean endTagFlag = false;
        Boolean openingBracket = false;
            
        System.out.println("Looking for closing tag...");
        while( text.length() > 0 ){
            String lastChar = text.substring(text.length() - 1, text.length());
            System.out.println("Last character: " + lastChar +", Text:" + text);
  
            if( closingBracket == false && ( !lastChar.equals(">") && !lastChar.equals("<") && !lastChar.equals("/") && !lastChar.equals("\n") ) ){
              
                return "Unknown";
            }
            
            if( closingBracket == false && ">".equals(lastChar) ){
                closingBracket = true;
            }
                
            if( endTagFlag == false && closingBracket == true && "/".equals(lastChar) ){
                endTagFlag = true;
            }
                
            if( openingBracket == false && closingBracket == true && endTagFlag == true && "<".equals(lastChar) ){
                return "Closing";
            }else if(openingBracket == false && closingBracket == true && "<".equals(lastChar)){
                // This is a <hi> tag, an opening tag.
                return "Opening";
            }
                
            text = text.substring(0, text.length() - 1);
        }
        return "Unknown";
    }
    


}
