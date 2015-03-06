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
        }
    }
    
}
