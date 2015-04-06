/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor.builders;

import htmleditor.HTMLEditor;
import javafx.scene.control.Menu;

/**
 *
 * @author thn1069
 */
public interface Builder {
    public void build(HTMLEditor editor);
    
    public Menu getProduct();
}
