package htmleditor.outline;

import java.util.ArrayList;

/**
 *
 * @author jlt8213
 */
public class HTMLComposite implements HTMLComponent {
    
    private ArrayList<HTMLComponent> children;
    private String closer; //only used w/ tag communication for recursion
    
    public HTMLComposite() {
        this.children = new ArrayList<HTMLComponent>();
        this.closer = "";
    }

    @Override
    public void add(HTMLComponent htmlComponent) {
        this.children.add(htmlComponent);
    }

    @Override
    public void remove(HTMLComponent htmlComponent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HTMLComponent getChild(int i) {
        return children.get(i);
    }

    @Override
    public String getHTML() {
        String newText = "";
        for( int i=0; i<this.children.size(); i++){
            newText += this.children.get(i).getHTML();
        }
        return newText;
    }
    
    public int getLength() { //only for neatness
        return getHTML().length();
    }
    
    public void setCloser(String closer){
        this.closer = closer;
    }
    
    public String getCloser(){
        return this.closer;
    }
    
}
