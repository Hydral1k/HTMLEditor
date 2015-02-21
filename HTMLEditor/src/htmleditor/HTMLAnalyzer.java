/*
 * A model used for analyzing each HTML Editor session and 
 * verify if each is in proper form.
 *
 *
 * @author trh8614
 */
package htmleditor;

/**
 * Main Analyzer Class
 */
public class HTMLAnalyzer {
    
    /**
     * Some fancy comment about what this does
     * @param bufferHTML
     * @return 
     */
    public boolean wellFormed( String bufferHTML ){
        
        if(true){ // some sort of stuff to check if the HTML is good, maybe a regex function
            
            return true;
            
        }else{
            
            return false;
            
        }
    }
    
    /**
     * Another fancy comment about what this does
     * @param bufferHTML
     * @return 
     */
    public String autoIndent( String bufferHTML ){
        
        // we return bufferHTML after it has been properly indented. 
        
        return bufferHTML;
    }
    
    // more functions will go in here. check the Domain Model if you would
    // like to add more and add the functions in there
    
}
