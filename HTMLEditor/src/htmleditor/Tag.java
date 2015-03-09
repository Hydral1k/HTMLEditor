/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmleditor;

import java.util.ArrayList;

/**
 * A class to represent a tag
 * @author trh8614
 */
public class Tag{
    public static int TableRows = -1;
    public static int TableCols = -1;
    String tag; 
    TagType tag_type;
    String tag_style; // abstract css styling, if so ever in this project
    ArrayList<Tag> tag_children;
    
    public Tag( String tag, TagType tag_type, String tag_style, ArrayList<Tag> tag_children){
        this.tag = tag;
        this.tag_type = tag_type;
        this.tag_style = tag_style;
        this.tag_children = tag_children;
    };
    
    @Override
    public String toString(){
        String HTML = "<" + tag;
        
        if(tag_style.length() > 0){ // Styles detected
            HTML += " style=\"" + tag_style + "\">";
            HTML += (tag_type == TagType.LIST) ? "\n": ""; 
        }else{ // No Styles
            HTML += ">";
            HTML += (tag_type == TagType.LIST) ? "\n": ""; 
        }
        
        for (Tag child_tag : tag_children) {
            HTML += "    " + child_tag.toString();
        }
        HTML += "</" + tag + ">\n";
        
        return HTML;
    };
    /**
     * I forgot to copy and paste this comment. -5 Points. 
     * @param numTabs
     * @return 
     */
    private String insertTabs( int numTabs ){
        String toInsert = "";
        for( int x = 0; x < numTabs; x++ ){
            toInsert = toInsert + "\t";
        }
        return toInsert;
    }
    
    /**
     * Creates an HTML table with the desired number of elements.
     * 
     * @param rows        the number of <tr> tags to add
     * @param cols        the number of <td> tags to add
     * @param initialTabs the initial indentation for the table
     * @return a single string containing all required tags
     */
    public String addTable( int rows, int cols, int initialTabs ){
    
        String table = insertTabs(initialTabs) + "<table>\n";
        
        for( int x = 0; x < rows; x++ ){
            table = table + insertTabs( initialTabs + 1 ) + "<tr>\n";
            for( int y = 0; y < cols; y++ ){
                table = table + insertTabs( initialTabs + 2 ) + "<td></td>\n";
            }
            table = table + insertTabs( initialTabs + 1 ) + "</tr>\n";
        }
        table = table + insertTabs(initialTabs) + "</table>\n";
        
        return table;
        
    }
    
    /**
     * Creates an HTML list for a given number of items.
     * 
     * @param items       the number of <li> tags to add
     * @param initialTabs the indentation of the line where the list is inserted
     * @param isOrdered   adds <ol> tags if true, or <ul> tags if false
     * @return a single string containing all required tags
     */
    public String addList( int items, int initialTabs, boolean isOrdered ){
        
        String list = "";
        if( isOrdered ){
            list = insertTabs( initialTabs ) + "<ol>\n";
        } else {
            list = insertTabs( initialTabs ) + "<ul>\n";
        }
        
        for( int x = 0; x < items; x++ ){
            list = list + insertTabs( initialTabs + 1 ) + "<li></li>\n";
        }
        
        if( isOrdered ){
            list = list + insertTabs( initialTabs ) + "</ol>\n";
        } else {
            list = list + insertTabs( initialTabs ) + "</ul>\n";
        }
        return list;
        
    }
    
    /**
     * Adds a one-line tag of any type.
     * 
     * @param initialTabs the initial indentation for the given line
     * @param tag         the text inside of the flag, e.g. a, div, span
     * @return the given tag as a string, with its closing tag
     */
    public String addSingleTag( String tag, int initialTabs ){
        if( tag.equals("br") ){
            return insertTabs(initialTabs) + "<br />";
        }
        return insertTabs(initialTabs) + "<" + tag + "></" + tag + ">\n";
    }
}

