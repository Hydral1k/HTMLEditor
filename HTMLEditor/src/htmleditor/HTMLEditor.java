/*
 * A lightweight HTML editor for SWEN-262.
 *
 * @author trh8614
 * @author aac6012
 * @author thn1069
 * @author jlt8213
 * @author edf7470
 * 
 * @version RELEASE 1
 */
package htmleditor;


import htmleditor.commands.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

// Main class
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
                new NewFileCommand(this).execute(null);
                this.tabPane.getSelectionModel().select(0);
                this.getText().requestFocus();
        }
        this.tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        
        
        /*
        tabPane.getSelectionModel().selectedItemProperty().
                addListener(
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
        */
        
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
        
        
        //This is the command line parameters
        final Parameters param = getParameters() ;
        List<String> paramList = param.getRaw() ;
        if (!paramList.isEmpty()) {
            File f = new File(paramList.get(0)) ;
            openFile(f) ;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private MenuBar buildMenuBarWithMenus(final ReadOnlyDoubleProperty menuWidthProperty){
      menuBar = new MenuBar();
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
      
      // Prepare 'Edit' drop-down menu
      final Menu editMenu = new Menu("Edit") ;
      editMenu.setStyle("-fx-text-fill: white");
      
      //Undo item
      MenuItem undoItem = new MenuItem("Undo") ;
      undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN)) ;
      undoItem.setOnAction(new MyEventHandler(new UndoCommand(this))) ;
      editMenu.getItems().add(undoItem) ;
      
      //Redo item
      MenuItem redoItem = new MenuItem("Redo") ;
      redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN)) ;
      redoItem.setOnAction(new MyEventHandler(new RedoCommand(this))) ;
      editMenu.getItems().add(redoItem) ;
      
      //Objectify ** TEMPORARY **
      MenuItem objectItem = new MenuItem("Objectify") ;
      objectItem.setAccelerator(new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN)) ;
      objectItem.setOnAction(new MyEventHandler(new ObjectCommand(this))) ;
      editMenu.getItems().add(objectItem) ;
      
      
      //Add Edit menu to menu bar
      menuBar.getMenus().add(editMenu) ;

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
      
      // Indent Menu
      final Menu indentBufferMenu = new Menu("Indent");
      final MenuItem indentCurrentLine = new MenuItem("Ident Current Line");
      indentCurrentLine.setOnAction(new MyEventHandler(new IndentCommand(this, IndentType.INDENT_CURRENT_LINE)));
      final MenuItem indentSelection = new MenuItem("Indent Selection");
      indentSelection.setOnAction(new MyEventHandler(new IndentCommand(this, IndentType.INDENT_SELECTION)));
      final MenuItem indentAll = new MenuItem("Indent All");
      indentAll.setOnAction(new MyEventHandler(new IndentCommand(this, IndentType.INDENT_ALL)));
      indentBufferMenu.getItems().addAll(indentCurrentLine, indentSelection, indentAll);
      menuBar.getMenus().add(indentBufferMenu);
      
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
      
      for (int i = 1; i <= 8; i++) {
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
   
    
    /**
     * Gets the previous line with respect to the carets position
     * @param caretPosition
     * @return String (Previous line)
     */
    public String getPrevLine(Integer caretPosition){
        TextArea thisTA = getText();
        String[] lines = thisTA.getText().substring(0, caretPosition).split("\n");
        
        if(lines.length >= 2){
            return lines[lines.length - 2];
        }
        return "";
    }
    
    /**
     * Gets the curr line with respect to the carets position.
     * @param caretPosition
     * @return String (Current line)
     */
    public String getCurrLine(Integer caretPosition){
        TextArea thisTA = getText();
        
        String str = thisTA.getText().substring(0, caretPosition);
        Scanner sc = new Scanner(str);
        String returnString = "";
        while(sc.hasNextLine())
            returnString = sc.nextLine();
        return returnString;
    }
    
    /**
     * Finds the index number of the \n character in the buffer
     * @return IndexPosition
     */
    public Integer getCurrentLineStartPosition(){
        TextArea thisTA = getText();
        Integer old_length = thisTA.getCaretPosition();
        String str = thisTA.getText().substring(0, thisTA.getCaretPosition());
        
        return str.lastIndexOf("\n");
        
    }
    

    /* Returns text string currently in active buffer */
    public String getBuffer(){
        return getText().getText();
    }
    
    /* Returns the text in the textArea that is currently selected */
    public String getBufferSelection(){
        return getText().getSelectedText();
    }
    
    /* Returns the position of the caret */
    public Integer getCarrotPosition(){
        return getText().getCaretPosition();
    }
    
    /* Sets the position of the caret to the provided integer index */
    public void setCarrotPosition(Integer caretPosition){
        getText().positionCaret(caretPosition);
    }
    
    // Redundant, should be removed in next release or refactored
    public void insertIntoBufferAtCarrot(String text, Integer carrotPosition){
        insertIntoBufferAtPos( text, carrotPosition);
    }
    
    public void insertIntoBufferAtPos(String text, Integer position){
        getText().insertText(position, text);
    }
    
    /**
     * An overwatch method that replaces all instances of tabs with the specified
     * indentation as per the requirements. If a tab is removed, the function
     * attempts to reposition the caret properly.
     */
    public void replaceTabWithSpace(){
        TextArea thisTA = getText();
        
        if(thisTA.getText().contains("\t")){
            int temp = getCarrotPosition();
            thisTA.setText(thisTA.getText().replace("\t", 
                new String(new char[HTMLEditor.this.indent_size]).replace("\0", " ")
                )
            );
            setCarrotPosition(temp + HTMLEditor.this.indent_size);
        }
    }
    
    /**
     * Replaces the text inside the textArea with the provided String.
     * @param text - The String that is replacing the current text.
     */
    public void setBuffer(String text){
        getText().setText(text);
    }
    
    /* Returns file name currently in active buffer (complete path) */
    public String getFileName(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        return thisTab.getId();
    }
    
    /* Returns the TextArea object of the current tab. */
    public TextArea getText(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        BorderPane content = (BorderPane)thisTab.getContent();
        TextArea thisTA = (TextArea)content.getRight();
        return thisTA;
    }
    
    /* Closes the current tab */
    public void closeCurrentTab(){
        this.tabPane.getTabs().remove(this.tabPane.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Prompts the user to select a file via the default file explorer.
     * @return The file that the user selects in the explorer window.
     */
    public File requestFile(){
        return this.fileChooser.showOpenDialog(this.stage) ;
    }
    
    /**
     * Opens the provided file inside a new tab.
     * @param f - The file to be opened in a new tab
     */
    public void openFile(File f){
        if (f != null){
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
                ta = getText();
                ta.setText(this.readFile(f));
                thisTab.setText(f.getName());
                thisTab.setOnClosed(new CloseListener(this));
                thisTab.setId(f.getAbsolutePath());
            }
            else{
                Tab newTab = new Tab();
                ta = new TextArea();
                ta.setText(this.readFile(f));
                newTab.setContent(ta);
                newTab.setText(f.getName());
                this.tabPane.getTabs().add(newTab);
                this.tabPane.getSelectionModel().select(newTab);
                newTab.setOnClosed(new CloseListener(this));
                newTab.setId(f.getAbsolutePath());
                
            }
        }
        
        
    }
    
    /**
     * Reads in and returns the text from the provided file.
     * @param file - The file that will be read in.
     * @return A String with the text of the entire file.
     */
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
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
 
        } catch (FileNotFoundException ex) {
            return "" ;
            //Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex){
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, "bufferedReader was never instantiated.", ex);
            }
        }
        return stringBuffer.toString();
    }
    
    /* Returns the tabPane of HTMLEditor */
    public TabPane getTabPane(){
        return this.tabPane ;
    }
    
    /* Returns the stage of HTMLEditor */
    public Stage getStage(){
        return this.stage ;
    }
    
    /* Returns the scene of HTMLEditor */
    public Scene getScene(){
        return this.scene ;
    }
    
    /* Returns the current tab */
    public Tab getCurrentTab() {
        return this.getTabPane().getSelectionModel().getSelectedItem() ;
    }
    
    /* Returns the background style CSS */
    public String getBackgroundStyleCss(){
        return this.BACKGROUND_STYLE_CSS;
    }
    
    /* Returns the background style CSS */
    public String getStyleCss(){
        return this.STYLE_CSS;
    }
    /* Returns the background style CSS */
    public int getVersion(){
        return this.VERSION;
    }
    
    
    /**
     * Checks the provided tab if any changes have been made to the tab since the last save.
     * @param tab - The tab to check for changes.
     * @return True if the text in the provided tab has changed.
     * ***Consider moving to HTMLAnalyzer or similar for blob reduction
     */
    public boolean hasChanged(Tab tab){
        boolean changedText=false;
        BorderPane thisPane = (BorderPane)tab.getContent();
        TextArea thisTA = (TextArea)thisPane.getRight();
        
        // user can still name the file "Untitled"
        /*
        if(tab.getText().equals("Untitled") && !thisTA.getText().equals("")){
            changedText = true;
        }*/
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
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        } catch (FileNotFoundException ex) {
            changedText = true;
        } catch (IOException ex) {
            Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String oldText = stringBuffer.toString();
        if(!oldText.equals(newText))
            changedText = true;
        
        return changedText;
    }
    
    /** 
     * Custom listener to handle the closing of a tab. 
     * The purpose of this is make sure the file is saved before closing in the buffer,
     * otherwise all the work since the last save will be lost.
     */

    /* This is the memento for the memento pattern.
     * Note that this may need to be changed since it must save
     * the current tabs state.
     */
    private class Memento {
        private String htmlBuffer ;
        private int cursorPos ;
        
        public Memento(String buffer, int pos) {
            this.htmlBuffer = buffer ;
            this.cursorPos = pos ;
        }
        
        public String getBuffer(){
            return this.htmlBuffer ;
    }
        
        public int getCurserPos(){
            return this.cursorPos ;
}
        
    }
    
    public void setState(Object o){
        Memento m = (Memento)o ; //Converting object from UndoManager.
        this.getText().setText(m.getBuffer()) ;
        this.setCarrotPosition(m.getCurserPos()) ;
    }
    
    public Object createMemento(){
        return new Memento(this.getText().getText(), this.getCarrotPosition()) ;
    }
}
