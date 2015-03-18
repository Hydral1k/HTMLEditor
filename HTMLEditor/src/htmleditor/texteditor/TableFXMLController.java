package htmleditor.texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 *
 * @author jlt8213
 */
public class TableFXMLController implements Initializable{
    @FXML private NumberTextField rowCount;
    @FXML private NumberTextField colCount;
    
    @FXML
    private void handleSubmitAction(ActionEvent event) {
        System.out.println(rowCount.getText());
        Tag.TableRows = Integer.parseInt(rowCount.getText());
        Tag.TableCols = Integer.parseInt(colCount.getText());
        Stage stage = (Stage) rowCount.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) rowCount.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
