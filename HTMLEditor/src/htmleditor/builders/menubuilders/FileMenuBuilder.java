/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.commands.ExitCommand;
import htmleditor.commands.MyEventHandler;
import htmleditor.commands.NewFileCommand;
import htmleditor.commands.OpenFileCommand;
import htmleditor.commands.SaveAsCommand;
import htmleditor.commands.SaveFileCommand;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author thn1069
 */
public class FileMenuBuilder implements Builder {
    
    private Menu menu;
    
    @Override
    public void build(HTMLEditor editor) {
        // Prepare left-most 'File' drop-down menu
        this.menu = new Menu("File");
        this.menu.setStyle("-fx-text-fill: white");

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
        this.menu.getItems().add(newItem);
        this.menu.getItems().add(openItem);
        this.menu.getItems().add(saveItem);
        this.menu.getItems().add(saveAsItem);

        // Seperator
        this.menu.getItems().add(new SeparatorMenuItem());

        // Exit
        MenuItem exitItem = new MenuItem("Exit", null);
        exitItem.setMnemonicParsing(true);
        exitItem.setOnAction(new MyEventHandler(new ExitCommand(editor))) ;
        this.menu.getItems().add(exitItem);
    }

    
    @Override
    public Menu getProduct() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
