/*
 * HI GUYS HOLY SHIT ITS WORKING
 *
 * 
 * @author aac6012
 * @author thn1069
 * @author jlt8213
 * @author edf7470
 * This is another commit test.
 */
package htmleditor;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;


/**
 *
 * @author trh8614
 */
public class HTMLEditor extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        
        final Group rootGroup = new Group();
        final Scene scene = new Scene(rootGroup, 800, 400, Color.WHEAT);
        final MenuBar menuBar = buildMenuBarWithMenus(primaryStage.widthProperty());
        rootGroup.getChildren().add(menuBar);
        primaryStage.setTitle("HTML Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private MenuBar buildMenuBarWithMenus(final ReadOnlyDoubleProperty menuWidthProperty){
      final MenuBar menuBar = new MenuBar();

      // Prepare left-most 'File' drop-down menu
      final Menu fileMenu = new Menu("File");
      fileMenu.getItems().add(new MenuItem("New"));
      fileMenu.getItems().add(new MenuItem("Open"));
      fileMenu.getItems().add(new MenuItem("Save"));
      fileMenu.getItems().add(new MenuItem("Save As"));
      fileMenu.getItems().add(new SeparatorMenuItem());
      fileMenu.getItems().add(new MenuItem("Exit"));
      menuBar.getMenus().add(fileMenu);
      
      // Prepare 'Help' drop-down menu
      final Menu helpMenu = new Menu("Help");
      final MenuItem searchMenuItem = new MenuItem("Search");
      searchMenuItem.setDisable(true);
      helpMenu.getItems().add(searchMenuItem);
      final MenuItem onlineManualMenuItem = new MenuItem("Online Manual");
      onlineManualMenuItem.setVisible(false);
      helpMenu.getItems().add(onlineManualMenuItem);
      helpMenu.getItems().add(new SeparatorMenuItem());
      final MenuItem aboutMenuItem =
         MenuItemBuilder.create()
                        .text("About")
                        .onAction(
                            new EventHandler<ActionEvent>()
                            {
                               @Override public void handle(ActionEvent e)
                               {
                               
                               }
                            })
                        .accelerator(
                            new KeyCodeCombination(
                               KeyCode.A, KeyCombination.CONTROL_DOWN))
                        .build();             
      helpMenu.getItems().add(aboutMenuItem);
      menuBar.getMenus().add(helpMenu);

      // bind width of menu bar to width of associated stage
      menuBar.prefWidthProperty().bind(menuWidthProperty);

      return menuBar;
   }
    
}
