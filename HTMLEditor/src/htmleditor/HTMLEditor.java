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


import htmleditor.builders.menubuilders.MenuBuilder;
import htmleditor.commands.*;
import htmleditor.texteditor.CloseListener;
import htmleditor.texteditor.TabData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private LinkViewPane linkView;
    private Scene scene;
    private MenuBar menuBar;
    private BorderPane canvas;
    private FileChooser fileChooser;
    private MenuItem wrapText;
    
    public boolean autoindent = true;
    public Integer indent_size = 4;  
    
    private String clipboard ;
    
    
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
        
        //This is for copy/cut/paste commands.
        this.clipboard = "" ;
        
        
        // General Menu
        this.menuBar = new MenuBuilder().buildMenuBarWithMenus(this, primaryStage.widthProperty(), this.getStyleCss());
        
        // Prepare 'Options' drop-down menu
        final Menu optionsMenu = new Menu("Options");
        // Text Wrapping item
        MenuItem textWrapItem = new MenuItem("Wrap Text (On)");
        textWrapItem.setOnAction(new MyEventHandler(new WrapTextSwitchCommand(this)));
        optionsMenu.getItems().add(textWrapItem);
        this.wrapText = textWrapItem;
        
        // Indent Option Menu
        final Menu indentMenu = new Menu("Auto Indent (On: " + this.indent_size + " Spaces)");
        final MenuItem indentTypeNone = new MenuItem("None");
        indentTypeNone.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent t){
                linkView.setVisible(autoindent);
            }
        });
        indentMenu.getItems().add(indentTypeNone);
        for (int i = 1; i <= 8; i++) {
            final MenuItem indentTypeNumbers = new MenuItem(i + " Spaces"); 
            indentTypeNumbers.setOnAction(new EventHandler<ActionEvent>(){
                @Override
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
  
        this.linkView = new LinkViewPane( this, LinkViewPane.IN_ORDER );
        
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
        
        //This adds the key listener for entering/deleting in blocks
        this.getText().addEventHandler(KeyEvent.KEY_PRESSED, new MyEventHandler(new KeyCommand(this)));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
    
    public TabData getTabData(){
        Tab thisTab = this.tabPane.getSelectionModel().getSelectedItem();
        return (TabData) thisTab.getUserData();
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
                ta.setStyle("-fx-text-fill: black;"+
                            "-fx-background-color: white;"+
                            "-fx-font: Courier New;"+
                            "-fx-font-family: monospace;"+
                            "-fx-font-size: 12;");
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
    
    /* Returns the HTMLAnalyzer of HTMLEditor */
    public HTMLAnalyzer getAnalyzer(){
        return this.analyzer ;
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
    
    /* Returns the Wrap Text Menu Item */
    public MenuItem getWrapTextMenu() {
        return this.wrapText;
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
    
    public LinkViewPane getLinkView(){
        return this.linkView ;
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
        if(tab.getText().r.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, "bufferedReader was never instantiated.", ex);
            }equals("Untitled") && !thisTA.getText().equals("")){
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
            } catch (IOException ex) {
                Logger.getLogger(HTMLEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch( NullPointerException ex){
                //file is new, hasn't been saved yet anyway
            }
          
        }
        String oldText = stringBuffer.toString();
        if(!oldText.equals(newText))
            changedText = true;
        
        return changedText;
    }

    public void showLinkView( boolean show ){
        if( show ){
            this.canvas.setBottom( linkView.getPane() );
        } else {
            this.canvas.setBottom( null );
        }
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
        
        /*
         * This method is used to compare two states.
         * It is used to prevent consecutive duplicates in undo/redo stacks.
         */
        @Override
        public boolean equals(Object o) {
            try{
                Memento m = (Memento)o ;
                return (this.getBuffer().equals(m.getBuffer()) &&
                    this.getCurserPos() == m.getCurserPos()) ;
            }
            catch (ClassCastException e){
                throw new UnsupportedOperationException("Unable to compare Memento to other classes.") ;
            }   
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
    
    //Sets the clipboard for copy/cut/paste
    public void setClipboard(String st) {
        this.clipboard = st ;
    }
    
    //Gets the clipboard for paste
    public String getClipboard() {
        return this.clipboard ;
    }
}
