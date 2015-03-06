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
        Integer carrotPosition = editor.getCarrotPosition();
        String buffer = editor.getBuffer().substring(0, carrotPosition);
        
        System.out.println("Carrot Position: "+ carrotPosition );
        KeyCode keyType = ((KeyEvent)t).getCode();
        
        // Auto Indent
        
        if( getClosingTagType(buffer) == "Opening" && 
            keyType != KeyCode.BACK_SPACE &&
            ((buffer.length() >= 5 && // tag is type of ul, ol, tr
                (   buffer.substring(carrotPosition - 5, carrotPosition).matches("<ul>\n") ||
                    buffer.substring(carrotPosition - 5, carrotPosition).matches("<ol>\n") ||
                    buffer.substring(carrotPosition - 5, carrotPosition).matches("<tr>\n")
                )
            ) || // tag is type of table
                ( buffer.length() >= 8 && buffer.substring(carrotPosition - 8, carrotPosition).matches("<table>\n"))
            )
          ){ 
            // we want the user to be able to delete the indenting via BACK_SPACE
            System.out.println("Adding newline with new indent with respect to previous line");
            String indent = "";
            Integer previous_indent_size = getDepthOfBuffer( buffer.substring( 0, buffer.length() - 1 ) );
            Integer indent_size = editor.indent_size;
            
            if( previous_indent_size % editor.indent_size != 0){ // the user changed the depth of the indentation
                indent_size = previous_indent_size;
            }
            
            for (int i = 0; i < (indent_size * 2); i++) {
                indent += " ";
            }
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + (indent_size * 2));
        }else if(   buffer.length() >= 4 &&  
                    buffer.substring(carrotPosition - 1, carrotPosition).matches("\n") &&
                    getClosingTagType(buffer) == "Closing" && 
                    keyType != KeyCode.BACK_SPACE){
            System.out.println("Adding newline with respect to previous line");
            String indent = "";
            Integer previous_indent_size = getDepthOfBuffer( buffer.substring( 0, buffer.length() - 1 ) );
            Integer indent_size = editor.indent_size;
            if( previous_indent_size % editor.indent_size != 0){ // the user changed the depth of the indentation
                indent_size = previous_indent_size;
            }
            
            for (int i = 0; i < indent_size; i++) {
                indent += " ";
            }
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + indent_size);
            
        }
        /*
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
        */
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
                return "Opening";
            }
                
            text = text.substring(0, text.length() - 1);
        }
        return "Unknown";
    }
}
