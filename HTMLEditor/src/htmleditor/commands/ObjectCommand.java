/*
 * The Command to make objects out of a buffer
 */
package htmleditor.commands;

import htmleditor.HTMLEditor;
import htmleditor.outline.HTMLComposite;
import htmleditor.outline.HTMLTag;
import htmleditor.outline.HTMLText;
import htmleditor.outline.StateEnum;
import static htmleditor.outline.StateEnum.*;
import javafx.event.Event;
import javafx.scene.control.TextArea;



/**
 *
 * @author jlt8213
 */
public class ObjectCommand implements Command {
    HTMLEditor editor ;
    
    
    public ObjectCommand(HTMLEditor editor){
        this.editor = editor;
    }
    
    public void execute(Event t){
        System.out.println("Objectify stub");
        TextArea thisTA = editor.getText();
        HTMLComposite bufferComposite = createComposite(thisTA.getText());
        System.out.println("This composite:");
        System.out.println(bufferComposite.getHTML());
        System.out.println("End composite");
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
        boolean closingTag = false;
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
                            newTag.add(newComposite); //broke?
                            i += newLength;
                            i += newComposite.getCloser().length() - 1;
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
        System.out.printf("original strings are %s and %s\n", tag1, tag2);
        tag1 = tag1.replace("<", "");
        tag1 = tag1.replace(">", "");
        tag2 = tag2.replace("</", "");
        tag2 = tag2.replace(">", "");
        
        System.out.printf("Checking if %s matches %s\n", tag1, tag2);
        return tag1.equals(tag2);
    }
    
}