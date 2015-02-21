/**
 * This can be used for helper functions in the controller.
 * Basically, these methods will insert text into a buffer.
 *
 * @author mss9627 
 */
public class tagMethods {

    public tagMethods(){}
    
    private String insertTabs( int numTabs ){
        String toInsert = "";
        for( int x = 0; x < numTabs; x++ ){
            toInsert = toInsert + "\t";
        }
        return toInsert;
    }
    
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

}