/*
 * Stores data about each tab
 * This can be very useful! Would've made things easier if I'd thought of it earlier
 */
package htmleditor;

/**
 *
 * @author jlt8213
 */
public class TabData {

    private boolean wordWrap = true;
    private boolean autoIndent = true;
    private int indentSpace = 4; //default indent length

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public boolean isWordWrap() {
        return wordWrap;
    }
}
