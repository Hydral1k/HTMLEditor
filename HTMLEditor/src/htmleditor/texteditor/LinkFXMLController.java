package htmleditor.texteditor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author aac6012
 */
public class LinkFXMLController implements Initializable{
    @FXML private TextField url;
    @FXML private TextField text;
    
    @FXML
    private void handleSubmitAction(ActionEvent event) {
        Tag.url = this.url.getText() ;
        Tag.text = this.text.getText() ;
        Stage stage = (Stage) this.url.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) this.url.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
