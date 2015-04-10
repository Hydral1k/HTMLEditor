package htmleditor.outline;

import java.util.ArrayList;

/**
 *
 * @author jlt8213
 * represents a tag object - composite object in composite pattern
 */
public class HTMLTag extends HTMLComposite{

    private ArrayList<HTMLComponent> children;
    private String tag;
    private String closeTag;
    private boolean isCollapsed;
    
    public HTMLTag(String tag) {
        super();
        this.tag = tag;
        this.closeTag = "";
        this.children = new ArrayList<>();
        this.isCollapsed = false;
    }
    
    @Override
    public void add(HTMLComponent htmlComponent) {
        this.children.add(htmlComponent);
    }
    
    public void collapseToggle(){
        isCollapsed = !isCollapsed;
    }
    
    public void setCloseTag(String closeTag){
        this.closeTag = closeTag;
    }

    @Override
    public String getHTML() {
        if(isCollapsed)
            return(tag);
        String newText = tag;
        //newText += super.getHTML();
        for( int i=0; i < children.size(); i++ )
            newText += children.get(i).getHTML();
        newText += closeTag; 
        return newText;
    }
    
}
