/*
 * A lightweight HTML editor for SWEN-262.
 *
 * 
 * @author aac6012
 * @author thn1069
 * @author jlt8213
 * @author edf7470
 * 
 * @version $Id:$
 */
package htmleditor;

import java.io.File;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;


/**
 *
 * @author trh8614
 */
public class HTMLEditor extends Application {
    
    // Version of application
    final int VERSION = 1;
    final String BACKGROUND_STYLE_CSS = "-fx-background-color: linear-gradient(to bottom, rgb(98, 98, 98), rgb(45, 45, 45));";
    final String STYLE_CSS = HTMLEditor.class.getResource("styles.css").toExternalForm();
    
    
    @Override
    public void start(Stage primaryStage) {
        
        final Group rootGroup = new Group();
        final Scene scene = new Scene(rootGroup, 800, 400, Color.DARKGREY);
        final MenuBar menuBar = buildMenuBarWithMenus(primaryStage.widthProperty());
        
        
        BorderPane canvas = new BorderPane();
        //canvas.getChildren().add(new Label("testicles"));

        TabPane tabPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("Untitled");
        tabPane.getTabs().add(tab);
        
        canvas.setTop(menuBar);
        canvas.setCenter(tabPane);
        canvas.prefHeightProperty().bind(scene.heightProperty());
        canvas.prefWidthProperty().bind(scene.widthProperty());
        scene.getStylesheets().clear();
        scene.getStylesheets().add(STYLE_CSS);
        
        //rootGroup.getChildren().add(menuBar);
        rootGroup.getChildren().add(canvas);
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
      menuBar.setStyle(STYLE_CSS);

      // Prepare left-most 'File' drop-down menu
      final Menu fileMenu = new Menu("File");
      fileMenu.setStyle("-fx-text-fill: white");
      fileMenu.getItems().add(new MenuItem("New"));
      fileMenu.getItems().add(new MenuItem("Open"));
      fileMenu.getItems().add(new MenuItem("Save"));
      fileMenu.getItems().add(new MenuItem("Save As"));
      
      // Seperator
      fileMenu.getItems().add(new SeparatorMenuItem());
      
      // Exit
      MenuItem exitItem = new MenuItem("Exit", null);
      exitItem.setMnemonicParsing(true);
      exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X,KeyCombination.CONTROL_DOWN));
      exitItem.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) { closeApp(); }
      });
      fileMenu.getItems().add(exitItem);
       
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
                        .onAction(new EventHandler<ActionEvent>(){
                               public void handle(ActionEvent e){ aboutApp(); }
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
    
   public void closeApp(){
       // we should also cycle through all open documents and check if they were saved.
       System.exit(0);
   }
   
   public void aboutApp(){
       
        double widthAppWindow = 400;
  
        Label description = new Label("HTML Editor v." + VERSION
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
        aboutUsPane.setMargin(btn, new Insets(8,8,8,8));
        aboutUsPane.setAlignment(btn, Pos.CENTER);
        aboutUsPane.setCenter(description);
        aboutUsPane.setBottom(btn);
        aboutUsPane.setStyle(BACKGROUND_STYLE_CSS);
        
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
