/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import htmleditor.commands.MyEventHandler;
import htmleditor.texteditor.IndentType;
import htmleditor.texteditor.TagType;
import htmleditor.commands.AboutAppCommand;
import htmleditor.commands.CopyCommand;
import htmleditor.commands.CutCommand;
import htmleditor.commands.ExitCommand;
import htmleditor.commands.IndentCommand;
import htmleditor.commands.InsertCommand;
import htmleditor.commands.NewFileCommand;
import htmleditor.commands.ObjectCommand;
import htmleditor.commands.OpenFileCommand;
import htmleditor.commands.PasteCommand;
import htmleditor.commands.RedoCommand;
import htmleditor.commands.SaveAsCommand;
import htmleditor.commands.SaveFileCommand;
import htmleditor.commands.UndoCommand;
import htmleditor.commands.WrapTextSwitchCommand;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author thn1069
 */
public class MenuBuilder {
    public MenuBar buildMenuBarWithMenus(HTMLEditor editor, final ReadOnlyDoubleProperty menuWidthProperty, String STYLE_CSS){
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle(STYLE_CSS);

        // Prepare left-most 'File' drop-down menu
        final Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-text-fill: white");

        //New File item
        MenuItem newItem = new MenuItem("New") ;
        newItem.setMnemonicParsing(true);
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(new MyEventHandler(new NewFileCommand(editor)));

        //Open File item
        MenuItem openItem = new MenuItem("Open") ;
        openItem.setMnemonicParsing(true);
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(new MyEventHandler(new OpenFileCommand(editor)));

        //Save File item
        MenuItem saveItem = new MenuItem("Save") ;
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(new MyEventHandler(new SaveFileCommand(editor)));

        //SaveAs File item
        MenuItem saveAsItem = new MenuItem("Save As...") ;
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveAsItem.setOnAction(new MyEventHandler(new SaveAsCommand(editor)));

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
        exitItem.setOnAction(new MyEventHandler(new ExitCommand(editor))) ;
        fileMenu.getItems().add(exitItem);

        menuBar.getMenus().add(fileMenu);

        // Prepare 'Edit' drop-down menu
        final Menu editMenu = new Menu("Edit") ;
        editMenu.setStyle("-fx-text-fill: white");
        
        //Copy item
        MenuItem copyItem = new MenuItem("Copy") ;
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)) ;
        copyItem.setOnAction(new MyEventHandler(new CopyCommand(editor))) ;
        editMenu.getItems().add(copyItem) ;
        
        //Cut item
        MenuItem cutItem = new MenuItem("Cut") ;
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)) ;
        cutItem.setOnAction(new MyEventHandler(new CutCommand(editor))) ;
        editMenu.getItems().add(cutItem) ;
        
        //Paste item
        MenuItem pasteItem = new MenuItem("Paste") ;
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)) ;
        pasteItem.setOnAction(new MyEventHandler(new PasteCommand(editor))) ;
        editMenu.getItems().add(pasteItem) ;

        //Undo item
        MenuItem undoItem = new MenuItem("Undo") ;
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN)) ;
        undoItem.setOnAction(new MyEventHandler(new UndoCommand(editor))) ;
        editMenu.getItems().add(undoItem) ;

        //Redo item
        MenuItem redoItem = new MenuItem("Redo") ;
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN)) ;
        redoItem.setOnAction(new MyEventHandler(new RedoCommand(editor))) ;
        editMenu.getItems().add(redoItem) ;

        //Objectify ** TEMPORARY **
        MenuItem objectItem = new MenuItem("Objectify") ;
        objectItem.setAccelerator(new KeyCodeCombination(KeyCode.J, KeyCombination.CONTROL_DOWN)) ;
        objectItem.setOnAction(new MyEventHandler(new ObjectCommand(editor))) ;
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
              subHeaderType.setOnAction(new MyEventHandler(new InsertCommand(editor, TagType.HEADER, dtls)));
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
            subListType.setOnAction(new MyEventHandler(new InsertCommand(editor, TagType.LIST, dtls)));
            listItem.getItems().add(subListType);
        }
        insertMenu.getItems().add(listItem);
        
        //Link item
        MenuItem linkItem = new MenuItem("Link") ;
        //tableItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN)) ;
        linkItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.LINK))) ;
        insertMenu.getItems().add(linkItem) ;
        
        //Image item
        MenuItem imageItem = new MenuItem("Image") ;
        imageItem.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN)) ;
        imageItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.IMAGE))) ;
        insertMenu.getItems().add(imageItem) ;
        
        //Add insert menu to menu bar
        menuBar.getMenus().add(insertMenu);

        // Indent Menu
        final Menu indentBufferMenu = new Menu("Indent");
        final MenuItem indentCurrentLine = new MenuItem("Ident Current Line");
        indentCurrentLine.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_CURRENT_LINE)));
        final MenuItem indentSelection = new MenuItem("Indent Selection");
        indentSelection.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_SELECTION)));
        final MenuItem indentAll = new MenuItem("Indent All");
        indentAll.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_ALL)));
        indentBufferMenu.getItems().addAll(indentCurrentLine, indentSelection, indentAll);
        menuBar.getMenus().add(indentBufferMenu);
        
        
        // bind width of menu bar to width of associated stage
        menuBar.prefWidthProperty().bind(menuWidthProperty);
        return menuBar;
    }
}
