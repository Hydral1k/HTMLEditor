package htmleditor.outline;

import htmleditor.HTMLEditor;
import static htmleditor.outline.StateEnum.EXIT;
import static htmleditor.outline.StateEnum.INBRACKET;
import static htmleditor.outline.StateEnum.INTEXT;
import static htmleditor.outline.StateEnum.SAVETAG;
import static htmleditor.outline.StateEnum.SAVETEXT;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    
    public void updateFolder(){
        // stub
        // iterate through map and positions, update based on change, 
        // preferably update sidebar too
    }
    
    public boolean foldLine(Integer lineNumber){
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
        Integer tagLength = unfoldTag.getHTML().length();
        String bufText = buffer.getText();
        unfoldTag.collapseToggle();
        bufText = bufText.substring(0, tagPosition) + unfoldTag.getHTML() + bufText.substring(tagPosition + tagLength);
        buffer.setText(bufText);
        folderMap.remove(lineNumber);
        tagPositions.remove(unfoldTag);
        return true;
        
    }
    public void unfoldAll(){
        
        while(!folderMap.isEmpty()){
            
            Set keys = folderMap.keySet();
            Object[] keyArray = keys.toArray();
            System.out.println("unfolding line " + (Integer)keyArray[0]);
            unfold((Integer)keyArray[0]);
            
        }
    }
    public boolean addToFolder(int lineNumber){
        TagTuple foundTag = findFirstTag(lineNumber);
        if(foundTag.getTag().isEmpty()){
            return false;
        }
        String thisText = HTMLEditor.getInstance().getText().getText();
        HTMLComposite thisTagComp = createCompositeRecursive(thisText.substring(foundTag.getTagEnd()+1), foundTag.getTag());
        if(thisTagComp.getCloser().isEmpty())
            return false;
        HTMLTag thisTag = new HTMLTag(foundTag.getTag());
        thisTag.setCloseTag(thisTagComp.getCloser());
        thisTag.add(thisTagComp);
        thisText = thisText.substring(0, foundTag.getTagStart()) + foundTag.getTag() + thisText.substring(foundTag.getTagStart()+thisTag.getHTML().length());
        HTMLEditor.getInstance().getText().setText(thisText);
        thisTag.collapseToggle();
        folderMap.put(lineNumber, thisTag );
        tagPositions.put(thisTag, foundTag.getTagStart());
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
                    if(i > buffer.length()){
                        state = EXIT; //write out tag?
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
                    if(closingTag){
                        closingTag = false;
                        if(tagMatches(closeTag, tempTag)){
                            thisComposite.setCloser(tempTag); //pass the closing tag back for HTMLTag object
                            return thisComposite;
                        }
                        tempText += tempTag;
                        tempTag = "";
                        i--;
                        state = INTEXT;
                        break;
                    } 
                    HTMLTag newTag = new HTMLTag(tempTag);
                    if(i < buffer.length()){
                        HTMLComposite newComposite = createCompositeRecursive(buffer.substring(i), tempTag); //recursive
                        if(newComposite.getCloser() != ""){ //it matched, these are the droids we're looking for
                            if(tagMatches(newComposite.getCloser(), closeTag)){
                                newTag.setCloseTag(newComposite.getCloser());
                                newTag.add(newComposite);
                                thisComposite.add(newTag);
                                thisComposite.setCloser(newComposite.getCloser());
                                return thisComposite;
                            }
                            newTag.setCloseTag(newComposite.getCloser());
                            int newLength = newComposite.getLength();
                            if(newLength > 0){
                                newTag.add(newComposite);
                            }
                            i += newTag.getLength() - tempTag.length() ;
                            thisComposite.add(newTag);
                        }
                        else{
                            thisComposite.add(newComposite);//new HTMLText(tempTag));
                            i += newComposite.getHTML().length();
                            //i--;
                        }
                    }
                    
                    tempTag = "";
                    i--;
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
        tag1 = tag1.replace("<", "");
        tag1 = tag1.replace(">", "");
        tag2 = tag2.replace("</", "");
        tag2 = tag2.replace(">", "");
        tag1 = tag1.replace(" ", "");
        tag2 = tag2.replace(" ", "");
        
        if(tag1.charAt(0) == 'a'){
            tag1 = "a";
        }
        
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