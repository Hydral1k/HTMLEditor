/*
 * HTMLText by Jordan Tice
 * Represents a composite pattern leaf node for objectifying a tag in a buffer
 * Contains child functions for consistency, but only raises errors when accessed
*/
package htmleditor.outline;

/**
 *
 * @author jlt8213
 */
public class HTMLText implements HTMLComponent {

    String text;
    
    public HTMLText(String text) {
        this.text = text;
    }

    @Override
    public void add(HTMLComponent htmlComponent) {
        System.err.println("Error - Text object has no children!");
    }

    @Override
    public void remove(HTMLComponent htmlComponent) {
        System.err.println("Error - Text object has no children!");
    }

    @Override
    public HTMLComponent getChild(int i) {
        System.err.println("Error - Text object has no children!");
        return null;
    }

    @Override
    public String getHTML() {
        return this.text;
    }
    
}
