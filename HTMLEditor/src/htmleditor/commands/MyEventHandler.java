/*
 * This is the handler for buttons.
 * Necessary for the Command pattern.
 */
package htmleditor.commands;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * @author aac6012
 */
   
public class MyEventHandler implements EventHandler{
    private final Command command ;

    public MyEventHandler(Command c){
        this.command = c ;
    }
    
    @Override
    public void handle(Event t) {
        command.execute(t) ;
    }   
}

