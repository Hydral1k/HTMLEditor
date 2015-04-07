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
