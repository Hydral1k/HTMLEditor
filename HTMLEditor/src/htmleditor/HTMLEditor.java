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


import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    final private KeyCombination CTRL_O = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);

    private HTMLAnalyzer analyzer;
    private Stage stage;
    private Group rootGroup;
    private TabPane tabPane;
    private Scene scene;
    private MenuBar menuBar;
    private BorderPane canvas;
    private FileChooser fileChooser;
    private MenuItem wrapText;
    
    public boolean autoindent = true;
    public Integer indent_size = 4;  
    
    
    private static HTMLEditor instance = null;
    public static HTMLEditor getInstance() {
       if(instance == null) {
          System.err.println("ERROR! - UNINITIALIZED EDITOR");
       }
       return instance;
    }
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.analyzer = new HTMLAnalyzer();
        this.stage = primaryStage;
        this.rootGroup = new Group();
        this.scene = new Scene(rootGroup, 800, 400, Color.DARKGREY);
        this.menuBar = buildMenuBarWithMenus(primaryStage.widthProperty());
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Select an HTML file...");
        
        
        this.canvas = new BorderPane();
        this.tabPane = new TabPane();
        if (this.tabPane.getTabs().size() == 0){
                this.addNewTab();
                this.tabPane.getSelectionModel().select(0);
                this.tabPane.getSelectionModel().getSelectedItem().getContent().requestFocus();
        }
        tabPane.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<Tab>() {
                @Override
                public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                    TextArea ta = HTMLEditor.getInstance().getText();
                    MenuItem wrapMenu = HTMLEditor.getInstance().wrapText;
                    if(ta.isWrapText())
                        wrapMenu.setText("Wrap Text (On)");
                    else{
                        wrapMenu.setText("Wrap Text (Off)");
                    }
                }
            }
        );
        
        this.canvas.setTop(this.menuBar);
        this.canvas.setCenter(this.tabPane);
        this.canvas.prefHeightProperty().bind(this.scene.heightProperty());
        this.canvas.prefWidthProperty().bind(this.scene.widthProperty());
        this.scene.getStylesheets().clear();
        this.scene.getStylesheets().add(STYLE_CSS);
        
        //rootGroup.getChildren().add(menuBar);
        this.rootGroup.getChildren().add(this.canvas);
        this.stage.setTitle("HTML Editor");
        this.stage.setScene(this.scene);
        this.stage.setOnCloseRequest(new MyEventHandler(new ExitCommand(this)));
        this.stage.show();
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

      //New File item
      MenuItem newItem = new MenuItem("New") ;
      newItem.setMnemonicParsing(true);
      newItem.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
      newItem.setOnAction(new MyEventHandler(new NewFileCommand(this)));
      
      //Open File item
      MenuItem openItem = new MenuItem("Open") ;
      openItem.setMnemonicParsing(true);
      openItem.setAccelerator(new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN));
      openItem.setOnAction(new MyEventHandler(new OpenFileCommand(this)));

      //Save File item
      MenuItem saveItem = new MenuItem("Save") ;
      saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN));
      saveItem.setOnAction(new MyEventHandler(new SaveFileCommand(this)));
      
      //SaveAs File item
      MenuItem saveAsItem = new MenuItem("Save As...") ;
      saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
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
      exitItem.setOnAction(new MyEventHandler(new ExitCommand(this))) ;
      fileMenu.getItems().add(exitItem);
       
      menuBar.getMenus().add(fileMenu);

      // Prepare 'Insert' drop-down menu
      final Menu insertMenu = new Menu("Insert");
      fileMenu.setStyle("-fx-text-fill: white");

      //Bold item
      MenuItem boldItem = new MenuItem("Bold") ;
      boldItem.setAccelerator(new KeyCodeCombination(KeyCode.B,KeyCombination.CONTROL_DOWN));
      boldItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.BOLD)));
      insertMenu.getItems().add(boldItem);

      //Italics item
      MenuItem italicsItem = new MenuItem("Italics") ;
      italicsItem.setAccelerator(new KeyCodeCombination(KeyCode.I,KeyCombination.CONTROL_DOWN));
      italicsItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.ITALICS)));
      insertMenu.getItems().add(italicsItem);
      
      //Header item
      Menu headerItem = new Menu("Header");
      // Cycle through and add other header types
      for (int i = 1; i < 7; i++) {
            MenuItem subHeaderType = new MenuItem("Header "+ i);
            Map<String, String> dtls  = new HashMap<>();
            dtls.put("Header Type", i + "");
            subHeaderType.setOnAction(new MyEventHandler(new InsertCommand(this, TagType.HEADER, dtls)));
            headerItem.getItems().add(subHeaderType); 
      }
      insertMenu.getItems().add(headerItem);
      
      //Table item
      MenuItem tableItem = new MenuItem("Table") ;
      tableItem.setAccelerator(new KeyCodeCombination(KeyCode.T,KeyCombination.CONTROL_DOWN));
      tableItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.TABLE)));
      insertMenu.getItems().add(tableItem);
      
      //List item
      Menu listItem = new Menu("List");
      String[] listTypes = {"Number", "Bullet", "Dictionary"};
      for (int i = 0; i < listTypes.length; i++) {
          MenuItem subListType = new MenuItem(listTypes[i]);
          Map<String, String> dtls = new HashMap<>();
          dtls.put("List Type", listTypes[i]);
          subListType.setOnAction(new MyEventHandler(new InsertCommand(this, TagType.LIST, dtls)));
          listItem.getItems().add(subListType);
      }
      insertMenu.getItems().add(listItem);
      
      menuBar.getMenus().add(insertMenu);
      
      // Prepare 'Options' drop-down menu
      final Menu optionsMenu = new Menu("Options");
      
      // Text Wrapping item
      MenuItem textWrapItem = new MenuItem("Wrap Text (On)");
      textWrapItem.setOnAction(new MyEventHandler(new WrapTextSwitchCommand(this)));
      optionsMenu.getItems().add(textWrapItem);
      this.wrapText = textWrapItem;
      
      
      // Indent Option Menu
      final Menu indentMenu = new Menu("Auto Indent (On: " + HTMLEditor.this.indent_size + " Spaces)");
      final MenuItem indentTypeNone = new MenuItem("None");
      indentTypeNone.setOnAction(new EventHandler<ActionEvent>(){
          public void handle(ActionEvent t){
              indentMenu.setText("Auto Indent (Off)");
              HTMLEditor.this.indent_size = 0;
          }
      });
      indentMenu.getItems().add(indentTypeNone);
      
      for (int i = 1; i < 8; i++) {
           final MenuItem indentTypeNumbers = new MenuItem(i + " Spaces"); 
           indentTypeNumbers.setOnAction(new EventHandler<ActionEvent>(){
               public void handle(ActionEvent t){
                   int width = Integer.parseInt(indentTypeNumbers.getText().substring(0,1)); // because js is cooler
                   indentMenu.setText("Auto Indent (On: " + width + " Spaces)");
                   HTMLEditor.this.indent_size = width;
               }
           });
           indentMenu.getItems().add(indentTypeNumbers);
      }
      optionsMenu.getItems().add(indentMenu);
      
      menuBar.getMenus().add(optionsMenu);
      
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
                        .onAction(new MyEventHandler(new AboutAppCommand(this)))
                        .build();             
      helpMenu.getItems().add(aboutMenuItem);
      menuBar.getMenus().add(helpMenu);

      // bind width of menu bar to width of associated stage
      menuBar.prefWidthProperty().bind(menuWidthProperty);
      return menuBar;
    }
    
    
    
    
    public void addNewTab(){
        Tab tab = new Tab();
        tab.setOnClosed(new closeListener());
        
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
        tab.setId("Untitled");
        tab.setUserData(new TabData());
        TextArea ta = new TextArea();
        
        ta.setOnKeyReleased(new MyEventHandler(new TextAnalysisCommand(this)));
        ta.setWrapText(true);
        ta.prefHeightProperty().bind(this.scene.heightProperty());
        ta.prefWidthProperty().bind(this.scene.widthProperty());
        /*ta.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
            public void handle(KeyEvent event){
                if (CTRL_S.match(event)){
                    saveFile();
                }else if (CTRL_W.match(event)){
                    closeCurrentTab();
                }else if (CTRL_O.match(event)){
                    openFile();
                }else if (CTRL_X.match(event)){
                    System.exit(0);
                }
            } 
        }); */
        
        tab.setContent(ta);
        
        /*
        tab.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
                currentTab.getContent().requestFocus();
                TextArea currentText = (TextArea)currentTab.getContent();
                currentText.requestFocus();
                System.out.println(currentTab.getContent());            
            }
        });
        */
        this.tabPane.getTabs().add(this.tabPane.getTabs().size(), tab);
        this.tabPane.getSelectionModel().select(tab);
        
        if (tab.isSelected()){
            tab.getContent().requestFocus();
        }
    
    }
    
    /* Returns text string currently in active buffer */
    public String getBuffer(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        TextArea thisTA = (TextArea)thisTab.getContent();
        return thisTA.getText();
    }
    
    public Integer getCarrotPosition(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        TextArea thisTA = (TextArea)thisTab.getContent();
        return thisTA.getCaretPosition();
    }
    
    public void setCarrotPosition(Integer caretPosition){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        TextArea thisTA = (TextArea)thisTab.getContent();
        thisTA.positionCaret(caretPosition);
    }
    
    public void insertIntoBufferAtCarrot(String text, Integer carrotPosition){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        TextArea thisTA = (TextArea)thisTab.getContent();
        thisTA.insertText(carrotPosition, text);
    }
    
    public void setBuffer(String text){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        TextArea thisTA = (TextArea)thisTab.getContent();
        thisTA.setText(text);
    }
    
    /* Returns file name currently in active buffer (complete path) */
    public String getFileName(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        return thisTab.getId();
    }
    
    public TextArea getText(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        return (TextArea)thisTab.getContent();
    }
    
    public void closeCurrentTab(){
        this.tabPane.getTabs().remove(this.tabPane.getSelectionModel().getSelectedItem());
    }
    
    
    public void openFile(){
        File file = this.fileChooser.showOpenDialog(this.stage);
        if (file != null){
            TextArea ta;
            Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
            //This try catch prevents a null pointer when no tabs are open.
            String buffer ;
            try{
                buffer = getBuffer() ;
            } catch (NullPointerException e){
                buffer = null ;
            }
            
            if(buffer != null && buffer.trim().equals("")){
                ta = (TextArea) this.tabPane.getSelectionModel().getSelectedItem().getContent();
                ta.setText(this.readFile(file));
                thisTab.setText(file.getName());
                thisTab.setOnClosed(new closeListener());
                thisTab.setId(file.getAbsolutePath());
            }
            else{
                Tab newTab = new Tab();
                ta = new TextArea();
                ta.setText(this.readFile(file));
                newTab.setContent(ta);
                newTab.setText(file.getName());
                this.tabPane.getTabs().add(newTab);
                this.tabPane.getSelectionModel().select(newTab);
                newTab.setOnClosed(new closeListener());
                newTab.setId(file.getAbsolutePath());
                
            }
        }
        
        
    }
    
    
    private String readFile(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
         
        try {
 
            bufferedReader = new BufferedReader(new FileReader(file));
             
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
                stringBuffer.append('\n');
            }
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stringBuffer.toString();
    }
    
    public TabPane getTabPane(){
        return this.tabPane ;
    }
    
    public Stage getStage(){
        return this.stage ;
    }
    
    public boolean hasChanged(Tab tab){
        boolean changedText=false;
        TextArea thisTA = (TextArea)tab.getContent();
        if(tab.getText().equals("Untitled") && !thisTA.getText().equals("")){
            changedText = true;
        }
        else if(!tab.getText().equals("Untitled")){
            String newText = thisTA.getText();
            File file = new File(tab.getText());
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    stringBuffer.append(text);
                    stringBuffer.append('\n');
                }
            } catch (FileNotFoundException ex) {
                changedText = true;
            } catch (IOException ex) {
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String oldText = stringBuffer.toString();
            if(!oldText.equals(newText))
                changedText = true;
        }
        return changedText;
    }

    class closeListener implements EventHandler<Event>{
        @Override
        public void handle(Event t) {
            analyzer = new HTMLAnalyzer();
            boolean changedText;
            Tab closedTab = (Tab) t.getTarget();
            changedText = hasChanged(closedTab);
            if(!changedText) //if nothing changed, let them leave
                return;
            int result = YesNoDialogBox.show(HTMLEditor.this.getStage(), "Warning!\nThe file you are closing contained unsaved changes.\nAre you sure you wish to close?");
            if( result == 1 )
                return;
            else{ //restore tab
                HTMLEditor.this.tabPane.getTabs().add(closedTab);
                HTMLEditor.this.tabPane.getSelectionModel().select(closedTab);
            }
        }
    }
    
    
}

