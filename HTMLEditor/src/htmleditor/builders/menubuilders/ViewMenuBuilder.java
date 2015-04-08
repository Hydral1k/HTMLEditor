/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import htmleditor.LinkViewPane;
import htmleditor.builders.Builder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author thn1069
 */
public class ViewMenuBuilder implements Builder {
    private Menu menu;
    
    
    @Override
    public void build(final HTMLEditor editor) {
        // Prepare 'Edit' drop-down menu
        this.menu = new Menu("View") ;
        this.menu.setStyle("-fx-text-fill: white");
        
        //Copy item
        Menu linkView = new Menu("Link View") ;
        linkView.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN)) ;
        
        MenuItem linkViewShow = new MenuItem("Show");
        linkViewShow.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                editor.showLinkView( true );
            }
        });
        linkView.getItems().add(linkViewShow);
        
        MenuItem linkViewHide = new MenuItem("Hide");
        linkViewHide.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t){
                editor.showLinkView( false );
            }
        });
        linkView.getItems().add(linkViewHide);
        
        this.menu.getItems().add(linkView);
        
        
    }
    
    
    public Menu getProduct(HTMLEditor editor) {
        this.build(editor);
        return this.menu;
    }
}
