package htmleditor;

import htmleditor.commands.YesNoCommand;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A Yes/No confirmation box.
 * Used to confirm a save on a non-well-formed buffer.
 * 
 * @author mss9627
 */
public class YesNoDialogBox extends Stage {
    
    private static final int WIDTH_DEFAULT = 400;
 
    private static Label label;
    private static YesNoDialogBox popup;
    private static int result;
 
    public static final int NO = 0;
    public static final int YES = 1;
 
    private YesNoDialogBox() {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);
 
        label = new Label();
        label.setWrapText(true);
        label.setGraphicTextGap(20);
 
        // Create Yes/no buttons and add their functionality.
        Button ybutton = new Button("Yes");
        ybutton.setOnAction(new MyEventHandler(new YesNoCommand(this, YES)));
        Button nbutton = new Button("No");
        nbutton.setOnAction(new MyEventHandler(new YesNoCommand(this, NO)));
 
        BorderPane borderPane = new BorderPane();
 
        BorderPane dropShadowPane = new BorderPane();
        dropShadowPane.getStyleClass().add("content");
        dropShadowPane.setTop(label);
 
        // Place buttons into stage.
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(ybutton);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(nbutton);
 
        dropShadowPane.setBottom(hbox);
 
        borderPane.setCenter(dropShadowPane);
 
        Scene scene = new Scene(borderPane);                      
        setScene(scene);
    }
 
    public static int show(Stage owner, String msg) {
        if (popup == null) {
            popup = new YesNoDialogBox();
        }
 
        label.setText(msg);
        final Text text = new Text(msg);
        text.snapshot(null, null);
 
        popup.setWidth(300);
        popup.setHeight(120);
 
        // Center box on top of the window calling it.
        popup.setX(owner.getX() + (owner.getWidth() / 2 - popup.getWidth() / 2));
        popup.setY(owner.getY() + (owner.getHeight() / 2 - popup.getHeight() / 2));
 
        popup.showAndWait();
 
        return result;
    }
    
    public void setResult( int newResult ){
        result = newResult;
    }
    
}