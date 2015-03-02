/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import javafx.event.Event;

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
        System.out.println("Text Changed!");
        String buffer = editor.getBuffer();
        Integer carrotPosition = editor.getCarrotPosition();
        
        System.out.println("Carrot Position: "+ carrotPosition );
        if( buffer.length() >= 4 && 
            buffer.substring(carrotPosition - 1, carrotPosition).matches("\n") &&
            isClosingTag(buffer)){

            editor.insertIntoBufferAtCarrot("    ", carrotPosition);
            editor.setCarrotPosition(carrotPosition + 4);
            System.out.println("Inserting Tab");
        }
    }

    private boolean isClosingTag(String text) {
        Boolean closingBracket = false;
        Boolean endTagFlag = false;
        Boolean openingBracket = false;
            
        System.out.println("Looking for closing tag...");
        while( text.length() > 0 ){
            String lastChar = text.substring(text.length() - 1, text.length());
            System.out.println("Last character: " + lastChar +", Text:" + text);
  
            if( closingBracket == false && ( !lastChar.equals(">") && !lastChar.equals("<") && !lastChar.equals("/") && !lastChar.equals("\n") ) ){
              
                return false;
            }
            
            if( closingBracket == false && ">".equals(lastChar) ){
                closingBracket = true;
            }
                
            if( endTagFlag == false && closingBracket == true && "/".equals(lastChar) ){
                endTagFlag = true;
            }
                
            if( openingBracket == false && closingBracket == true && endTagFlag == true && "<".equals(lastChar) ){
                return false;
            }else if(openingBracket == false && closingBracket == true && "<".equals(lastChar)){
                // This is a <hi> tag, an opening tag.
                return true;
            }
                
            text = text.substring(0, text.length() - 1);
        }
        return false;
    }

}
