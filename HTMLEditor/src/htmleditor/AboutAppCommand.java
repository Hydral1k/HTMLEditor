package htmleditor;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/*
 * This is the command to open the About window.
 */

/**
 * @author aac6012
 */
public class AboutAppCommand implements Command{
    final HTMLEditor editor ;
    
    public AboutAppCommand(HTMLEditor editor){
        this.editor = editor ;
    }
    
    public void execute(Event t) {
               
        double widthAppWindow = 400;
  
        Label description = new Label("HTML Editor v." + editor.VERSION
               + "\n\nSWEN-262 (Group 2)"
               + "\n\nBy Thomas Heissenberger, Emily Filmer, Jordan Tice, Michael Schug, Austin Cook, David Thong Nguyen"
               + "\n\nA light weight HTML editor used for editing HTML files.");
        description.autosize();
        description.setWrapText(true);
        description.setMaxWidth(widthAppWindow * .8);
        description.setTextFill(Paint.valueOf("white"));
       
        Button btn = new Button("Close");
        btn.setAlignment(Pos.BOTTOM_CENTER);
        
        BorderPane aboutUsPane = new BorderPane();
        BorderPane.setMargin(btn, new Insets(8,8,8,8));
        BorderPane.setAlignment(btn, Pos.CENTER);
        aboutUsPane.setCenter(description);
        aboutUsPane.setBottom(btn);
        aboutUsPane.setStyle(editor.BACKGROUND_STYLE_CSS);
        
        final Scene aboutUsScene = new Scene(aboutUsPane, widthAppWindow, 250);
        final Stage aboutUsStage = new Stage();
        
        aboutUsStage.setTitle("About Us");
        aboutUsStage.setScene(aboutUsScene);
        aboutUsStage.centerOnScreen();
        aboutUsStage.show();
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutUsStage.hide();
            }
        });
    }
    
}