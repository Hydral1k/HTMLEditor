/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.commands.InsertCommand;
import htmleditor.commands.MyEventHandler;
import htmleditor.texteditor.TagType;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author thn1069
 */
public class InsertMenuBuilder implements Builder {
    private Menu menu;
    
    
    @Override
    public void build(HTMLEditor editor) {
        // Prepare 'Insert' drop-down menu
        this.menu = new Menu("Insert");
        this.menu.setStyle("-fx-text-fill: white");

        //Bold item
        MenuItem boldItem = new MenuItem("Bold") ;
        boldItem.setAccelerator(new KeyCodeCombination(KeyCode.B,KeyCombination.CONTROL_DOWN));
        boldItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.BOLD)));
        this.menu.getItems().add(boldItem);

        //Italics item
        MenuItem italicsItem = new MenuItem("Italics") ;
        italicsItem.setAccelerator(new KeyCodeCombination(KeyCode.I,KeyCombination.CONTROL_DOWN));
        italicsItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.ITALICS)));
        this.menu.getItems().add(italicsItem);

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
        this.menu.getItems().add(headerItem);

        //Table item
        MenuItem tableItem = new MenuItem("Table") ;
        tableItem.setAccelerator(new KeyCodeCombination(KeyCode.T,KeyCombination.CONTROL_DOWN));
        tableItem.setOnAction(new MyEventHandler(new InsertCommand(TagType.TABLE)));
        this.menu.getItems().add(tableItem);

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
        this.menu.getItems().add(listItem);
    }

    
    @Override
    public Menu getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.menu;
    }
}
