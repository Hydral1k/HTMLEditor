/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.texteditor;

import javafx.scene.control.TextField;

/**
 *
 * @author jlt8213
 */
public class NumberTextField extends TextField{
    public NumberTextField(){
        this.setPromptText("Rows");
    }

    @Override
    public void replaceText(int i, int i1, String string) {
        if (string.matches("[0-9]") || string.isEmpty()){
            super.replaceText(i, i1, string);
        }
    }

    @Override
    public void replaceSelection(String string) {
        super.replaceSelection(string); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
