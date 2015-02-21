/**
 * This can be used for helper functions in the controller.
 * Basically, these methods will insert text into a buffer.
 *
 * @author mss9627 
 */

package htmleditor;

public class tagMethods {

    public tagMethods(){}
    
    /**
     * Inserts leading tabs before the elements to be added.
     * 
     * @param numTabs the amount of '\t' characters to place
     * @return a string containing only tab characters
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

}