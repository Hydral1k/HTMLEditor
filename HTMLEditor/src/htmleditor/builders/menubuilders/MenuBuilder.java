/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.HTMLEditor;
import htmleditor.builders.menubuilders.EditMenuBuilder;
import htmleditor.builders.menubuilders.EditMenuBuilder;
import htmleditor.builders.menubuilders.FileMenuBuilder;
import htmleditor.builders.menubuilders.FileMenuBuilder;
import htmleditor.builders.menubuilders.IndentMenuBuilder;
import htmleditor.builders.menubuilders.IndentMenuBuilder;
import htmleditor.builders.menubuilders.InsertMenuBuilder;
import htmleditor.builders.menubuilders.InsertMenuBuilder;
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
        
        
        // Add File menu to menu bar
        menuBar.getMenus().add(new FileMenuBuilder().getProduct(editor));
        //Add Edit menu to menu bar
        menuBar.getMenus().add(new EditMenuBuilder().getProduct(editor)) ;
        //Add Insert menu to menu bar
        menuBar.getMenus().add(new InsertMenuBuilder().getProduct(editor));
        //Add Indent menu to menu bar
        menuBar.getMenus().add(new IndentMenuBuilder().getProduct(editor));
        
        
        // bind width of menu bar to width of associated stage
        menuBar.prefWidthProperty().bind(menuWidthProperty);
        return menuBar;
    }
}
