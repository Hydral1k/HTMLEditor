/*
Any component of the HTML Composite object that is created when objectifying
the HTMLBuffer or a single tag for folding. The interface includes child functions
for continuity and anonymity when referring to objects, but leaf nodes will only
return null if these are accessed.
*/
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
