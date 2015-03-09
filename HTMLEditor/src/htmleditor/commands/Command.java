/*
 * This the base Command interface that all
 * concrete commands implement.
 */
package htmleditor.commands;

import javafx.event.Event;

/**
 * @author aac6012
 */
public interface Command {
    
    //This is the only method for Command.
    public void execute(Event t) ;
    
}


