/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import java.io.File;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 

/**
 *  Currently NOT IMPLEMENTED. Possibly in the future?
 * 
 * @author trh8614
 */
public class UserManager {
    
    final static String UserSettingsRoot = "C:\\HTMLEditor\\settings.xml";
    
    
    public static void init(){
        
    }
    
    public static boolean makeConfig( boolean autoIndent, boolean wordWrap, boolean noTabs){
        try{
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // make XML config
            
            Document settings = docBuilder.newDocument();
            Element settingsElement = settings.createElement("Settings");
            
            // Options
            Element autoIndentElement = settings.createElement("AutoIndent");
            autoIndentElement.setNodeValue(String.valueOf(autoIndent));
            
            Element wordWrapElement = settings.createElement("WordWrap");
            wordWrapElement.setNodeValue(String.valueOf(wordWrap));
            
            Element noTabElement = settings.createElement("NoTabs");
            noTabElement.setNodeValue(String.valueOf(noTabs));
            
            settingsElement.appendChild(autoIndentElement);
            settingsElement.appendChild(wordWrapElement);
            settingsElement.appendChild(noTabElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(settings);
            StreamResult result = new StreamResult(new File(UserSettingsRoot));
            
            transformer.transform(source, result);
            System.out.println("User Config Saved! Location : " + UserSettingsRoot);
                    
            return true;
            
        }catch(ParserConfigurationException pc){
            
            // popup window declaring the error.
            
            return false;
        }catch(TransformerException tfe){
            
            // another stupid popup of some stuff
        }
        
        // some odd error occured.
        return false;
        
    }
    
    public static boolean setConfig(){
        return true;
    }
    
    public static void getConfig(){
        
    }
    
}
