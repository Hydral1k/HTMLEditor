package htmleditor.outline;

/**
 *
 * @author jlt8213
 */
public interface HTMLComponent {
   
   public void add(HTMLComponent htmlComponent);
   public void remove(HTMLComponent htmlComponent); //consider changing to an array based on change handling
   public HTMLComponent getChild(int i);
   public String getHTML();
   
}
