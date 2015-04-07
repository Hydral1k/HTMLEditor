/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.commands.IndentCommand;
import htmleditor.commands.MyEventHandler;
import htmleditor.texteditor.IndentType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author thn1069
 */
public class IndentMenuBuilder implements Builder {
    private Menu menu;
    
    
    @Override
    public void build(HTMLEditor editor) {
        // Indent Menu
        this.menu = new Menu("Indent");
        final MenuItem indentCurrentLine = new MenuItem("Ident Current Line");
        indentCurrentLine.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_CURRENT_LINE)));
        final MenuItem indentSelection = new MenuItem("Indent Selection");
        indentSelection.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_SELECTION)));
        final MenuItem indentAll = new MenuItem("Indent All");
        indentAll.setOnAction(new MyEventHandler(new IndentCommand(editor, IndentType.INDENT_ALL)));
        this.menu.getItems().addAll(indentCurrentLine, indentSelection, indentAll);
    }

    
    public Menu getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.menu;
    }
}
