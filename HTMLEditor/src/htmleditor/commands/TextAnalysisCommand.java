
package htmleditor.commands;

import htmleditor.HTMLAnalyzer;
import htmleditor.HTMLEditor;
import java.util.Scanner;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * TextAnalysisCommand class, used to modify the HTMLEditor
 * as well as watch it's buffer during changes. Event handeled by a hook
 * that is on the TextArea for KeyPress
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
                    getClosingTagType(buffer) == "Closing" &&
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
    
    /**
     * Gets the last line in the string
     * @param str
     * @param caretPos
     * @return String of lastline
     */
    private String getPrevLine(String str, int caretPos){
        str = str.substring(0, caretPos);
        Scanner sc = new Scanner(str);
        String returnString = "";
        while(sc.hasNextLine())
            returnString = sc.nextLine();
        return returnString;
    }
    
    /**
     * Gets the indentation depth of a line
     * @param previousLine
     * @return Integer # of spaces (indents)
     */
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
    
    /**
     * Checks a line for the trailing tag type.
     * If the tag type is a closing tag, it returns "Closing"
     * If the tag type is opening tag, it returns "Opening"
     * If unknown, "Unkown"
     * 
     * ATTN: This function should eventually be refactored and properly rewritten
     * 
     * @param text
     * @return String TagType
     */
    private String getClosingTagType(String text) {
        Boolean closingBracket = false;
        Boolean endTagFlag = false;
        Boolean openingBracket = false;
            
        System.out.println("Looking for closing tag...");
        while( text.length() > 0 ){
            String lastChar = text.substring(text.length() - 1, text.length());
            System.out.println("Last character: " + lastChar +", Text:" + text);
  
            if( closingBracket == false && 
                    ( !lastChar.equals(">") && 
                    !lastChar.equals("<") && 
                    !lastChar.equals("/") && 
                    !lastChar.equals("\n") 
                    ) 
                ){
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
