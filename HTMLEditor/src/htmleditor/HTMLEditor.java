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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.control.SelectionModel;
import javafx.event.EventHandler;
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
    
    final private KeyCombination CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    final private KeyCombination CTRL_X = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    final private KeyCombination CTRL_W = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);

    
    private Group rootGroup;
    private Scene scene;
    private MenuBar menuBar;
    private BorderPane canvas;
    private TabPane tabPane;
    
    @Override
    public void start(Stage primaryStage) {
        this.rootGroup = new Group();
        this.scene = new Scene(rootGroup, 800, 400, Color.DARKGREY);
        this.menuBar = buildMenuBarWithMenus(primaryStage.widthProperty());
        
		this.canvas = new BorderPane();
		//canvas.getChildren().add(new Label("testicles"));
		this.tabPane = new TabPane();
		SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		this.tabPane.setSelectionModel(selectionModel);
		if (this.tabPane.getTabs().size() == 0){
			this.addNewTab();
			this.tabPane.getSelectionModel().select(0);
		}
        
        this.canvas.setTop(this.menuBar);
        this.canvas.setCenter(this.tabPane);
        this.canvas.prefHeightProperty().bind(this.scene.heightProperty());
        this.canvas.prefWidthProperty().bind(this.scene.widthProperty());
        this.scene.getStylesheets().clear();
        this.scene.getStylesheets().add(STYLE_CSS);
        
        //rootGroup.getChildren().add(menuBar);
        this.rootGroup.getChildren().add(this.canvas);
        primaryStage.setTitle("HTML Editor");
        primaryStage.setScene(this.scene);
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
      //final Menu fileMenu = new Menu("File");
      fileMenu.setStyle("-fx-text-fill: white");

      //New File item
      MenuItem newItem = new MenuItem("New") ;
      newItem.setMnemonicParsing(true);
      newItem.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
      newItem.setOnAction(new MyEventHandler(new NewFileCommand(this)));
      
      //Open File item
      MenuItem openItem = new MenuItem("Open") ;
      openItem.setOnAction(new MyEventHandler(new OpenFileCommand(this)));

      //Save File item
      MenuItem saveItem = new MenuItem("Save") ;
      saveItem.setOnAction(new MyEventHandler(new SaveFileCommand(this)));
      
      //SaveAs File item
      MenuItem saveAsItem = new MenuItem("Save As...") ;
      saveAsItem.setOnAction(new MyEventHandler(new SaveAsCommand(this)));
      
      //Add all items to the left-most dropdown menu
      fileMenu.getItems().add(newItem);
      fileMenu.getItems().add(openItem);
      fileMenu.getItems().add(saveItem);
      fileMenu.getItems().add(saveAsItem);
      
      // Seperator
      fileMenu.getItems().add(new SeparatorMenuItem());
      
      // Exit
      MenuItem exitItem = new MenuItem("Exit", null);
      exitItem.setMnemonicParsing(true);
      exitItem.setAccelerator(new KeyCodeCombination(KeyCode.X,KeyCombination.CONTROL_DOWN));
      exitItem.setOnAction(new MyEventHandler(new ExitCommand(this))) ;
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
    
    
    public void addNewTab(){
        Tab tab = new Tab();
        
        /*
        if (this.tabPane.getTabs().size() == 0){
            tab.setId("0");
            tab.setText("Untitled " + tab.getId());
        }else{
            int i = 0;
            for (Tab t : this.tabPane.getTabs()){
                if (Integer.parseInt(t.getId()) == i){
                    i += 1;
                }
                else if (Integer.parseInt(t.getId()) > i){
                    i -= 1;
                    //System.out.println(this.tabPane.getTabs());
                    tab.setId(Integer.toString(i));
                    tab.setText("Untitled " + Integer.toString(i));
                }
                else{
                    i += 1;
                    tab.setId(Integer.toString(i));
                    tab.setText("Untitled " + Integer.toString(i));
                }
            }
        }
        */
        tab.setText("Untitled");
        TextArea ta = new TextArea();
        tab.setContent(ta);
        ta.prefHeightProperty().bind(this.scene.heightProperty());
        ta.prefWidthProperty().bind(this.scene.widthProperty());
        ta.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            public void handle(KeyEvent event){
                if (CTRL_S.match(event)){
                    saveFile();
                }else if (CTRL_W.match(event)){
                    closeCurrentTab();
                }else if (CTRL_X.match(event)){
                    System.exit(0);
                }
            }
        });
        
        
        tab.setContent(ta);
        this.tabPane.getTabs().add(tab);
        this.tabPane.getSelectionModel().select(tab);
        
        if (tab.isSelected()){
            tab.getContent().requestFocus();
        }
    
    }
    
    
    public void saveFile(){
        
    }
    
    
    public void closeCurrentTab(){
        for (Tab t : this.tabPane.getTabs()){
            if (t.isSelected()){
                this.tabPane.getTabs().remove(t);
            }
        }
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
