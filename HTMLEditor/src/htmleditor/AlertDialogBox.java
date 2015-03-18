package htmleditor;

import htmleditor.commands.MyEventHandler;
import htmleditor.commands.AlertCommand;
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
 * A stage for an alert message.
 * Tells the user a message and provides a button to close the stage.
 * 
 * @author mss9627
 */
public class AlertDialogBox extends Stage {
    
    private static final int WIDTH_DEFAULT = 400;
 
    private static Label label;
    private static AlertDialogBox popup;
 
    private AlertDialogBox() {
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.TRANSPARENT);
 
        label = new Label();
        label.setWrapText(true);
        label.setGraphicTextGap(20);
 
        Button ok = new Button("Okay");
        ok.setOnAction(new MyEventHandler(new AlertCommand(this)));
 
        BorderPane borderPane = new BorderPane();
 
        BorderPane dropShadowPane = new BorderPane();
        dropShadowPane.getStyleClass().add("content");
        dropShadowPane.setTop(label);
 
        HBox hbox = new HBox();
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(ok);
 
        dropShadowPane.setBottom(hbox);
 
        borderPane.setCenter(dropShadowPane);
 
        Scene scene = new Scene(borderPane);                      
        setScene(scene);
    }
 
    public static void show(Stage owner, String msg) {
        if (popup == null) {
            popup = new AlertDialogBox();
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
    }
    
}