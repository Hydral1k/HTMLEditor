package htmleditor.builders.menubuilders;

import htmleditor.HTMLEditor;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.MenuBar;

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
        // Add View menu to menu bar
        menuBar.getMenus().add(new ViewMenuBuilder().getProduct(editor));
        
        
        // bind width of menu bar to width of associated stage
        menuBar.prefWidthProperty().bind(menuWidthProperty);
        return menuBar;
    }
}
