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
        
        editor.replaceTabWithSpace();
        Integer carrotPosition = editor.getCarrotPosition();
        String buffer = editor.getBuffer().substring(0, carrotPosition);
        
        System.out.println("Carrot Position: "+ carrotPosition + " Buffer: " + buffer);
        KeyCode keyType = ((KeyEvent)t).getCode();
        
        
        // Only non-navigation characters
        if( keyType != KeyCode.ENTER){
            return;
        }
        
        // Auto Indent
        if((buffer.length() >= 5 && // tag is type of ul, ol, tr
                (   buffer.substring(carrotPosition - 5, carrotPosition).toLowerCase().matches("<ul>\n") ||
                    buffer.substring(carrotPosition - 5, carrotPosition).toLowerCase().matches("<ol>\n") ||
                    buffer.substring(carrotPosition - 5, carrotPosition).toLowerCase().matches("<tr>\n")
                )
            ) || // tag is type of table
                ( buffer.length() >= 8 && buffer.substring(carrotPosition - 8, carrotPosition).toLowerCase().matches("<table>\n"))
            
          ){ 
            
            System.out.println("Adding newline with new indent with respect to previous line");
            String indent = "";
            Integer previous_indent_size = getDepthOfBuffer( getPrevLine(buffer, editor.getCarrotPosition()) );
            Integer indent_size = editor.indent_size;
            
            if( previous_indent_size % editor.indent_size != 0){ // the user changed the depth of the indentation
                System.out.println("User set his own indent size from the previous line");
                indent_size = previous_indent_size;
            }
            
            for (int i = 0; i < (indent_size + previous_indent_size); i++) {
                indent += " ";
            }
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + indent_size + previous_indent_size);
        }else if(   buffer.length() >= 4 &&  
                    buffer.substring(carrotPosition - 1, carrotPosition).matches("\n")){
            System.out.println("Adding newline with respect to previous line");
            String indent = "";
            Integer previous_indent_size = getDepthOfBuffer( getPrevLine(buffer, editor.getCarrotPosition()) );
            for (int i = 0; i < previous_indent_size; i++) {
                indent += " ";
            }
            
            editor.insertIntoBufferAtCarrot(indent, carrotPosition);
            editor.setCarrotPosition(carrotPosition + previous_indent_size);
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
        
    private Integer getDepthOfBuffer(String previousLine){
        int depth = 0;
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
