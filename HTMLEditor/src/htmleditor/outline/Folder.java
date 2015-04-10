package htmleditor.outline;

import htmleditor.HTMLEditor;
import static htmleditor.outline.StateEnum.*;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextArea;

/**
 *
 * @author jlt8213
 * The plan is for this to maintain a dictionary of folded elements.
 * this must add new elements, maintain the offset of existing elements, and 
 * handle unfolding
 */
public class Folder {
    Map<Integer, HTMLTag> folderMap = new HashMap<Integer, HTMLTag>();
    Map<HTMLTag, Integer> tagPositions = new HashMap<HTMLTag, Integer>();
    
    /*NOTE
        - tagPositions must be updated any time text is changed!
        - consider making folderMap store the position instead of the line number?
            would have to calculate line number to toggle, but would allow change
            otherwise line numbers in folderMap will need to update as well
    */
    
    public boolean foldLine(Integer lineNumber){
        System.out.println(folderMap.size() + " items in folderman\n" + tagPositions.size() + " positions");
        if(folderMap.containsKey(lineNumber))
            return unfold(lineNumber);
        else{
            return addToFolder(lineNumber);
        }
    }
    public boolean unfold(Integer lineNumber){
        HTMLTag unfoldTag = folderMap.get(lineNumber);
        TextArea buffer = HTMLEditor.getInstance().getText();
        Integer tagPosition = tagPositions.get(unfoldTag);
        String bufText = buffer.getText();
        bufText = bufText.substring(0, tagPosition) + bufText.substring(unfoldTag.getHTML().length());
        unfoldTag.collapseToggle();
        bufText = bufText.substring(0, tagPosition) + unfoldTag.getHTML() + bufText.substring(tagPosition);
        buffer.setText(bufText);
        folderMap.remove(lineNumber);
        tagPositions.remove(unfoldTag);
        return true;
        
    }
    public boolean addToFolder(int lineNumber){
        System.out.println("folding line " + lineNumber);
        TagTuple foundTag = findFirstTag(lineNumber);
        if(foundTag.getTag().isEmpty()){
            return false;
        }
        System.out.println(foundTag.getTag() + " is at index " + foundTag.getTagStart() + " until " + foundTag.getTagEnd());
        String thisText = HTMLEditor.getInstance().getText().getText();
        HTMLComposite thisTagComp = createCompositeRecursive(thisText.substring(foundTag.getTagEnd()+1), foundTag.getTag());
        if(thisTagComp.getCloser().isEmpty())
            return false;
        HTMLTag thisTag = new HTMLTag(foundTag.getTag());
        thisTag.setCloseTag(thisTagComp.getCloser());
        thisTag.add(thisTagComp);
        
        thisText = thisText.substring(0, foundTag.getTagStart()) + foundTag.getTag() + thisText.substring(thisTagComp.getHTML().length());
        HTMLEditor.getInstance().getText().setText(thisText);
        thisTag.collapseToggle();
        folderMap.put(lineNumber, thisTag );
        tagPositions.put(thisTag, foundTag.getTagStart());
        System.out.println("the HTMLTag html is:\n" + thisTag.getHTML());
        return true;
    }
    
    
    public TagTuple findFirstTag(int lineNumber){
        String thisText = HTMLEditor.getInstance().getText().getText();
        int lines = 0;
        String tag = "";
        lineNumber --;
        int tagIndex;
        for(int i=0; i< thisText.length(); i++){
            if(lines == lineNumber){
                if(thisText.charAt(i) == '<'){
                    tag += '<';
                    tagIndex = i;
                    i++;
                    while(i < thisText.length()){ //while inside tag
                        if(thisText.charAt(i) == '\n') //tag doesn't finish on this line
                            return new TagTuple("", 0, 0);
                        if(thisText.charAt(i) == '>'){
                            return new TagTuple(tag + ">", tagIndex, i);
                        }
                        if( (tag.length() == 1 ) && thisText.charAt(i) == '/' ){
                            tag = "";
                            break;
                        }
                        tag += thisText.charAt(i);
                        i++;
                    }
                }
            }
            if(thisText.charAt(i) == '\n'){
                lines++;
            }
        }
        return new TagTuple("", 0, 0);
    }
    
    public HTMLComposite createComposite(String buffer){ //RECUSION YO
        return createCompositeRecursive(buffer, "");
        
    }
    
    /* Probably add in a check for first line number, save w/ tag */
    public HTMLComposite createCompositeRecursive(String buffer, String closeTag){
        HTMLComposite thisComposite = new HTMLComposite();
        int i = 0;
        StateEnum state = INTEXT; 
        String tempText = "";
        String tempTag = "";
        boolean closingTag = false; //shouldn't need its own state
        while(state != EXIT){
            assert(i < buffer.length());
            switch(state){
                case INTEXT: //regular text
                    assert(tempTag.isEmpty());
                    if( i >= buffer.length()){
                        if(!tempText.isEmpty()){
                            thisComposite.add(new HTMLText(tempText));
                        }
                        state = EXIT;
                        break;
                    }
                    else if(buffer.charAt(i) == '<'){
                        tempTag += '<';
                        if(tempText.isEmpty()){
                            state = INBRACKET;
                        }
                        else{
                            state = SAVETEXT;//check
                        }
                    }
                    else{
                        tempText += buffer.charAt(i);
                    }
                    break;
                case INBRACKET: //inside tag brackets. **if no close is found, convert to HTMLText
                    //should also check for tag inside tag
                    if(i > buffer.length()){
                        state = EXIT; //write out tag!
                        break; 
                    }
                    else if(buffer.charAt(i) == '/'){
                        closingTag = true;
                        tempTag += '/';
                    }
                    else if(buffer.charAt(i) == '>'){
                        tempTag += '>';
                        state = SAVETAG;
                    }
                    else{ tempTag += buffer.charAt(i); }
                    break;
                case SAVETEXT: //save HTMLText object
                    thisComposite.add(new HTMLText(tempText));
                    tempText = "";
                    i--;
                    if(!tempTag.isEmpty())
                        state = INBRACKET;
                    else{ state = INTEXT; }
                    break;
                case SAVETAG: //finished tag object
                    /* thoughts:
                    recursion should occur here, create HTMLTag with 
                        child being HTMLComposite from recursive call
                    needs a way to only parse until closing tag from original 
                        tag is reached, possibly by modifying recursive function 
                        or probably adding helper function
                    tag won't always be closed, if not it should just return the HTMLText
                    */
                    if(closingTag){
                        closingTag = false;
                        //System.out.println(tagMatches(closeTag, tempTag));
                        if(!tagMatches(closeTag, tempTag)){
                            tempText += tempTag;
                            tempTag = "";
                        }
                        else{
                            thisComposite.setCloser(tempTag); //pass the closing tag back for HTMLTag object
                            return thisComposite;
                        }
                        state = INTEXT;
                        break;
                    }
                    HTMLTag newTag = new HTMLTag(tempTag);
                    if(i < buffer.length()){
                        HTMLComposite newComposite = createCompositeRecursive(buffer.substring(i), tempTag); //recursive
                        if(newComposite.getCloser() != "")
                            newTag.setCloseTag(newComposite.getCloser());
                        //else convert opening tag to text cause it isn't a complete tag, keep composite in case of further tags
                        int newLength = newComposite.getLength();
                        if(newLength > 0){
                            newTag.add(newComposite);
                            i += newLength;
                            i += newComposite.getCloser().length() - 1;
                            return newTag; //FOR FOLDER ONLY
                        }
                    }
                    thisComposite.add(newTag);
                    tempTag = "";
                    state = INTEXT;
                    break;
                default:
                    System.err.println("Invalid state, ya dingus!");
                    break;
            }
            i++;
        }
        
        return thisComposite; 
    }
    
    private boolean tagMatches(String tag1, String tag2){
        /*still needs to strip whitespace, check for special html cases */
        //System.out.printf("original strings are %s and %s\n", tag1, tag2);
        tag1 = tag1.replace("<", "");
        tag1 = tag1.replace(">", "");
        tag2 = tag2.replace("</", "");
        tag2 = tag2.replace(">", "");
        
        //System.out.printf("Checking if %s matches %s\n", tag1, tag2);
        return tag1.equals(tag2);
    }
    
}

class TagTuple{
    private String tag;
    private int tagStart;
    private int tagEnd;

    public TagTuple(String tag, int tagStart, int tagEnd) {
        this.tag = tag;
        this.tagStart = tagStart;
        this.tagEnd = tagEnd;
    }

    public String getTag() {
        return tag;
    }

    public int getTagStart() {
        return tagStart;
    }

    public int getTagEnd() {
        return tagEnd;
    }

    
    
}