package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.builders.Builder;
import htmleditor.commands.CopyCommand;
import htmleditor.commands.CutCommand;
import htmleditor.commands.MyEventHandler;
import htmleditor.commands.PasteCommand;
import htmleditor.commands.RedoCommand;
import htmleditor.commands.UndoCommand;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author thn1069
 */
public class EditMenuBuilder implements Builder {
    private Menu menu;
    
    
    @Override
    public void build(HTMLEditor editor) {
        // Prepare 'Edit' drop-down menu
        this.menu = new Menu("Edit") ;
        this.menu.setStyle("-fx-text-fill: white");
        
        //Copy item
        MenuItem copyItem = new MenuItem("Copy") ;
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)) ;
        copyItem.setOnAction(new MyEventHandler(new CopyCommand(editor))) ;
        this.menu.getItems().add(copyItem) ;
        
        //Cut item
        MenuItem cutItem = new MenuItem("Cut") ;
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN)) ;
        cutItem.setOnAction(new MyEventHandler(new CutCommand(editor))) ;
        this.menu.getItems().add(cutItem) ;
        
        //Paste item
        MenuItem pasteItem = new MenuItem("Paste") ;
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN)) ;
        pasteItem.setOnAction(new MyEventHandler(new PasteCommand(editor))) ;
        this.menu.getItems().add(pasteItem) ;

        //Undo item
        MenuItem undoItem = new MenuItem("Undo") ;
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN)) ;
        undoItem.setOnAction(new MyEventHandler(new UndoCommand(editor))) ;
        this.menu.getItems().add(undoItem) ;

        //Redo item
        MenuItem redoItem = new MenuItem("Redo") ;
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN)) ;
        redoItem.setOnAction(new MyEventHandler(new RedoCommand(editor))) ;
        this.menu.getItems().add(redoItem) ;

    }
    
    
    public Menu getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.menu;
    }
}
