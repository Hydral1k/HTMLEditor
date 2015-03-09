package htmleditor;

import javafx.event.Event;

/**
 * Command for a confirmation box.
 * Closes the calling box and returns whether "Yes" or "No" was selected.
 * 
 * @author mss9627
 */
public class YesNoCommand implements Command {

    YesNoDialogBox confirm;
    private final int type;
    
    public YesNoCommand( YesNoDialogBox confirmBox, int commandType ){
        confirm = confirmBox;
        type = commandType;
    }
    
    public void execute(Event t) {
        confirm.setResult(type);
        confirm.close();
    }
    
}