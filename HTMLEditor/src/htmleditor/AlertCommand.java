package htmleditor;

import javafx.event.Event;

/**
 * Command for an alert popup.
 * Closes the popup box.
 * 
 * @author mss9627
 */
public class AlertCommand implements Command {

    AlertDialogBox alert;
    
    public AlertCommand( AlertDialogBox alertBox ){
        alert = alertBox;
    }
    
    @Override
    public void execute(Event t) {
        alert.close();
    }
    
}
